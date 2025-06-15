package cz.dan.fetcher.domain.person.service;

import cz.dan.fetcher.domain.outbox.service.request.OutboxRequestService;
import cz.dan.fetcher.domain.person.entity.PersonOutbox;
import cz.dan.fetcher.domain.person.entity.PersonOutboxRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
public class PersonOutboxService extends OutboxRequestService<PersonOutbox> {

    public PersonOutboxService(PersonOutboxRepository repository) {
        super(repository);
    }

}
