package idea.verlif.spring.limit;

import idea.verlif.spring.limit.anno.Limit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/30 9:04
 */
@Aspect
@Component
@ConditionalOnProperty(prefix = "station.limit", value = "enable", matchIfMissing = true)
public class LimitAspect {

    private final HashMap<Class<? extends LimitHandler>, LimitHandler> handlerMap;

    @Autowired
    private NotArrivedHandler naHandler;

    public LimitAspect(@Autowired ApplicationContext context) {
        handlerMap = new HashMap<>();
        Map<String, LimitHandler> map = context.getBeansOfType(LimitHandler.class);
        for (LimitHandler handler : map.values()) {
            handlerMap.put(handler.getClass(), handler);
        }
    }

    @Around("@within(idea.verlif.spring.limit.anno.Limit) || @annotation(idea.verlif.spring.limit.anno.Limit)")
    public Object onLimit(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature sig = joinPoint.getSignature();
        Method method = ((MethodSignature) sig).getMethod();

        Limit limit = method.getAnnotation(Limit.class);
        if (limit == null) {
            limit = method.getDeclaringClass().getAnnotation(Limit.class);
        }
        LimitHandler handler = handlerMap.get(limit.handler());
        if (handler == null) {
            return naHandler.noSuchHandler(limit.handler());
        }
        // 生成限定Key
        String key = limit.key();
        if (key.length() == 0) {
            // 未指定Key则取方法名
            key = method.getName();
        }
        if (handler.arrived(key)) {
            return joinPoint.proceed();
        } else {
            return naHandler.notArrived(method, limit);
        }
    }
}
