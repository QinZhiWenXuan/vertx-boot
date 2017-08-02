package xuan.wen.zhi.qin.vertx.handler;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FailureHandler {
    private static final Logger logger = LoggerFactory.getLogger(FailureHandler.class);

    public void httpFailureHandler(RoutingContext failureHandler) {
        Throwable failure = failureHandler.failure();
        if (null != failure) {
            JsonObject error = new JsonObject().put("code", 500).put("message", "Internal Server Error : " + failure.getLocalizedMessage());
            logger.error("http error :", failure);
            failureHandler.response().putHeader("Content-Type", "application/json").end(error.toBuffer());
            return;
        } else {
            return;
        }


    }
}
