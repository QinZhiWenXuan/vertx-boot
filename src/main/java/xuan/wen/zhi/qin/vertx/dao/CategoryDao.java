package xuan.wen.zhi.qin.vertx.dao;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xuan.wen.zhi.qin.core.vertx.jdbc.VertxDao;

import java.util.List;

@Component
public class CategoryDao {
    @Autowired
    private VertxDao vertxDao;
    @Autowired
    private JsonObject categorySql;

    public void query(HttpServerResponse response, Handler<List<JsonObject>> event) {
        final JsonArray params = new JsonArray().add(12).add(1).add("java");
        vertxDao.getConnection(response, fn -> {
            vertxDao.updateWithParams(fn, categorySql.getString("save"), params, response, handle -> {
                vertxDao.query(fn, categorySql.getString("query"), response, result -> {
                    vertxDao.commit(fn, response, commit -> {
                        event.handle(result.getRows());
                    });
                });
            });
        });
    }

    public void pager(HttpServerResponse response, Handler<String> event) {
        vertxDao.getConnection(response, fn -> {
            vertxDao.query(fn, categorySql.getString("pager"), response, pager -> {
                vertxDao.query(fn, categorySql.getString("query"), response, result -> {
                    JsonObject json = new JsonObject().put("list", result.getRows()).put("total", pager.getRows().get(0));
                    vertxDao.commit(fn, response, commit -> {
                        event.handle(json.toString());
                    });
                });
            });
        });
    }
}
