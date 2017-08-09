package xuan.wen.zhi.qin.core.vertx.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import xuan.wen.zhi.qin.core.vertx.handler.web.FailureHandler;

@Component
public class VertxServer extends AbstractVerticle {
    @Value(value = "${server.port}")
    private Integer port;

    @Autowired
    private Router router;

    @Autowired
    private FailureHandler failureHandler;
    /**
     * If your verticle does a simple, synchronous start-up then override this method and put your start-up
     * code in there.
     *
     * @throws Exception
     */
    @Override
    public void start() throws Exception {
        super.start();
        this.httpFailureHandler();
        this.routeStatic();
        this.vertx.createHttpServer().requestHandler(this.router::accept).listen(this.port);
    }
    private void httpFailureHandler() {
        this.router.route().failureHandler(this.failureHandler::httpFailureHandler);
    }
    private void routeStatic() {
        this.router.route().handler(StaticHandler.create());
    }
}
