package cz.dan.fetcher.domain.football.request.team.outbox.job;

import cz.dan.fetcher.domain.outbox.job.request.RequestJobProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("cz.dan.fetcher.football.team.request.job")
@Data
public class FootballTeamRequestJobProperties implements RequestJobProperties {
    int chunk;

    String jobIdentifier;

    int maxRetries;

}
