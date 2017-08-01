package xuan.wen.zhi.qin.configs.vertx;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfiguration {
    @Autowired
    private Vertx vertx;

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
        JDBCClient jdbcClient = JDBCClient.createShared(this.vertx, config);
        return jdbcClient;
    }
}
