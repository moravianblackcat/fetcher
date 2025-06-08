package cz.dan.fetcher.infra.fetcher.api.sportmonks.config;

import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SportmonksFeignConfig {

    private final SportmonksApiConfigurationProperties apiConfigurationProperties;

    @Bean
    public RequestInterceptor sportmonksRequestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Authorization", apiConfigurationProperties.getToken());
        };
    }
}
