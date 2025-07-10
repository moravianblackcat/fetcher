package cz.dan.fetcher.domain.football.request.player.inbox.entity;

import cz.dan.fetcher.domain.football.request.entity.RequestId;
import cz.dan.fetcher.domain.inbox.entity.request.Request;
import cz.dan.fetcher.domain.source.Source;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.EAGER;

@EqualsAndHashCode(callSuper = true)
@Slf4j
@Entity
@IdClass(RequestId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class FootballPlayerRequest extends Request {

    @Id
    @Column(nullable = false)
    private Long id;

    @Id
    @Column(nullable = false)
    @Enumerated(STRING)
    private Source source;

    @Column(nullable = false)
    @Default
    private OffsetDateTime createdAt = ZonedDateTime.now(Clock.systemUTC()).toOffsetDateTime();

    @OneToMany(mappedBy = "footballPlayerRequest", cascade = ALL, orphanRemoval = true, fetch = EAGER)
    @Default
    private List<FootballPlayerRequestFailureDetail> failureDetails = new ArrayList<>();

    @Override
    public void addFailureDetail(String reason) {
        failureDetails.add(FootballPlayerRequestFailureDetail.builder()
                .footballPlayerRequest(this)
                .reason(reason)
                .build());
    }

    @Override
    public int getNumberOfFailures() {
        return failureDetails.size();
    }

}
