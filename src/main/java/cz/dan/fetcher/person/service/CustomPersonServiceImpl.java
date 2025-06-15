package cz.dan.fetcher.person.service;

import cz.dan.avro.fetcher.outbox.PersonRequest;
import cz.dan.fetcher.domain.outbox.service.request.OutboxRequestService;
import cz.dan.fetcher.domain.person.entity.Person;
import cz.dan.fetcher.domain.person.entity.PersonOutbox;
import cz.dan.fetcher.domain.person.service.PersonService;
import cz.dan.fetcher.person.mapper.CustomPersonMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static cz.dan.fetcher.domain.source.Source.Custom;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class CustomPersonServiceImpl implements CustomPersonService {

    private final CustomPersonMapper customPersonMapper;

    private final OutboxRequestService<PersonOutbox> outboxRequestService;

    private final PersonService personService;

    @Override
    public void save(PersonRequest request) {
        Long internalId = personService.save(Person.builder().source(Custom).build());
        outboxRequestService.save(customPersonMapper.from(request, internalId));
    }

}
