package cz.dan.fetcher.infra.fetcher.api.sportmonks.football;

import cz.dan.fetcher.domain.football.request.player.inbox.entity.FootballPlayerRequest;
import cz.dan.fetcher.domain.football.request.player.outbox.entity.FootballPlayerRequestOutbox;
import cz.dan.fetcher.domain.outbox.exception.resource.ResourceNotFoundException;
import cz.dan.fetcher.domain.outbox.fetcher.Fetcher;
import cz.dan.fetcher.domain.source.Source;
import cz.dan.fetcher.infra.fetcher.api.sportmonks.football.client.SportmonksFootballApiClient;
import cz.dan.fetcher.infra.fetcher.api.sportmonks.football.dto.SportmonksFootballPlayerProfileDto;
import cz.dan.fetcher.infra.fetcher.api.sportmonks.football.dto.SportmonksFootballPlayerProfileDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static cz.dan.fetcher.domain.source.Source.Sportmonks;

@Component
@RequiredArgsConstructor
@Slf4j
public class SportmonksFootballPlayerFetcher implements Fetcher<FootballPlayerRequestOutbox, FootballPlayerRequest> {

    private final SportmonksFootballApiClient client;

    private final SportmonksFootballPlayerProfileDtoMapper mapper;

    @Override
    public boolean supports(Source source) {
        return Sportmonks == source;
    }

    @Override
    public FootballPlayerRequestOutbox get(long id) throws Exception {
        SportmonksFootballPlayerProfileDto playerProfile = getSportmonksFootballPlayerProfileDto(id);

        return mapper.from(playerProfile);
    }

    private SportmonksFootballPlayerProfileDto getSportmonksFootballPlayerProfileDto(long id) throws Exception {
        SportmonksFootballPlayerProfileDto dto = client.getPlayerProfile(id);
        if (dto == null || dto.data() == null) {
            throw new ResourceNotFoundException("Sportmonks API returned no data for football player ID %s."
                    .formatted(id));
        }

        return dto;
    }

}
