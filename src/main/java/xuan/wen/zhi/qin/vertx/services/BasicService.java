package xuan.wen.zhi.qin.vertx.services;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public abstract class BasicService {
    private static final Logger logger = LoggerFactory.getLogger(BasicService.class);
    @Autowired
    protected Router router;

    protected void response(HttpServerResponse response, String json) {
        logger.debug("response \t", json);
        response.putHeader("Content-Type", "application/json").end(Buffer.buffer(json));
        return;
    }

    @PostConstruct
    protected void init() {
        this.deploy();
    }

    protected abstract void deploy();
}
