package xuan.wen.zhi.qin.core.vertx.factory;

import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import xuan.wen.zhi.qin.core.vertx.server.VertxServer;

public class VertxInitFactory implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(VertxInitFactory.class);
    @Autowired
    private VertxServer vertxServer;

    /**
     * Invoked by a BeanFactory after it has set all bean properties supplied
     * (and satisfied BeanFactoryAware and ApplicationContextAware).
     * <p>This method allows the bean instance to perform initialization only
     * possible when all bean properties have been set and to throw an
     * exception in the event of misconfiguration.
     *
     * @throws Exception in the event of misconfiguration (such
     *                   as failure to set an essential property) or if initialization fails.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("init factory ... ");
        Vertx.vertx().deployVerticle(this.vertxServer, handle -> {
            if (handle.succeeded()) {
                logger.info("deployVerticle succeefully");
            } else {
                logger.error("deployVerticle fail : {}", handle.cause().getLocalizedMessage());
            }
        });
    }
}
