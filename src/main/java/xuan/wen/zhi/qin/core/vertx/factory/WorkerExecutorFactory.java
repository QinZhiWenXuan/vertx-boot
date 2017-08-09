package xuan.wen.zhi.qin.core.vertx.factory;

import io.vertx.core.WorkerExecutor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class WorkerExecutorFactory extends VertxFactory {
    public WorkerExecutor createWorkerExecutor(String name, int poolSize, long maxExecuteTime) {
        return super.vertx.createSharedWorkerExecutor(name, poolSize, maxExecuteTime);
    }

    public void close(WorkerExecutor workerExecutor) {
        Optional.of(workerExecutor).ifPresent(consumer -> {
            consumer.close();
        });
    }
}
