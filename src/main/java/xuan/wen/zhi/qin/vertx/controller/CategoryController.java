package xuan.wen.zhi.qin.vertx.controller;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xuan.wen.zhi.qin.core.vertx.web.VertxController;
import xuan.wen.zhi.qin.vertx.dao.CategoryDao;

@Component
public class CategoryController extends VertxController {
    @Autowired
    private CategoryDao categoryDao;

    @Override
    protected void deploy() {
        this.router.get("/category/all").handler(this::all);
        this.router.get("/category/get").handler(this::get);
    }

    private void all(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        categoryDao.query(routingContext.response(), event -> {
            super.response(response, event.toString());
        });
    }
    private void get(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        categoryDao.pager(routingContext.response(), event -> {
            super.response(response, event.toString());
        });
    }
}
