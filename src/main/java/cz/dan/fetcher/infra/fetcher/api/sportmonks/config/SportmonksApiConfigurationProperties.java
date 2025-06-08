package cz.dan.fetcher.infra.fetcher.api.sportmonks.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("sportmonks.api")
@Data
public class SportmonksApiConfigurationProperties {

    String token;

}
