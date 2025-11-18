package takehomechallenge.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    private static final Logger log = LoggerFactory.getLogger(AsyncConfig.class);

    @Bean(name = "notificationTaskExecutor")
    @Override
    public Executor getAsyncExecutor(){
        log.info("Configuring async task executor for notifications");

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // Core pool size: min of threds always active
        executor.setCorePoolSize(5);

        executor.setMaxPoolSize(10);

        executor.setThreadNamePrefix("notification-async-");

        executor.setWaitForTasksToCompleteOnShutdown(true);

        executor.setAwaitTerminationSeconds(30);

        executor.initialize();

        log.info("✅ Async task executor configured successfully");
        log.info("   Core pool size: {}", executor.getCorePoolSize());
        log.info("   Max pool size: {}", executor.getMaxPoolSize());
        log.info("   Queue capacity: {}", executor.getQueueCapacity());

        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, params) -> {
            log.error("❌ Uncaught exception in async method: {}", method.getName());
            log.error("❌ Exception: {}", throwable.getMessage(), throwable);
            log.error("❌ Parameters: {}", params);
        };
    }
}
