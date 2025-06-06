package cz.dan.fetcher.football.request.player.outbox.domain.entity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FootballPlayerRequestOutboxRepository extends CrudRepository<FootballPlayerRequestOutbox, Long> {
}
