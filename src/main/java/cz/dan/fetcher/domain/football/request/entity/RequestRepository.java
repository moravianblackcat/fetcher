package cz.dan.fetcher.domain.football.request.entity;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RequestRepository<T> extends CrudRepository<T, RequestId> {

    List<T> findOldestForProcessing(int limit);

}
