package cz.dan.fetcher.domain.football.request.entity;

import cz.dan.fetcher.domain.source.Source;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static jakarta.persistence.EnumType.STRING;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RequestId implements Serializable {

    private Long id;

    @Enumerated(STRING)
    private Source source;

}
