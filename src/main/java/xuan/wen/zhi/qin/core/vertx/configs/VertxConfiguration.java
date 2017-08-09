package xuan.wen.zhi.qin.core.vertx.configs;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xuan.wen.zhi.qin.core.vertx.factory.VertxInitFactory;

import javax.sql.DataSource;

@Configuration
public class VertxConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(VertxConfiguration.class);

    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;
    @Value("${spring.datasource.max_pool_size}")
    private Integer maxPoolSize;
    @Value("${spring.datasource.initial_pool_size}")
    private Integer initialPoolSize;
    @Value("${spring.datasource.min_pool_size}")
    private Integer minPoolSize;
    @Value("${spring.datasource.max_statements}")
    private Integer maxStatements;
    @Value("${spring.datasource.max_statements_per_connection}")
    private Integer maxStatementsPerConnection;
    @Value("${spring.datasource.max_idle_time}")
    private Integer maxIdleTime;

    @Bean
    public Vertx vertx() {
        Vertx vertx = Vertx.vertx();
        logger.info("vertx\t{}", vertx.toString());
        return vertx;
    }

    @Bean
    public Router router() {
        Vertx vertx = this.vertx();
        Router router = Router.router(vertx);
        router.route().handler(CookieHandler.create());
        router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));
        return router;
    }

    @Bean(value = "jdbcClient")
    public JDBCClient jdbcClient() {
        JsonObject config = new JsonObject();
        config.put("url", url)
                .put("user", username)
                .put("password", password)
                .put("driver_class", driverClassName)
                .put("max_pool_size", maxPoolSize)
                .put("initial_pool_size", initialPoolSize)
                .put("min_pool_size", minPoolSize)
                .put("max_statements", maxStatements)
                .put("max_statements_per_connection", maxStatementsPerConnection)
                .put("max_idle_time", maxIdleTime);
        JDBCClient jdbcClient = JDBCClient.createShared(this.vertx(), config);
        return jdbcClient;
    }

    @Bean(value = "dataSourceClient")
    public JDBCClient dataSourceClient(DataSource dataSource) {
        return JDBCClient.create(this.vertx(), dataSource);
    }

    @Bean
    public VertxInitFactory vertxInitFactory() {
        return new VertxInitFactory();
    }
}
