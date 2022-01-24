package idea.verlif.spring.limit;

import idea.verlif.spring.limit.anno.Limit;

import java.lang.reflect.Method;

/**
 * 无效通路处理器。<br/>
 * 处理无法通过的限制接口的返回值。
 *
 * @author Verlif
 */
public interface NotArrivedHandler {

    /**
     * 当被限制的接口配置了未添加的限制器时调用。
     *
     * @param cl 未添加的限制器类
     * @return 返回前端的数据
     */
    Object noSuchHandler(Class<? extends LimitHandler> cl);

    /**
     * 被限制器限制无法通行时触发。
     *
     * @param method 触发的方法
     * @param limit  限制注解
     * @return 返回前端的数据
     */
    Object notArrived(Method method, Limit limit);
}
