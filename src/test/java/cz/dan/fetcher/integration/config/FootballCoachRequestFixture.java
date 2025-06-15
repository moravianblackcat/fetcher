package cz.dan.fetcher.integration.config;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class FootballCoachRequestFixture {

    private final JdbcTemplate jdbcTemplate;

    public void saveFootballCoachRequest(long coachId, String source, String state) {
        String sql = "INSERT INTO football_coach_request (id, source, state, created_at) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, coachId, source, state, Instant.now());
    }

}
