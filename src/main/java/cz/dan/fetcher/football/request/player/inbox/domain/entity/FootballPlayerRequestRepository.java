package cz.dan.fetcher.football.request.player.inbox.domain.entity;

import cz.dan.fetcher.football.request.player.inbox.domain.entity.FootballPlayerRequest.FootballPlayerRequestId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FootballPlayerRequestRepository extends CrudRepository<FootballPlayerRequest, FootballPlayerRequestId> {
}
