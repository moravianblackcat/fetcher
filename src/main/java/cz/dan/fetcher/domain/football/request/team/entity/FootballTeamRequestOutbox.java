package cz.dan.fetcher.domain.football.request.team.entity;

import cz.dan.fetcher.domain.outbox.entity.request.Outbox;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FootballTeamRequestOutbox implements Outbox {

    @Id
    private long id;

    @Column(nullable = false)
    private String name;

    @Column()
    private Integer founded;

    @Column(nullable = false, length = 3)
    private String country;

    @Column(nullable = false)
    private String stadium;

    @Column
    private String city;

    @Override
    public void setId(Long id) {
        this.id = id;
    }

}
