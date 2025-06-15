package cz.dan.fetcher.infra.fetcher.api.sportmonks;

import cz.dan.fetcher.domain.football.request.coach.inbox.entity.FootballCoachRequest;
import cz.dan.fetcher.domain.outbox.exception.resource.ResourceNotFoundException;
import cz.dan.fetcher.domain.outbox.fetcher.Fetcher;
import cz.dan.fetcher.domain.person.entity.PersonOutbox;
import cz.dan.fetcher.domain.source.Source;
import cz.dan.fetcher.infra.fetcher.api.sportmonks.football.client.SportmonksFootballApiClient;
import cz.dan.fetcher.infra.fetcher.api.sportmonks.football.dto.SportmonksFootballCoachProfileDto;
import cz.dan.fetcher.infra.fetcher.api.sportmonks.football.dto.SportmonksFootballCoachProfileDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static cz.dan.fetcher.domain.source.Source.Sportmonks;

@Component
@RequiredArgsConstructor
@Slf4j
public class SportmonksFootballCoachFetcher implements Fetcher<PersonOutbox, FootballCoachRequest> {

    private final SportmonksFootballApiClient client;

    private final SportmonksFootballCoachProfileDtoMapper mapper;

    @Override
    public boolean supports(Source source) {
        return Sportmonks == source;
    }

    @Override
    public PersonOutbox get(long id) throws Exception {
        SportmonksFootballCoachProfileDto coachProfile = getSportmonksFootballCoachProfileDto(id);

        return mapper.from(coachProfile);
    }

    private SportmonksFootballCoachProfileDto getSportmonksFootballCoachProfileDto(long id) throws Exception {
        SportmonksFootballCoachProfileDto dto = client.getCoachProfile(id);
        if (dto == null || dto.data() == null) {
            throw new ResourceNotFoundException("Sportmonks API returned no data for football player ID %s."
                    .formatted(id));
        }

        return dto;
    }

}
