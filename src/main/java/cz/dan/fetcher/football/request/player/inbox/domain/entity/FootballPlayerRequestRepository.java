package cz.dan.fetcher.football.request.player.inbox.domain.entity;

import cz.dan.fetcher.football.request.player.inbox.domain.entity.FootballPlayerRequest.FootballPlayerRequestId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FootballPlayerRequestRepository extends CrudRepository<FootballPlayerRequest, FootballPlayerRequestId> {

    @Query(value = "SELECT * FROM football_player_request WHERE state = 'SCHEDULED' ORDER BY created_at LIMIT ?",
            nativeQuery = true)
    List<FootballPlayerRequest> findOldestScheduled(int limit);

}
