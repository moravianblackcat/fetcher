package cz.dan.fetcher.domain.football.request.player.outbox.service;

import cz.dan.fetcher.domain.football.request.player.outbox.entity.FootballPlayerRequestOutbox;
import cz.dan.fetcher.domain.football.request.player.outbox.entity.FootballPlayerRequestOutboxRepository;
import cz.dan.fetcher.domain.outbox.service.request.OutboxRequestService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
public class FootballPlayerRequestOutboxService extends OutboxRequestService<FootballPlayerRequestOutbox> {

    public FootballPlayerRequestOutboxService(FootballPlayerRequestOutboxRepository repository) {
        super(repository);
    }

}
