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
public class FootballPlayerRequestOutboxServiceImpl implements FootballPlayerRequestOutboxService {

    private final FootballPlayerRequestOutboxRepository repository;

    @Override
    public void delete(long id) {
        repository.deleteById(id);
    }

    @Override
    public Iterable<FootballPlayerRequestOutbox> getAll() {
        return repository.findAll();
    }

    @Override
    public void save(FootballPlayerRequestOutbox outbox) {
        repository.save(outbox);
    }

}
