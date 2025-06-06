package cz.dan.fetcher.football.request.player.outbox.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FootballPlayerRequestOutbox {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;

    @Column(nullable = false)
    private long sourceId;

    @Column(nullable = false, length = 3)
    private String nationality;

    @Column(nullable = false)
    @Enumerated(STRING)
    private Position position;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String displayName;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    @Enumerated(STRING)
    private Source source;

    public enum Position {
        goalkeeper,
        defender,
        midfielder,
        forward
    }

    public enum Source {
        Sportmonks
    }

}
