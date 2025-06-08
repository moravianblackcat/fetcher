package cz.dan.fetcher.domain.outbox.job.request;

import cz.dan.fetcher.domain.job.JobProperties;

public interface RequestJobProperties extends JobProperties {

    int getChunk();

    int getMaxRetries();

}
