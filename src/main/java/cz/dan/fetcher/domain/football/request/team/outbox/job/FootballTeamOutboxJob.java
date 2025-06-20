package cz.dan.fetcher.domain.football.request.team.outbox.job;

import cz.dan.fetcher.domain.football.request.team.entity.FootballTeamRequestOutbox;
import cz.dan.fetcher.domain.outbox.job.request.RequestOutboxJob;
import cz.dan.fetcher.domain.outbox.sender.Sender;
import cz.dan.fetcher.domain.outbox.service.request.OutboxRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Slf4j
public class FootballTeamOutboxJob extends RequestOutboxJob<FootballTeamRequestOutbox> {

    public FootballTeamOutboxJob(FootballTeamOutboxJobProperties jobProperties,
                                OutboxRequestService<FootballTeamRequestOutbox> outboxRequestService,
                                Set<Sender<FootballTeamRequestOutbox>> senders) {
        super(jobProperties, outboxRequestService, senders);
    }

}
