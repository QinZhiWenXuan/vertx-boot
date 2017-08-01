package xuan.wen.zhi.qin.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import xuan.wen.zhi.qin.vertx.handler.FailureHandler;

@Component
public class VertxServer extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(VertxServer.class);
    @Value(value = "${server.port}")
    private Integer port;

    @Autowired
    private Router router;
    @Autowired
    private HttpServer httpServer;

    @Autowired
    private FailureHandler failureHandler;

    @Override
    public void start() throws Exception {
        super.start();
        Router router = this.router;
        this.httpFailureHandler();
        this.routeStatic();
        this.vertx.createHttpServer().requestHandler(router::accept).listen(this.port);
    }

    private void httpFailureHandler() {
        this.router.route().failureHandler(this.failureHandler::httpFailureHandler);
        return;
    }

    private void routeStatic() {
        this.router.route().handler(StaticHandler.create());
    }

}
