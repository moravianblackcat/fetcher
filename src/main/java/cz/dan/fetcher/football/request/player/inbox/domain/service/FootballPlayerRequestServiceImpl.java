package cz.dan.fetcher.football.request.player.inbox.domain.service;

import cz.dan.fetcher.football.request.player.inbox.domain.entity.FootballPlayerRequest;
import cz.dan.fetcher.football.request.player.inbox.domain.entity.FootballPlayerRequestRepository;
import cz.dan.fetcher.football.request.player.inbox.domain.entity.mapper.FootballPlayerRequestMapper;
import cz.dan.fetcher.football.request.player.inbox.domain.source.FootballPlayerRequestSource;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class FootballPlayerRequestServiceImpl implements FootballPlayerRequestService {

    private final FootballPlayerRequestMapper mapper;

    private final FootballPlayerRequestRepository repository;

    @Override
    public void saveRequests(List<Long> playerIds, FootballPlayerRequestSource source) {
        List<FootballPlayerRequest> requests =
                playerIds.stream().map(playerId -> mapper.from(playerId, source)).toList();

        log.info("Saving {} football player requests.", requests.size());
        repository.saveAll(requests);
    }

}
