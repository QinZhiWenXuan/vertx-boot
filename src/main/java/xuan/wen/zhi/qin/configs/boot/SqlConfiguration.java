package xuan.wen.zhi.qin.configs.boot;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import sun.misc.IOUtils;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class SqlConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(SqlConfiguration.class);
    @Value(value = "classpath:vertx/index.json")
    private Resource index;
    @Value(value = "classpath:vertx/sql/category.json")
    private Resource category;

    @Bean
    public JsonObject indexJson() {
        JsonObject jsonObject = null;
        try (InputStream inputStream = index.getInputStream()) {
            jsonObject = new JsonObject(Buffer.buffer(IOUtils.readFully(inputStream, -1, true)));
        } catch (IOException e) {
            e.printStackTrace();
            jsonObject = new JsonObject();
        }
        return jsonObject;
    }

    @Bean
    public JsonObject categorySql() {
        JsonObject jsonObject = null;
        try (InputStream inputStream = category.getInputStream()) {
            jsonObject = new JsonObject(Buffer.buffer(IOUtils.readFully(inputStream, -1, true)));
        } catch (IOException e) {
            logger.error("parse categorySql error : {}", e.getLocalizedMessage());
            jsonObject = new JsonObject();
        }
        return jsonObject;
    }
}
