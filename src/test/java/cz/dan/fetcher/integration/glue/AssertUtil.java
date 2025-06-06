package cz.dan.fetcher.integration.glue;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@RequiredArgsConstructor
@Component
public class AssertUtil {

    private final JdbcTemplate jdbcTemplate;

    public Supplier<String> getActualState(String tableName, Long requestId, String source) {
        return () -> jdbcTemplate.queryForObject(
                "SELECT state FROM " + tableName + " WHERE id = ? AND source = ?",
                String.class,
                requestId, source);
    }

}
