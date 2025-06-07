package cz.dan.fetcher.domain.football.request.player.outbox.service;

import cz.dan.fetcher.domain.football.request.player.outbox.entity.FootballPlayerRequestOutbox;
import cz.dan.fetcher.domain.football.request.player.outbox.entity.FootballPlayerRequestOutboxRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FootballPlayerRequestOutboxService {

    private final FootballPlayerRequestOutboxRepository repository;

    public void save(FootballPlayerRequestOutbox footballPlayer) {
        repository.save(footballPlayer);
    }

}
