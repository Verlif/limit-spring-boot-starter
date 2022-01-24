package idea.verlif.spring.limit.impl;

import idea.verlif.spring.limit.LimitHandler;
import idea.verlif.spring.limit.NotArrivedHandler;
import idea.verlif.spring.limit.anno.Limit;

import java.lang.reflect.Method;

/**
 * @author Verlif
 */
public class DefaultNotArrivedHandler implements NotArrivedHandler {

    @Override
    public Object noSuchHandler(Class<? extends LimitHandler> cl) {
        return "No such LimitHandler - " + cl.getName();
    }

    @Override
    public Object notArrived(Method method, Limit limit) {
        return "limited!";
    }
}
