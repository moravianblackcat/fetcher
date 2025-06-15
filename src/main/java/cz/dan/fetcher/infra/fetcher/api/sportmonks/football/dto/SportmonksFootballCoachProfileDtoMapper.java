package cz.dan.fetcher.infra.fetcher.api.sportmonks.football.dto;

import cz.dan.fetcher.domain.person.entity.PersonOutbox;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(unmappedTargetPolicy = IGNORE)
public interface SportmonksFootballCoachProfileDtoMapper {

    @Mapping(source = "data.nationality.iso3", target = "nationality")
    @Mapping(source = "data.firstName", target = "firstName")
    @Mapping(source = "data.lastName", target = "lastName")
    @Mapping(source = "data.name", target = "name")
    @Mapping(source = "data.displayName", target = "displayName")
    @Mapping(source = "data.dateOfBirth", target = "dateOfBirth")
    PersonOutbox from(SportmonksFootballCoachProfileDto dto);

}
