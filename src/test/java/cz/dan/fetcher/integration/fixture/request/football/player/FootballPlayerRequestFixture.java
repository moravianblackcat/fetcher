package cz.dan.fetcher.integration.fixture.request.football.player;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class FootballPlayerRequestFixture {

    private final JdbcTemplate jdbcTemplate;

    public void saveFootballPlayerRequest(long playerId, String source, String state) {
        String sql = "INSERT INTO football_player_request (id, source, state, created_at) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, playerId, source, state, Instant.now());
    }

}
