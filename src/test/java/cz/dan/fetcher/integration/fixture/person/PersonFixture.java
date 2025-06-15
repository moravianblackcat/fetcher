package cz.dan.fetcher.integration.fixture.person;

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
public class PersonFixture {

    private final JdbcTemplate jdbcTemplate;

    public void savePersonOutboxes(List<Map<String, Object>> outboxes) {
        String statement = "INSERT INTO person_outbox VALUES (?, ?, ?, ?, ?, ?, ?);";

        jdbcTemplate.batchUpdate(statement, new PersonOutboxBatchPreparedStatementSetter(outboxes));
    }

    private record PersonOutboxBatchPreparedStatementSetter(List<Map<String, Object>> outboxes)
            implements BatchPreparedStatementSetter {

        @Override
        public void setValues(PreparedStatement ps, int i) throws SQLException {
            Map<String, Object> person = outboxes.get(i);
            ps.setLong(1, (Long) person.get("id"));
            ps.setString(2, (String) person.get("nationality"));
            ps.setString(3, (String) person.get("firstName"));
            ps.setString(4, (String) person.get("lastName"));
            ps.setString(5, (String) person.get("name"));
            ps.setString(6, (String) person.get("displayName"));
            ps.setDate(7, Date.valueOf((LocalDate) person.get("dateOfBirth")));
        }

        @Override
        public int getBatchSize() {
            return outboxes.size();
        }
    }

}
