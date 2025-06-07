package cz.dan.fetcher.domain.football.request.player.outbox.job;


import cz.dan.fetcher.domain.outbox.job.request.RequestJobProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("cz.dan.fetcher.football.player.request.job")
@Data
public class FootballPlayerRequestJobProperties implements RequestJobProperties {
    int chunk;

    int maxRetries;
}
