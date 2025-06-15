package cz.dan.fetcher.person.service;

import cz.dan.avro.fetcher.outbox.PersonRequest;
import cz.dan.fetcher.domain.outbox.service.request.OutboxRequestService;
import cz.dan.fetcher.domain.person.entity.Person;
import cz.dan.fetcher.domain.person.entity.PersonOutbox;
import cz.dan.fetcher.domain.person.service.PersonService;
import cz.dan.fetcher.person.mapper.CustomPersonMapper;
import cz.dan.fetcher.person.mapper.CustomPersonMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static cz.dan.fetcher.domain.source.Source.Custom;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomPersonServiceImplTest {

    @Mock
    private OutboxRequestService<PersonOutbox> outboxRequestService;

    @Mock
    private PersonService personService;

    @Spy
    private final CustomPersonMapper customPersonMapper = new CustomPersonMapperImpl();

    @InjectMocks
    private CustomPersonServiceImpl sut;

    @Test
    void savesIntoOutboxWithAnInternalId() {
        when(personService.save(Person.builder().source(Custom).build())).thenReturn(55L);

        sut.save(PersonRequest.newBuilder()
                .setFirstName("Craig")
                .setLastName("Bellamy")
                .setNationality("WAL")
                .build());

        verify(outboxRequestService, times(1))
                .save(PersonOutbox.builder()
                        .id(55L)
                        .firstName("Craig")
                        .lastName("Bellamy")
                        .name("Craig Bellamy")
                        .displayName("Craig Bellamy")
                        .nationality("WAL")
                        .build());
    }

}