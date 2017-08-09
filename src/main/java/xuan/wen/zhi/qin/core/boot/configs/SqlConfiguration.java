package xuan.wen.zhi.qin.core.boot.configs;

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
    @Value(value = "classpath:vertx/sql/category.json")
    private Resource category;
    @Value(value = "classpath:vertx/sql/role.json")
    private Resource role;

    @Bean
    public JsonObject categorySql() {
        return this.parseSQL(category);
    }

    @Bean
    public JsonObject roleSql() {
        return this.parseSQL(role);
    }

    private JsonObject parseSQL(Resource sql) {
        JsonObject jsonObject = null;
        try (InputStream inputStream = sql.getInputStream()) {
            jsonObject = new JsonObject(Buffer.buffer(IOUtils.readFully(inputStream, -1, true)));
        } catch (IOException e) {
            logger.error("parse sql error : {}", e.getLocalizedMessage());
            jsonObject = new JsonObject();
        }
        return jsonObject;
    }
}
