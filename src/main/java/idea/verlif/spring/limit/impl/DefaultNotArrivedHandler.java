package idea.verlif.spring.limit.impl;

import idea.verlif.spring.limit.LimitHandler;
import idea.verlif.spring.limit.NotArrivedHandler;
import idea.verlif.spring.limit.anno.Limit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;

/**
 * @author Verlif
 */
public class DefaultNotArrivedHandler implements NotArrivedHandler {

    private static final Logger LOGGER = LogManager.getLogger(NotArrivedHandler.class);

    @Override
    public Object noSuchHandler(Class<? extends LimitHandler> cl) {
        LOGGER.warn("No such LimitHandler - " + cl.getName());
        return null;
    }

    @Override
    public Object notArrived(Method method, Limit limit) {
        LOGGER.warn("limited!");
        return null;
    }
}
