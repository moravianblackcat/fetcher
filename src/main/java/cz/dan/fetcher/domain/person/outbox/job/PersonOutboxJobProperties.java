package cz.dan.fetcher.domain.person.outbox.job;

import cz.dan.fetcher.domain.job.JobProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("cz.dan.fetcher.person.outbox.job")
@Data
public class PersonOutboxJobProperties implements JobProperties {

    String jobIdentifier;

}
