package xuan.wen.zhi.qin.vertx.services;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xuan.wen.zhi.qin.vertx.daos.CategoryDao;

@Component
public class CategoryService extends BasicService {
    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);
    @Autowired
    private CategoryDao categoryDao;

    @Override
    protected void deploy() {
        this.router.get("/vertx/category").handler(this::all);
        this.router.post("/vertx/category").handler(this::save);
    }

    private void all(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        this.categoryDao.pager(event -> {
            this.response(response, event.toString());
        }, response);
    }

    private void save(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        this.categoryDao.save(event -> {
            JsonObject result = new JsonObject().put("code", event.intValue() > 0 ? 200 : 500);
            this.response(response, result.toString());
        }, response);
    }
}
