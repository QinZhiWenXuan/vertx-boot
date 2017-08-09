package xuan.wen.zhi.qin.core.vertx.factory;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import org.springframework.stereotype.Component;

@Component
public class ExecuteBlockingFactory extends VertxFactory {

    public <T> void executeBlocking(Handler<Future<T>> blockingCodeHandler, Handler<AsyncResult<T>> resultHandler) {
        this.vertx.executeBlocking(blockingCodeHandler, resultHandler);
    }
}
