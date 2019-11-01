package com.luke.milo.springcloud.configuration;

import com.luke.milo.springcloud.hystrix.MiloHystrixStrategy;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Descrtption MiloFeignConfiguration
 * @Author luke
 * @Date 2019/9/26
 **/
@Slf4j
@Configuration
public class MiloFeignConfiguration{

    /**
     * feign远程调用拦截器
     * @return
     */
    @Bean
    @Qualifier("miloFeignTransactionContextInterceptor")
    public MiloFeignTransactionContextInterceptor miloFeignTransactionContextInterceptor(){
        return new MiloFeignTransactionContextInterceptor(log);
    }

    /**
     * 使用者开启的hystrix熔断功能后，
     * feign的调用使用 hystrix-cloud线程池执行远程调用，
     * 所以需要进行事务上下文的跨线程传递
     * @return
     */
    @Bean
    @ConditionalOnProperty(name = "feign.hystrix.enabled")
    public HystrixConcurrencyStrategy hystrixConcurrencyStrategy() {
        return new MiloHystrixStrategy();
    }

}
