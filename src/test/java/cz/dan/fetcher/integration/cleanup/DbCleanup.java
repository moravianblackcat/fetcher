package cz.dan.fetcher.integration.cleanup;

import io.cucumber.java.Before;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.jdbc.core.JdbcTemplate;

@RequiredArgsConstructor
public class DbCleanup {

    private final DbCleanupProperties dbCleanupProperties;

    private final JdbcTemplate jdbcTemplate;

    @Before
    public void cleanUp() {
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
        String[] tables = dbCleanupProperties.getTables().split(",");
        for (String table:tables) {
            jdbcTemplate.execute("TRUNCATE TABLE " + table);
        }
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
    }

    @ConfigurationProperties("db.cleanup")
    @Value
    public static class DbCleanupProperties {
        String tables;
    }

}
