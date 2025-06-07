package cz.dan.fetcher.infra.fetcher.api.football.sportmonks.dto;

import cz.dan.fetcher.domain.football.request.player.outbox.entity.FootballPlayerRequestOutbox;
import cz.dan.fetcher.infra.fetcher.api.football.sportmonks.dto.SportmonksFootballPlayerProfileDto.Data.Position;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import static cz.dan.fetcher.domain.football.request.player.outbox.entity.FootballPlayerRequestOutbox.Position.forward;
import static cz.dan.fetcher.infra.fetcher.api.football.sportmonks.dto.SportmonksFootballPlayerProfileDto.Data.PositionCode.attacker;
import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(unmappedTargetPolicy = IGNORE)
public interface SportmonksFootballPlayerProfileDtoMapper {

    @Mapping(source = "data.id", target = "sourceId")
    @Mapping(source = "data.nationality.iso3", target = "nationality")
    @Mapping(source = "data.position", target = "position", qualifiedByName = "mapPosition")
    @Mapping(source = "data.firstName", target = "firstName")
    @Mapping(source = "data.lastName", target = "lastName")
    @Mapping(source = "data.name", target = "name")
    @Mapping(source = "data.displayName", target = "displayName")
    @Mapping(source = "data.dateOfBirth", target = "dateOfBirth")
    @Mapping(constant = "Sportmonks", target = "source")
    FootballPlayerRequestOutbox from(SportmonksFootballPlayerProfileDto dto);

    @Named("mapPosition")
    default FootballPlayerRequestOutbox.Position mapPosition(Position position) {
        if (attacker == position.code()) {
            return forward;
        } else {
            return FootballPlayerRequestOutbox.Position.valueOf(position.code().name());
        }
    }

}
