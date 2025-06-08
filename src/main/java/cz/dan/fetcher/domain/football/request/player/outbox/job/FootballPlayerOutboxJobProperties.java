package cz.dan.fetcher.domain.football.request.player.outbox.job;

import cz.dan.fetcher.domain.job.JobProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("cz.dan.fetcher.football.player.outbox.job")
@Data
public class FootballPlayerOutboxJobProperties implements JobProperties {

    String jobIdentifier;

}
