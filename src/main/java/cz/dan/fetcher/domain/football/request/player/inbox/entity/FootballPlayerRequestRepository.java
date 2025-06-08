package cz.dan.fetcher.domain.football.request.player.inbox.entity;

import cz.dan.fetcher.domain.football.request.player.inbox.entity.FootballPlayerRequest.FootballPlayerRequestId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FootballPlayerRequestRepository extends CrudRepository<FootballPlayerRequest, FootballPlayerRequestId> {

    @Query(value = "SELECT * FROM football_player_request WHERE state = 'SCHEDULED' OR state = 'RETRY'"
            + " ORDER BY created_at LIMIT ?",
            nativeQuery = true)
    List<FootballPlayerRequest> findOldestScheduled(int limit);

}
