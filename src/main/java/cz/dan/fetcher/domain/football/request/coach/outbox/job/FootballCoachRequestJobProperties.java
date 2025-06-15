package cz.dan.fetcher.domain.football.request.coach.outbox.job;

import cz.dan.fetcher.domain.outbox.job.request.RequestJobProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("cz.dan.fetcher.football.coach.request.job")
@Data
public class FootballCoachRequestJobProperties implements RequestJobProperties {
    int chunk;

    String jobIdentifier;

    int maxRetries;
}
