package cz.dan.fetcher.domain.football.request.player.inbox.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FootballPlayerRequestFailureDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private OffsetDateTime timestamp;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "player_id", referencedColumnName = "id"),
            @JoinColumn(name = "source", referencedColumnName = "source")
    })
    private FootballPlayerRequest footballPlayerRequest;

}