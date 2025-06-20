package cz.dan.fetcher.domain.football.request.team.outbox.job;

import cz.dan.fetcher.domain.football.request.team.entity.FootballTeamRequestOutbox;
import cz.dan.fetcher.domain.football.request.team.inbox.entity.FootballTeamRequest;
import cz.dan.fetcher.domain.inbox.service.request.InboxRequestService;
import cz.dan.fetcher.domain.outbox.fetcher.Fetcher;
import cz.dan.fetcher.domain.outbox.job.request.RequestJob;
import cz.dan.fetcher.domain.outbox.job.request.RequestJobProcessor;
import cz.dan.fetcher.domain.outbox.service.request.OutboxRequestService;
import cz.dan.fetcher.domain.team.entity.Team;
import cz.dan.fetcher.domain.team.service.TeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Slf4j
public class FootballTeamJob extends RequestJob<FootballTeamRequestOutbox, FootballTeamRequest> {

    private final TeamService teamService;

    public FootballTeamJob(Set<Fetcher<FootballTeamRequestOutbox, FootballTeamRequest>> fetchers,
                           InboxRequestService<FootballTeamRequest> inboxRequestService,
                           OutboxRequestService<FootballTeamRequestOutbox> outboxRequestService,
                           FootballTeamRequestJobProperties properties,
                           RequestJobProcessor requestJobProcessor,
                           TeamService teamService) {
        super(fetchers, inboxRequestService, outboxRequestService, properties, requestJobProcessor);
        this.teamService = teamService;
    }

    @Override
    protected Long saveInternally(FootballTeamRequest request) {
        return teamService.save(Team.builder().sourceId(request.getId()).source(request.getSource()).build());
    }

}
