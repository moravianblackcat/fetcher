package cz.dan.fetcher.football.request.player.inbox.domain.service;

import cz.dan.fetcher.football.request.player.inbox.domain.entity.FootballPlayerRequest;
import cz.dan.fetcher.football.request.player.inbox.domain.entity.FootballPlayerRequestRepository;
import cz.dan.fetcher.football.request.player.inbox.domain.entity.mapper.FootballPlayerRequestMapper;
import cz.dan.fetcher.football.request.player.inbox.domain.source.FootballPlayerRequestSource;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FootballPlayerRequestServiceImpl implements FootballPlayerRequestService {

    private final FootballPlayerRequestMapper mapper;

    private final FootballPlayerRequestRepository repository;

    @Override
    public void save(FootballPlayerRequest request) {
        repository.save(request);
    }

    @Override
    public void saveRequests(List<Long> playerIds, FootballPlayerRequestSource source) {
        List<FootballPlayerRequest> requests =
                playerIds.stream().map(playerId -> mapper.from(playerId, source)).toList();

        log.info("Saving {} football player requests.", requests.size());
        repository.saveAll(requests);
    }

    @Override
    public List<FootballPlayerRequest> getOldestScheduled(int number) {
        return repository.findOldestScheduled(number);
    }

    @Override
    public void setToRetryAndSave(FootballPlayerRequest request) {
        setToStateAndSave(FootballPlayerRequest::toRetry, request);
    }

    @Override
    public void setToErrorAndSave(FootballPlayerRequest request) {
        setToStateAndSave(FootballPlayerRequest::toError, request);
    }

    @Override
    public void setToResourceNotFoundAndSave(FootballPlayerRequest request) {
        setToStateAndSave(FootballPlayerRequest::toResourceNotFound, request);
    }

    @Override
    public void setToCompletedAndSave(FootballPlayerRequest request) {
        setToStateAndSave(FootballPlayerRequest::toCompleted, request);
    }

    private void setToStateAndSave(Consumer<FootballPlayerRequest> setToState, FootballPlayerRequest request) {
        setToState.accept(request);
        repository.save(request);
    }

}
