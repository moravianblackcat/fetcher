package cz.dan.fetcher.outbox.domain.job;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import static jakarta.transaction.Transactional.TxType.REQUIRES_NEW;

/**
 * This class serves just as a workaround for {@link cz.dan.fetcher.outbox.domain.job.RequestJob#process},
 * which cannot be annotated with {@link jakarta.transaction.Transactional} directly.
 */
@Component
public class RequestJobProcessor {

    @Transactional(REQUIRES_NEW)
    public void process(Runnable runnable) {
        runnable.run();
    }

}
