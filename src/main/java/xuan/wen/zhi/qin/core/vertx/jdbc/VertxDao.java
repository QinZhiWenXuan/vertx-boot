package xuan.wen.zhi.qin.core.vertx.jdbc;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.UpdateResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import xuan.wen.zhi.qin.core.vertx.handler.web.ResponseHandler;

import java.util.Optional;

@Component
public class VertxDao {
    private static final Logger logger = LoggerFactory.getLogger(VertxDao.class);
    @Autowired
    @Qualifier(value = "dataSourceClient")
    protected JDBCClient jdbcClient;
    @Autowired
    protected ResponseHandler responseHandler;

    protected void errorHandler(final HttpServerResponse response, final Throwable cause) {
        logger.error("sql error : ", cause);
        JsonObject error = new JsonObject().put("code", 500).put("message", "sql error");
        this.responseHandler.response(response, error.toString());
    }

    protected Handler<AsyncResult<Void>> resultHandler(final HttpServerResponse response, final Handler<Void> handler) {
        return call -> {
            if (call.failed()) {
                this.errorHandler(response, call.cause());
                return;
            }
            if (null != handler)
                try {
                    handler.handle(null);
                } catch (Exception e) {
                    this.errorHandler(response, e);
                }
        };
    }

    protected <T> Handler<AsyncResult<T>> resultHandler(final HttpServerResponse response, final SQLConnection connection, final Handler<T> handler) {
        return call -> {
            try {
                if (call.failed()) {
                    this.rollbackHandler(connection, call.cause(), response);
                    return;
                } else {
                    handler.handle(call.result());
                }
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage());
                this.rollbackHandler(connection, e, response);
            }
        };
    }

    public Handler<AsyncResult<Void>> transactionHandler(final SQLConnection connection, final HttpServerResponse response, final Handler<Void> handler) {
        return call -> {
            try {
                if (call.failed()) {
                    this.close(connection, response);
                    this.errorHandler(response, call.cause());
                    return;
                } else {
                    this.close(connection, response);
                    handler.handle(null);
                }
            } catch (Exception e) {
                this.close(connection, response);
                this.errorHandler(response, e);
                return;
            }
        };
    }

    public void getConnection(final HttpServerResponse response, final Handler<SQLConnection> connectionHandler) {
        this.jdbcClient.getConnection(call -> {
            if (call.failed()) {
                this.errorHandler(response, call.cause());
                return;
            }
            this.startTransaction(call.result(), response, connectionHandler);
        });
    }

    public void startTransaction(final SQLConnection connection, final HttpServerResponse response, final Handler<SQLConnection> connectionHandler) {
        Optional.of(connection).ifPresent(consumer -> {
            consumer.setAutoCommit(false, this.resultHandler(response, call -> {
                connectionHandler.handle(consumer);
            }));
        });
    }

    public void commit(final SQLConnection connection, final HttpServerResponse response, final Handler<Void> handler) {
        Optional.of(connection).ifPresent(consumer -> {
            consumer.commit(this.transactionHandler(connection, response, handler));
        });
    }

    public void rollback(final SQLConnection connection, final HttpServerResponse response, final Handler<Void> handler) {
        Optional.of(connection).ifPresent(consumer -> {
            consumer.rollback(this.transactionHandler(connection, response, handler));
        });
    }

    public void rollbackHandler(final SQLConnection connection, final Throwable cause, final HttpServerResponse response) {
        this.rollback(connection, response, fail -> {
            this.errorHandler(response, cause);
            return;
        });
    }

    public void close(final SQLConnection connection, final HttpServerResponse response) {
        Optional.of(connection).ifPresent(consumer -> {
            consumer.close(this.resultHandler(response, null));
        });
    }

    public void query(final SQLConnection connection, final String sql, final HttpServerResponse response, final Handler<ResultSet> handler) {
        logger.info("sql \t {}", sql);
        connection.query(sql, this.resultHandler(response, connection, handler));
    }

    public void queryWithParams(final SQLConnection connection, final String sql, final JsonArray params, final HttpServerResponse response, final Handler<ResultSet> handler) {
        logger.info("sql \t {}", sql);
        connection.queryWithParams(sql, params, this.resultHandler(response, connection, handler));
    }

    public void update(final SQLConnection connection, final String sql, final HttpServerResponse response, final Handler<UpdateResult> handler) {
        logger.info("sql \t {}", sql);
        connection.update(sql, this.resultHandler(response, connection, handler));
    }

    public void updateWithParams(final SQLConnection connection, final String sql, final JsonArray params, final HttpServerResponse response, final Handler<UpdateResult> handler) {
        logger.info("sql \t {}", sql);
        connection.updateWithParams(sql, params, this.resultHandler(response, connection, handler));
    }
}
