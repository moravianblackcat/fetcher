package cz.dan.fetcher.domain.outbox.fetcher;

import cz.dan.fetcher.domain.inbox.entity.request.Request;
import cz.dan.fetcher.domain.outbox.entity.request.Outbox;
import cz.dan.fetcher.domain.source.Source;

public interface Fetcher<E extends Outbox, R extends Request> {

    boolean supports(Source source);

    E get(long id) throws Exception;

}
