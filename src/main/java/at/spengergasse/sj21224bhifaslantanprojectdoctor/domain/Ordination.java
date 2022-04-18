package at.spengergasse.sj21224bhifaslantanprojectdoctor.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "ordination")
public class Ordination extends AbstractPersistable<Long> {
    @NotNull
    @NotBlank(message = "Abbreviation required")
    private String abbrevitation;
    @NotNull
    @Embedded
    private Address address;


    //private Doctor headdoctor;

    private String name;
    private LocalDateTime created_at;
    private LocalDateTime replaced_at;
    @OneToOne
    private Manager manager;

    //   @ManyToOne
    //   List<Doctor> doctors = new ArrayList<>();

    @OneToMany(mappedBy = "ordination", cascade = CascadeType.REMOVE)
    List<Secretary> secretaries = new ArrayList<>();




    @Override
    public String toString() {
        return "Ordination{" +
                "abbrevitation='" + abbrevitation + '\'' +
                ", address=" + address +
                ", name='" + name + '\'' +
                ", created_at=" + created_at +
                ", replaced_at=" + replaced_at +
                '}';
    }
}
