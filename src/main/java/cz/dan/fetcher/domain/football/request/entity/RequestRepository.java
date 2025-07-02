package cz.dan.fetcher.domain.football.request.entity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface RequestRepository<T> extends CrudRepository<T, RequestId> {

    List<T> findOldestForProcessing(int limit);

}
