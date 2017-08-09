package xuan.wen.zhi.qin.core.vertx.handler.web;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FailureHandler {
    private static final Logger logger = LoggerFactory.getLogger(FailureHandler.class);
    @Autowired
    private ResponseHandler responseHandler;

    public void httpFailureHandler(RoutingContext failureHandler) {
        int statusCode = failureHandler.statusCode() ;
        if (statusCode != 200) {
            logger.error("httpFailureHandler : {}", statusCode);
            failureHandler.response().setStatusCode(failureHandler.statusCode()).end();
            return;
        }
        Throwable failure = failureHandler.failure();
        if (null != failure) {
            JsonObject error = new JsonObject().put("code", 500).put("message", "Internal Server Error : " + failure.getLocalizedMessage());
            logger.error("http error :", failure);
            responseHandler.response(failureHandler.response(), error.toString());
            return;
        }
    }
}
