package cz.dan.fetcher.domain.football.request.team.entity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FootballTeamRequestOutboxRepository extends CrudRepository<FootballTeamRequestOutbox, Long> {
}
