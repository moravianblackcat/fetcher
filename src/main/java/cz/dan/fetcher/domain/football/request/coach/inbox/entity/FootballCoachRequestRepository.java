package cz.dan.fetcher.domain.football.request.coach.inbox.entity;

import cz.dan.fetcher.domain.football.request.entity.RequestRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FootballCoachRequestRepository extends RequestRepository<FootballCoachRequest> {

    @Query(value = "SELECT * FROM football_coach_request WHERE state = 'SCHEDULED' OR state = 'RETRY'"
            + " ORDER BY created_at LIMIT ?",
            nativeQuery = true)
    List<FootballCoachRequest> findOldestForProcessing(int limit);

}
