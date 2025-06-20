package cz.dan.fetcher.domain.football.request.team.outbox.job;

import cz.dan.fetcher.domain.job.JobProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("cz.dan.fetcher.football.team.outbox.job")
@Data
public class FootballTeamOutboxJobProperties implements JobProperties {

    String jobIdentifier;

}
