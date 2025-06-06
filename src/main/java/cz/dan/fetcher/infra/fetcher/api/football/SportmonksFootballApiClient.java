package cz.dan.fetcher.infra.fetcher.api.football;

import cz.dan.fetcher.infra.fetcher.api.football.dto.SportmonksFootballPlayerProfileDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "sportmonksFootballClient",
        url = "${sportmonks.rest.api.url}")
public interface SportmonksFootballApiClient {

    @GetMapping("/football/players/{playerId}?include=nationality;position")
    SportmonksFootballPlayerProfileDto getPlayerProfile(@PathVariable("playerId") long playerId) throws Exception;

}
