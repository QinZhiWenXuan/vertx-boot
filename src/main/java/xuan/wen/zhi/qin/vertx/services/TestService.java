package xuan.wen.zhi.qin.vertx.services;

import io.vertx.core.MultiMap;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TestService {
    private static final Logger logger = LoggerFactory.getLogger(TestService.class);

    public void get(RoutingContext routingContext) {
        MultiMap params = routingContext.request().params();
        logger.info("params : \t {}", params.toString());
        routingContext.response().end(params.toString());
    }
}
