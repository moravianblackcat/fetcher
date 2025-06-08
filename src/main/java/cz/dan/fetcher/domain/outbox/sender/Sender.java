package cz.dan.fetcher.domain.outbox.sender;

import cz.dan.fetcher.domain.outbox.entity.request.Outbox;

public abstract class Sender<O extends Outbox> {

    public abstract void sendOutbox(O outbox);

}
