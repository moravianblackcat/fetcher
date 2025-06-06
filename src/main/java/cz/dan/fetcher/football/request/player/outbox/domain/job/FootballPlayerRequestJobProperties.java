package cz.dan.fetcher.football.request.player.outbox.domain.job;


import cz.dan.fetcher.outbox.domain.job.RequestJobProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("cz.dan.fetcher.football.player.request.job")
@Data
public class FootballPlayerRequestJobProperties implements RequestJobProperties {
    int chunk;

    int maxRetries;
}
