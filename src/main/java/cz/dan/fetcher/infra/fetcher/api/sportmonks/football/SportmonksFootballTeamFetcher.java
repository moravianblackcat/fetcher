package cz.dan.fetcher.infra.fetcher.api.sportmonks.football;

import cz.dan.fetcher.domain.football.request.team.entity.FootballTeamRequestOutbox;
import cz.dan.fetcher.domain.football.request.team.inbox.entity.FootballTeamRequest;
import cz.dan.fetcher.domain.outbox.exception.resource.ResourceNotFoundException;
import cz.dan.fetcher.domain.outbox.fetcher.Fetcher;
import cz.dan.fetcher.domain.source.Source;
import cz.dan.fetcher.infra.fetcher.api.sportmonks.football.client.SportmonksFootballApiClient;
import cz.dan.fetcher.infra.fetcher.api.sportmonks.football.dto.SportmonksFootballTeamProfileDto;
import cz.dan.fetcher.infra.fetcher.api.sportmonks.football.dto.SportmonksFootballTeamProfileDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static cz.dan.fetcher.domain.source.Source.Sportmonks;

@Component
@RequiredArgsConstructor
@Slf4j
public class SportmonksFootballTeamFetcher implements Fetcher<FootballTeamRequestOutbox, FootballTeamRequest> {

    private final SportmonksFootballApiClient client;

    private final SportmonksFootballTeamProfileDtoMapper mapper;

    @Override
    public boolean supports(Source source) {
        return Sportmonks == source;
    }

    @Override
    public FootballTeamRequestOutbox get(long id) throws Exception {
        SportmonksFootballTeamProfileDto dto = getDto(id);

        return mapper.from(dto);
    }

    private SportmonksFootballTeamProfileDto getDto(long id) throws Exception {
        SportmonksFootballTeamProfileDto dto = client.getTeamProfile(id);
        if (dto == null || dto.data() == null) {
            throw new ResourceNotFoundException("Sportmonks API returned no data for football team ID %s."
                    .formatted(id));
        }

        return dto;
    }

}
