package cz.dan.fetcher.domain.football.request.team.outbox.service;

import cz.dan.fetcher.domain.football.request.team.entity.FootballTeamRequestOutbox;
import cz.dan.fetcher.domain.football.request.team.entity.FootballTeamRequestOutboxRepository;
import cz.dan.fetcher.domain.outbox.service.request.OutboxRequestService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
public class FootballTeamRequestOutboxService extends OutboxRequestService<FootballTeamRequestOutbox> {

    protected FootballTeamRequestOutboxService(FootballTeamRequestOutboxRepository repository) {
        super(repository);
    }

}
