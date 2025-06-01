package cz.dan.fetcher.integration.fixture.request.football.player;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FootballPlayerRequestFixture {

    private final JdbcTemplate jdbcTemplate;

    public void saveFootballPlayerRequest(long playerId, String source, String state) {
        String sql = "INSERT INTO football_player_request (player_id, source, state) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, playerId, source, state);
    }

}
