package xuan.wen.zhi.qin.vertx.daos;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class BasicDao {
    private static final Logger logger = LoggerFactory.getLogger(BasicDao.class);
    @Autowired
    protected JDBCClient jdbcClient;

    protected void errorHandler(HttpServerResponse response, Throwable cause) {
        if (!response.ended()) {
            logger.error("sql error : ", cause);
            JsonObject error = new JsonObject().put("code", 500).put("message", "sql error");
            response.putHeader("Content-Type", "application/json").end(error.toBuffer());
        } else {
            return;
        }
    }


    protected void getConnection(Handler<SQLConnection> connectionHandler, HttpServerResponse response) {
        this.jdbcClient.getConnection(async -> {
            if (async.succeeded()) {
                SQLConnection connection = async.result();
                this.startTransaction(connection, connectionHandler, response);
            } else {
                this.errorHandler(response, async.cause());
                return;
            }
        });
    }

    protected void startTransaction(SQLConnection connection, Handler<SQLConnection> connectionHandler, HttpServerResponse response) {
        Optional.of(connection).ifPresent(consumer -> {
            consumer.setAutoCommit(false, call -> {
                if (call.failed()) {
                    this.errorHandler(response, call.cause());
                    return;
                } else {
                    connectionHandler.handle(consumer);
                }
            });
        });

    }

    protected void commit(SQLConnection connection, Handler<Void> handler, HttpServerResponse response) {
        Optional.of(connection).ifPresent(consumer -> {
            consumer.commit(this.transactionHandler(connection, handler, response));
        });

    }

    protected void rollback(SQLConnection connection, Handler<Void> handler, HttpServerResponse response) {
        Optional.of(connection).ifPresent(consumer -> {
            consumer.rollback(this.transactionHandler(connection, handler, response));
        });

    }

    protected Handler<AsyncResult<Void>> transactionHandler(SQLConnection connection, Handler<Void> handler, HttpServerResponse response) {
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
                this.errorHandler(response, e);
                return;
            }
        };
    }

    protected void close(SQLConnection connection, HttpServerResponse response) {
        Optional.of(connection).ifPresent(consumer -> {
            consumer.close(fn -> {
                if (fn.failed()) {
                    this.errorHandler(response, fn.cause());
                    return;
                }
            });
        });
    }
}
