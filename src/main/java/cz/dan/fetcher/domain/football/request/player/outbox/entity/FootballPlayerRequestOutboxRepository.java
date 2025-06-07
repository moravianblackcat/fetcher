package cz.dan.fetcher.domain.football.request.player.outbox.entity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FootballPlayerRequestOutboxRepository extends CrudRepository<FootballPlayerRequestOutbox, Long> {
}
