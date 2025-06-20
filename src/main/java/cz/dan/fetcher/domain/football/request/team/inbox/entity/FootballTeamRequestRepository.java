package cz.dan.fetcher.domain.football.request.team.inbox.entity;

import cz.dan.fetcher.domain.football.request.entity.RequestRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FootballTeamRequestRepository extends RequestRepository<FootballTeamRequest> {

    @Query(value = "SELECT * FROM football_team_request WHERE state = 'SCHEDULED' OR state = 'RETRY'"
            + " ORDER BY created_at LIMIT ?",
            nativeQuery = true)
    List<FootballTeamRequest> findOldestForProcessing(int limit);

}
