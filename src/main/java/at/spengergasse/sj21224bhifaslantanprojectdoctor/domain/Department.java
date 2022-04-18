package at.spengergasse.sj21224bhifaslantanprojectdoctor.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "department")
public class Department extends AbstractPersistable<Long> {
    @NotNull(message = "Abbreviation can not be null")
    @NotBlank(message = "Abbreviation can not be blank")
    @NotEmpty(message = "Abbreviation can not be empty")
    private String abbreviation;

    @NotNull(message = "Name can not be null")
    @NotBlank(message = "Name can not be blank")
    @NotEmpty(message = "Name can not be empty")
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @NotNull(message = "Head can not be null")
    private Doctor head;
    @ManyToOne(cascade = CascadeType.ALL)
   //@NotNull(message = "Hospital can not be null")
    private Hospital hospital;

    private LocalDateTime created;
    private LocalDateTime updated;



    @JsonIgnore
    @OneToMany(mappedBy = "department", cascade = CascadeType.REMOVE)
    private List<Doctor> doctorList = new ArrayList<Doctor>();

    public String getDepartment() {
        return "Ordination{" +
                "abbrevitation ='" + abbreviation + '\'' +
                ", name =" + name +
                ", name='" + name + '\'' +
                ", created_at=" + created +
                ", replaced_at=" + updated +
                '}';
    }
}