package xuan.wen.zhi.qin.vertx.aop;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xuan.wen.zhi.qin.core.vertx.jdbc.VertxDao;

import java.util.ArrayList;
import java.util.List;

@Component
@Aspect
public class DaoAop {
    private static final Logger logger = LoggerFactory.getLogger(DaoAop.class);
    @Autowired
    private VertxDao vertxDao;


    @Around(value = "execution(* xuan.wen.zhi.qin.vertx.dao.*.*(..))")
    public Object buildConnection(ProceedingJoinPoint joinPoint) throws Throwable {
        List<Object> results = new ArrayList<>(1);
        logger.info("增强处理开始");
        Object[] args = joinPoint.getArgs();
        Handler handler = null;
        if (null != args && args.length > 0) {
            HttpServerResponse response = null;
            final List<Integer> index = new ArrayList<>(1);
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                if (arg == null) {
                    index.add(i);
                    continue;
                }
                if (arg instanceof HttpServerResponse) {
                    response = (HttpServerResponse) arg;
                    continue;
                }
            }
            if (index.size() > 0) {
                vertxDao.getConnection(response, fn -> {
                    try {
                        args[index.get(0)] = fn;
                        results.add(joinPoint.proceed(args));
                    } catch (Throwable e) {
                        logger.error("proceed error :", e);
                        throw new RuntimeException(e);
                    }
                });
            } else {
                results.add(joinPoint.proceed(args));
            }
            logger.info("增强处理结束");
        }
        return results.size() > 0 ? results.get(0) : null;
    }

}
