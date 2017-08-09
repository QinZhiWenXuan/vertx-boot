package xuan.wen.zhi.qin.core.vertx.factory;

import io.vertx.core.Vertx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VertxFactory {
    @Autowired
    protected Vertx vertx;
}
