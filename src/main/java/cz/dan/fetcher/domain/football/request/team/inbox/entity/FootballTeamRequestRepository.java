package cz.dan.fetcher.domain.football.request.team.inbox.entity;

import cz.dan.fetcher.domain.football.request.entity.RequestRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface FootballTeamRequestRepository extends RequestRepository<FootballTeamRequest> {

    @Query(value = "SELECT id FROM football_team_request WHERE id IN (?1) AND source = ?2",
            nativeQuery = true)
    Set<Long> findAlreadyExistingRequestIds(List<Long> ids, String source);

    @Query(value = "SELECT * FROM football_team_request WHERE state = 'SCHEDULED' OR state = 'RETRY'"
            + " ORDER BY created_at LIMIT ?",
            nativeQuery = true)
    List<FootballTeamRequest> findOldestForProcessing(int limit);

}
