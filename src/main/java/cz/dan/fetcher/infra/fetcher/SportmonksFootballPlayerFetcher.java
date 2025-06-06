package cz.dan.fetcher.infra.fetcher;

import cz.dan.fetcher.football.request.player.outbox.domain.entity.FootballPlayerRequestOutbox;
import cz.dan.fetcher.infra.fetcher.api.football.SportmonksFootballApiClient;
import cz.dan.fetcher.infra.fetcher.api.football.dto.SportmonksFootballPlayerProfileDto;
import cz.dan.fetcher.infra.fetcher.api.football.dto.SportmonksFootballPlayerProfileDtoMapper;
import cz.dan.fetcher.outbox.domain.entity.Source;
import cz.dan.fetcher.outbox.domain.exception.ResourceNotFoundException;
import cz.dan.fetcher.outbox.domain.fetcher.FootballPlayerFetcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static cz.dan.fetcher.outbox.domain.entity.Source.Sportmonks;

@Component
@RequiredArgsConstructor
@Slf4j
public class SportmonksFootballPlayerFetcher implements FootballPlayerFetcher {

    private final SportmonksFootballApiClient client;

    private final SportmonksFootballPlayerProfileDtoMapper mapper;

    @Override
    public boolean supports(Source source) {
        return Sportmonks == source;
    }

    @Override
    public FootballPlayerRequestOutbox getFootballPlayer(long id) throws Exception {
        SportmonksFootballPlayerProfileDto playerProfile = getSportmonksFootballPlayerProfileDto(id);

        return mapper.from(playerProfile);
    }

    private SportmonksFootballPlayerProfileDto getSportmonksFootballPlayerProfileDto(long id) throws Exception {
        SportmonksFootballPlayerProfileDto dto = client.getPlayerProfile(id);
        if (dto.data() == null) {
            throw new ResourceNotFoundException("Sportmonks API returned no data for football player ID %s."
                    .formatted(id));
        }

        return dto;
    }

}
