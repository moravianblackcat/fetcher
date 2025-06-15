package cz.dan.fetcher.domain.person.entity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonOutboxRepository extends CrudRepository<PersonOutbox, Long> {
}
