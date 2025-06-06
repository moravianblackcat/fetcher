package cz.dan.fetcher.outbox.domain.job;

public interface RequestJobProperties {

    int getChunk();

    int getMaxRetries();

}
