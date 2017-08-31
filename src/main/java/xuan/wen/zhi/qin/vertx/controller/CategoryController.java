package xuan.wen.zhi.qin.vertx.controller;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xuan.wen.zhi.qin.core.vertx.factory.EventBusFactory;
import xuan.wen.zhi.qin.core.vertx.web.VertxController;
import xuan.wen.zhi.qin.vertx.dao.CategoryDao;

@Component
public class CategoryController extends VertxController {
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private EventBusFactory eventBusFactory;

    @Override
    protected void deploy() {
        this.router.get("/category/all").handler(this::all);
        this.router.get("/category/get").handler(this::get);
        this.router.get("/vertx/publish").handler(this::publish);
        this.router.get("/vertx/send").handler(this::send);
        this.consumer();

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

    private void publish(RoutingContext routingContext) {
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                eventBusFactory.createEventBus().publish("news-feed", "some-news" + i);
            }
        }).start();
        super.response(routingContext.response(), null);
    }

    private void send(RoutingContext routingContext) {
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                eventBusFactory.createEventBus().send("news-feed", "some-news" + i, fn -> {
                    if (fn.succeeded()) {
                        System.err.println("reply\t" + fn.result().body());
                    }
                });
            }
        }).start();
        super.response(routingContext.response(), null);
    }

    private void consumer() {
        eventBusFactory.createEventBus().consumer("news-feed", consumer -> {
            System.err.println("consumer\t" + consumer.body().toString());
            consumer.reply("consumer over");
        });
    }
}
