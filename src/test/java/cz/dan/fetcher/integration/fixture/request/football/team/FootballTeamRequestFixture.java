package cz.dan.fetcher.integration.fixture.request.football.team;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class FootballTeamRequestFixture {

    private final JdbcTemplate jdbcTemplate;

    public void saveFootballTeamRequest(long teamId, String source, String state) {
        String sql = "INSERT INTO football_team_request (id, source, state, created_at) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, teamId, source, state, Instant.now());
    }
    
}
