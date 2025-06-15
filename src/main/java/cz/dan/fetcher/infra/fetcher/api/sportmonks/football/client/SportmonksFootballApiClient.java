package cz.dan.fetcher.infra.fetcher.api.sportmonks.football.client;

import cz.dan.fetcher.infra.fetcher.api.sportmonks.config.SportmonksFeignConfig;
import cz.dan.fetcher.infra.fetcher.api.sportmonks.football.dto.SportmonksFootballCoachProfileDto;
import cz.dan.fetcher.infra.fetcher.api.sportmonks.football.dto.SportmonksFootballPlayerProfileDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "sportmonksFootballClient",
        url = "${sportmonks.rest.api.url}",
        configuration = SportmonksFeignConfig.class)
public interface SportmonksFootballApiClient {

    @GetMapping("/football/players/{playerId}?include=nationality;position")
    SportmonksFootballPlayerProfileDto getPlayerProfile(@PathVariable("playerId") long playerId) throws Exception;

    @GetMapping("/football/coaches/{coachId}?include=nationality")
    SportmonksFootballCoachProfileDto getCoachProfile(@PathVariable("coachId") long coachId) throws Exception;

}
