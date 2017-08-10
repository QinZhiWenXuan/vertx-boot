package xuan.wen.zhi.qin.core.vertx.handler.web;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class ResponseHandler {
    private static final Logger logger = LoggerFactory.getLogger(ResponseHandler.class);
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";

    public void errorHandler(HttpServerResponse response, Throwable cause) {
        if (!response.ended()) {
            logger.error("error : ", cause);
            JsonObject error = new JsonObject().put("code", 500).put("message", cause.getLocalizedMessage());
            this.response(response, error.toString());
        } else {
            return;
        }
    }

    public void response(HttpServerResponse response, String json) {
        logger.info("response \t{}", json);
        if (!response.ended()) {
            response.putHeader(CONTENT_TYPE, APPLICATION_JSON);
            if (StringUtils.hasLength(json)) {
                response.end(Buffer.buffer(json));
            } else {
                response.end();
            }
        }
        return;
    }
}
