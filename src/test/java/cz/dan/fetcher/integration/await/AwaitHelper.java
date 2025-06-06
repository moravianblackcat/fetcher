package cz.dan.fetcher.integration.await;

import org.awaitility.core.ThrowingRunnable;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

public final class AwaitHelper {

    private AwaitHelper() {
        throw new UnsupportedOperationException();
    }

    public static void assertRows(int timeoutInSeconds, Supplier<List<Map<String, Object>>> supplierOfActualRows,
                                  List<Map<String, Object>> expectedRows) {
        assertInSeconds(timeoutInSeconds, () -> {
            List<Map<String, Object>> actualRows = supplierOfActualRows.get();
            assertThat(actualRows).isNotNull().containsAll(expectedRows);
        });
    }

    public static <T> void assertValue(int timeoutInSeconds, Supplier<T> supplierOfActualValue, T expectedValue) {
        assertInSeconds(timeoutInSeconds, () -> {
            T actualValue = supplierOfActualValue.get();
            assertThat(actualValue).isNotNull().isEqualTo(expectedValue);
        });
    }

    public static void assertValueContains(int timeoutInSeconds, Supplier<String> supplierOfActualValue,
                                               String expectedValue) {
        assertInSeconds(timeoutInSeconds, () -> {
            String actualValue = supplierOfActualValue.get();
            assertThat(actualValue).isNotNull().contains(expectedValue);
        });
    }

    private static void assertInSeconds(int timeoutSeconds, ThrowingRunnable throwingRunnable) {
        await().atMost(timeoutSeconds, SECONDS).untilAsserted(throwingRunnable);
    }

}
