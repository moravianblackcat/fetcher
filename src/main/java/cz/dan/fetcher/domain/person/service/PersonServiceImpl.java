package cz.dan.fetcher.domain.person.service;

import cz.dan.fetcher.domain.person.entity.Person;
import cz.dan.fetcher.domain.person.entity.PersonRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    @Override
    public Long save(Person person) {
        return personRepository.save(person).getId();
    }

}
