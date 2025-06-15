package cz.dan.fetcher.domain.football.request.player.outbox.job;

import cz.dan.fetcher.domain.football.request.player.outbox.entity.FootballPlayerRequestOutbox;
import cz.dan.fetcher.domain.outbox.sender.Sender;
import cz.dan.fetcher.domain.outbox.service.request.OutboxRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FootballPlayerOutboxJobTest {

    @Mock
    private FootballPlayerOutboxJobProperties jobProperties;

    @Mock
    private OutboxRequestService<FootballPlayerRequestOutbox> outboxRequestService;

    @Mock
    private Sender<FootballPlayerRequestOutbox> footballPlayerRequestOutboxSender;

    private FootballPlayerOutboxJob sut;

    @BeforeEach
    void setUp() {
        sut = new FootballPlayerOutboxJob(jobProperties, outboxRequestService,
                Set.of(footballPlayerRequestOutboxSender));
    }

    @Test
    void sendsOutboxAndDeletesIt() {
        when(outboxRequestService.getAll())
                .thenReturn(List.of(FootballPlayerRequestOutbox.builder().id(1L).build()));

        sut.run();

        verify(footballPlayerRequestOutboxSender, times(1))
                .sendOutbox(FootballPlayerRequestOutbox.builder().id(1L).build());
        verify(outboxRequestService, times(1)).delete(1L);
    }

    @Test
    void doesNotDeleteOutboxIfSendingFails() {
        when(outboxRequestService.getAll())
                .thenReturn(List.of(FootballPlayerRequestOutbox.builder().build()));
        doThrow(new RuntimeException()).when(footballPlayerRequestOutboxSender).sendOutbox(any());

        sut.run();

        verifyNoMoreInteractions(outboxRequestService);
    }

}