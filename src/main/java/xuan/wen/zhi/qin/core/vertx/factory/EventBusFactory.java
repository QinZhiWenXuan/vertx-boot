package xuan.wen.zhi.qin.core.vertx.factory;

import io.vertx.core.eventbus.EventBus;
import org.springframework.stereotype.Component;

@Component
public class EventBusFactory extends VertxFactory {
    public EventBus createEventBus() {
        return super.vertx.eventBus() ;
    }
}
