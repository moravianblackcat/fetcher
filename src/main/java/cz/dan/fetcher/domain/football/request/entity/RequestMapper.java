package cz.dan.fetcher.domain.football.request.entity;

import cz.dan.fetcher.domain.source.Source;

public interface RequestMapper<T> {

    T from(long id, Source source);

}
