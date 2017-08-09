package xuan.wen.zhi.qin.core.vertx.web;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xuan.wen.zhi.qin.core.vertx.handler.web.ResponseHandler;

import javax.annotation.PostConstruct;

@Component
public abstract class VertxController {
    private static final Logger logger = LoggerFactory.getLogger(VertxController.class);
    @Autowired
    protected Router router;
    @Autowired
    protected ResponseHandler responseHandler;

    protected void response(HttpServerResponse response, String json) {
        this.responseHandler.response(response, json);
    }

    @PostConstruct
    protected void register() {
        this.deploy();
    }

    protected abstract void deploy();
}
