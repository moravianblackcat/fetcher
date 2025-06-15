package cz.dan.fetcher.person.mapper;

import cz.dan.avro.fetcher.outbox.PersonRequest;
import cz.dan.fetcher.domain.person.entity.PersonOutbox;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(unmappedTargetPolicy = IGNORE)
public interface CustomPersonMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "request", target = "name", qualifiedByName = "mapName")
    @Mapping(source = "request", target = "displayName", qualifiedByName = "mapDisplayName")
    PersonOutbox from(PersonRequest request, long id);

    @Named("mapName")
    default String mapName(PersonRequest request) {
        return request.getName() == null
                ? concatenate(request.getFirstName(), request.getLastName())
                : request.getName();
    }

    @Named("mapDisplayName")
    default String mapDisplayName(PersonRequest request) {
        return request.getDisplayName() == null
                ? concatenate(request.getFirstName(), request.getLastName())
                : request.getDisplayName();
    }

    private String concatenate(String firstName, String lastName) {
        return firstName + " " + lastName;
    }

}
