package cz.dan.fetcher.domain.football.request.player.inbox.entity;

import cz.dan.fetcher.domain.football.request.entity.RequestRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface FootballPlayerRequestRepository extends RequestRepository<FootballPlayerRequest> {

    @Query(value = "SELECT id FROM football_player_request WHERE id IN (?1) AND source = ?2",
            nativeQuery = true)
    Set<Long> findAlreadyExistingRequestIds(List<Long> ids, String source);

    @Query(value = "SELECT * FROM football_player_request WHERE state = 'SCHEDULED' OR state = 'RETRY'"
            + " ORDER BY created_at LIMIT ?",
            nativeQuery = true)
    List<FootballPlayerRequest> findOldestForProcessing(int limit);

}
