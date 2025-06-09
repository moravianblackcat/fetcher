package cz.dan.fetcher.domain.person.entity;

import cz.dan.fetcher.domain.source.Source;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_id")
    private Long sourceId;

    @Column(nullable = false)
    @Enumerated(STRING)
    private Source source;

}
