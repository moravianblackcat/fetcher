package cz.dan.fetcher.domain.outbox.job.request;

public interface RequestJobProperties {

    int getChunk();

    int getMaxRetries();

}
