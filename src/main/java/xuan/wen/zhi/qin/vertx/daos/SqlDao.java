package xuan.wen.zhi.qin.vertx.daos;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.UpdateResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlDao extends BasicDao {
    private static final Logger logger = LoggerFactory.getLogger(SqlDao.class);

    protected Handler<AsyncResult<ResultSet>> handlerResultSet(SQLConnection connection, Handler<ResultSet> handler, HttpServerResponse response) {
        return call -> {
            try {
                if (call.failed()) {
                    this.rollbackHandler(connection, call.cause(), response);
                } else {
                    handler.handle(call.result());
                    super.commit(connection, succeed -> {
                    }, response);
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getLocalizedMessage());
                this.rollbackHandler(connection, e, response);
            }
        };
    }

    protected void rollbackHandler(SQLConnection connection, Throwable cause, HttpServerResponse response) {
        super.rollback(connection, fail -> {
            this.errorHandler(response, cause);
            return;
        }, response);
    }

    protected Handler<AsyncResult<UpdateResult>> handlerUpdateResult(SQLConnection connection, Handler<UpdateResult> handler, HttpServerResponse response) {
        return call -> {
            try {
                if (call.failed()) {
                    this.rollbackHandler(connection, call.cause(), response);
                } else {
                    handler.handle(call.result());
                    super.commit(connection, succeed -> {
                        logger.info("commit succeed");
                    }, response);
                }
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage());
                this.rollbackHandler(connection, e.getCause(), response);
            }
        };
    }


    protected void query(String sql, Handler<ResultSet> handler, HttpServerResponse response) {
        super.getConnection(fn -> {
            this.query(fn, sql, handler, response);
        }, response);
    }

    protected void query(SQLConnection connection, String sql, Handler<ResultSet> handler, HttpServerResponse response) {
        connection.query(sql, this.handlerResultSet(connection, handler, response));
    }

    protected void queryWithParams(String sql, JsonArray params, Handler<ResultSet> handler, HttpServerResponse response) {
        logger.debug("sql \t {}", sql);
        super.getConnection(fn -> {
            this.queryWithParams(fn, sql, params, handler, response);
        }, response);
    }

    protected void queryWithParams(SQLConnection connection, String sql, JsonArray params, Handler<ResultSet> handler, HttpServerResponse response) {
        logger.debug("sql \t {}", sql);
        connection.queryWithParams(sql, params, this.handlerResultSet(connection, handler, response));
    }


    protected void update(String sql, Handler<UpdateResult> handler, HttpServerResponse response) {
        super.getConnection(fn -> {
            this.update(fn, sql, handler, response);
        }, response);
    }

    protected void update(SQLConnection connection, String sql, Handler<UpdateResult> handler, HttpServerResponse response) {
        logger.debug("sql \t {}", sql);
        connection.update(sql, this.handlerUpdateResult(connection, handler, response));
    }

    protected void updateWithParams(String sql, JsonArray params, Handler<UpdateResult> handler, HttpServerResponse response) {
        super.getConnection(fn -> {
            this.updateWithParams(fn, sql, params, handler, response);
        }, response);
    }

    protected void updateWithParams(SQLConnection connection, String sql, JsonArray params, Handler<UpdateResult> handler, HttpServerResponse response) {
        logger.debug("sql \t {}", sql);
        connection.updateWithParams(sql, params, this.handlerUpdateResult(connection, handler, response));
    }
}
