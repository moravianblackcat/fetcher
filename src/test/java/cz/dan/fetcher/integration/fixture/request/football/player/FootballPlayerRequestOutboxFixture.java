package cz.dan.fetcher.integration.fixture.request.football.player;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FootballPlayerRequestOutboxFixture {

    private final JdbcTemplate jdbcTemplate;

    public void saveFootballPlayerRequestOutboxes(List<Map<String, Object>> outboxes) {
        String statement = "INSERT INTO football_player_request_outbox VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        jdbcTemplate.batchUpdate(statement, new FootballPlayerOutboxBatchPreparedStatementSetter(outboxes));
    }

    private record FootballPlayerOutboxBatchPreparedStatementSetter(List<Map<String, Object>> outboxes)
            implements BatchPreparedStatementSetter {

        @Override
        public void setValues(PreparedStatement ps, int i) throws SQLException {
            Map<String, Object> player = outboxes.get(i);
            ps.setLong(1, (Long) player.get("id"));
            ps.setLong(2, (Long) player.get("sourceId"));
            ps.setString(3, (String) player.get("nationality"));
            ps.setString(4, (String) player.get("position"));
            ps.setString(5, (String) player.get("firstName"));
            ps.setString(6, (String) player.get("lastName"));
            ps.setString(7, (String) player.get("name"));
            ps.setString(8, (String) player.get("displayName"));
            ps.setDate(9, Date.valueOf((LocalDate) player.get("dateOfBirth")));
            ps.setString(10, (String) player.get("source"));
        }

        @Override
        public int getBatchSize() {
            return outboxes.size();
        }
    }

}
