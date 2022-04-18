package at.spengergasse.sj21224bhifaslantanprojectdoctor.domain;


import org.hibernate.annotations.Cascade;
import org.springframework.data.jpa.domain.AbstractPersistable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
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
@Table(name = "hospital")
public class Hospital extends AbstractPersistable<Long> {
    @NotNull(message = "Abbreviation can not be null")
    @NotBlank(message = "Abbreviation can not be blank")
    @NotEmpty(message = "Abbreviation can not be empty")
    private String abbreviation;
    @NotNull(message = "Name can not be null")
    @NotBlank(message = "Name can not be blank")
    @NotEmpty(message = "Name can not be empty")
    private String name;
    private LocalDateTime created;
    private LocalDateTime updated;
    @Embedded
    @NotNull(message = "Phonenumber can not be null")
    private Phonenumber phoneNumber;
    @Embedded
    @NotNull(message = "Address can not be null")
    private Address address;
    @OneToOne(cascade = CascadeType.ALL)
    @NotNull(message = "Head can not be null")
    private Doctor head;
    @OneToOne(cascade = CascadeType.ALL)
    @NotNull(message = "Manager can not be null")
    private Manager manager;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Department> departmentList = new ArrayList<Department>();

    public String getHospital() {
        return "Ordination{" +
                "name: '" + name + '\'' +
                "abbreviation: " + abbreviation +
                ",phoneNumber: " + phoneNumber +
                ", head: " + head +
                ", manager: " + manager +
                ", address: " + address +
                ", created_at: " + created +
                ", replaced_at: " + updated +
                '}';
    }

}
