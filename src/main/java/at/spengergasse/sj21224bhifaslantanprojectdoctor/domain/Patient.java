package at.spengergasse.sj21224bhifaslantanprojectdoctor.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)

@Entity
@Table(name = "patient")
public class Patient extends Person {
    @NotNull
    @NotBlank(message = "SvNumber required")
    private String svNumber;
    @JsonIgnore
    @OneToMany(mappedBy = "patient", cascade = CascadeType.PERSIST)
    private List<Diagnosis> diagnoses = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "patient",cascade = CascadeType.PERSIST)
    private List<Therapy> therapies=new ArrayList<>();

    private LocalDateTime created_at;
    private LocalDateTime replaced_at;
    @Builder
    public Patient(Name name, Gender gender, Phonenumber phonenumber, Title title, Address address, LocalDate birthDate, String svNumber,LocalDateTime created_at, LocalDateTime replaced_at){
        super(name,gender,phonenumber,title,address,birthDate);
        this.svNumber = svNumber;
        this.created_at=created_at;
        this.replaced_at=replaced_at;
    }

    @Override
    public String toString() {
        return super.toString() + "Patient{" +
                "svNumber='" + svNumber + '\'' +
                ", created_at=" + created_at +
                ", replaced_at=" + replaced_at +
                '}';
    }


}