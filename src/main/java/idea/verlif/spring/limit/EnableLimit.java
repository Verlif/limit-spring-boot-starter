package idea.verlif.spring.limit;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Verlif
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Configuration
@Documented
@Import({LimitConfig.class, LimitAspect.class})
public @interface EnableLimit {
}
