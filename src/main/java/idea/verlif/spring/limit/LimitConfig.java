package idea.verlif.spring.limit;

import idea.verlif.spring.limit.impl.DefaultNotArrivedHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Verlif
 */
@Configuration
public class LimitConfig {

    @Bean
    @ConditionalOnMissingBean(NotArrivedHandler.class)
    public NotArrivedHandler notArrivedHandler() {
        return new DefaultNotArrivedHandler();
    }
}
