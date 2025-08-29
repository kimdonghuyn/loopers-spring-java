package com.loopers.infrastructure.payment;

import feign.Request;
import org.springframework.context.annotation.Bean;

public class PgFeignClientConfig {
    @Bean
    public Request.Options feignOption() {
        return new Request.Options(1000, 3000);
    }

    @Bean
    public feign.Retryer retryer() {
        return feign.Retryer.NEVER_RETRY;
    }

}
