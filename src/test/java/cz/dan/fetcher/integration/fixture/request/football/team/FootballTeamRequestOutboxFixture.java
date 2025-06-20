package cz.dan.fetcher.integration.fixture.request.football.team;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FootballTeamRequestOutboxFixture {

    private final JdbcTemplate jdbcTemplate;

    public void saveFootballTeamRequestOutboxes(List<Map<String, Object>> outboxes) {
        String statement = "INSERT INTO football_team_request_outbox VALUES (?, ?, ?, ?, ?, ?);";

        jdbcTemplate.batchUpdate(statement, new FootballTeamOutboxBatchPreparedStatementSetter(outboxes));
    }

    private record FootballTeamOutboxBatchPreparedStatementSetter(List<Map<String, Object>> outboxes)
            implements BatchPreparedStatementSetter {

        @Override
        public void setValues(PreparedStatement ps, int i) throws SQLException {
            Map<String, Object> player = outboxes.get(i);
            ps.setLong(1, (Long) player.get("id"));
            ps.setString(2, (String) player.get("name"));
            ps.setLong(3, (Long) player.get("founded"));
            ps.setString(4, (String) player.get("country"));
            ps.setString(5, (String) player.get("stadium"));
            ps.setString(6, (String) player.get("city"));
        }

        @Override
        public int getBatchSize() {
            return outboxes.size();
        }

    }

}
