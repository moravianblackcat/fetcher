package cz.dan.fetcher.integration.wiremock;

import com.github.tomakehurst.wiremock.client.WireMock;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class WireMockConfiguration {

    @Bean(destroyMethod = "")
    public WireMock sportmonkRestApiWireMock(SportmonksRestApiConfigurationProperties properties) {
        return new WireMock(properties.getHost(), properties.getPort());
    }

    @ConfigurationProperties("sportmonks.rest.api")
    @Value
    public static class SportmonksRestApiConfigurationProperties {
        String host;

        int port;
    }
}
