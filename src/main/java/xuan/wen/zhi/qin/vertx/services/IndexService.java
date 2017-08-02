package xuan.wen.zhi.qin.vertx.services;

import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class IndexService {
    private static final Logger logger = LoggerFactory.getLogger(IndexService.class);
    @Autowired
    private Router router;
    @Autowired
    private JsonObject indexJson ;
    @PostConstruct
    public void deploy() {
        logger.info("IndexService deploy 。。。。。。");
        logger.info("router \t {}", router.toString());
        this.router.get("/vertx/index").handler(this::get);
    }

    private void get(RoutingContext routingContext) {
        System.err.println("indexJson\t" + indexJson.toString());
        MultiMap params = routingContext.request().params();
        logger.info("params : \t {}", params.toString());
        routingContext.response().end(params.toString());
    }

}
