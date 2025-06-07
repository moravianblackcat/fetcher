package cz.dan.fetcher.domain.outbox.fetcher;

import cz.dan.fetcher.domain.inbox.entity.request.Source;
import cz.dan.fetcher.domain.outbox.entity.request.Outbox;

public interface Fetcher<E extends Outbox> {

    boolean supports(Source source);

    E get(long id) throws Exception;

}
