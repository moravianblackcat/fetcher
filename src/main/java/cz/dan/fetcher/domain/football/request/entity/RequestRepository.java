package cz.dan.fetcher.domain.football.request.entity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Set;

@NoRepositoryBean
public interface RequestRepository<T> extends CrudRepository<T, RequestId> {

    Set<Long> findAlreadyExistingRequestIds(List<Long> ids, String source);

    List<T> findOldestForProcessing(int limit);

}
