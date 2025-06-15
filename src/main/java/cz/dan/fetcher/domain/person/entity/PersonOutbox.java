package cz.dan.fetcher.domain.person.entity;

import cz.dan.fetcher.domain.outbox.entity.request.Outbox;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonOutbox implements Outbox {

    @Id
    private long id;

    @Column(nullable = false, length = 3)
    private String nationality;

    @Column(nullable = false, name = "first_name")
    private String firstName;

    @Column(nullable = false, name = "last_name")
    private String lastName;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, name = "display_name")
    private String displayName;

    @Column(nullable = false, name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Override
    public void setId(Long id) {
        this.id = id;
    }

}
