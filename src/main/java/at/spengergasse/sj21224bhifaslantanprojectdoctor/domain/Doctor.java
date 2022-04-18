package at.spengergasse.sj21224bhifaslantanprojectdoctor.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
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
@EqualsAndHashCode(callSuper = false)

@Entity
@Table(name = "doctor")
public class Doctor extends  Person{

    @NotNull(message = "Abbreviation can not be null")
    @NotBlank(message = "Abbreviation can not be blank")
    @NotEmpty(message = "Abbreviation can not be empty")
    private String abbreviation;
    @NotNull(message = "Ärztekammer Id can not be null")
    @NotBlank(message = "Ärztekammer Id can not be blank")
    @NotEmpty(message = "Ärztekammer Id can not be empty")
    private String aerztekammerId;
    @NotNull
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Subject  can not be null")
    private Subject subject;
    @NotNull
    @Enumerated(EnumType.STRING)
    @NotNull
    private Position position;
    @NotNull
    private double salary;
    private LocalDateTime created;
    private LocalDateTime updated;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Department department;
    @JsonIgnore
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.PERSIST)
    private List<Diagnosis> patientList = new ArrayList<Diagnosis>();

    @JsonIgnore
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.PERSIST)
    private List<Therapy> therapyList = new ArrayList<>();

    @Builder
    public Doctor(Name name, Gender gender, Phonenumber phonenumber,LocalDateTime created, Title title, Address address, LocalDate birthDate, String abbreviation, String aerztekammerId, Subject subject, Position position, double salary,Department department){
        super(name,gender,phonenumber,title,address,birthDate);
        this.abbreviation = abbreviation;
        this.created = created;
        this.aerztekammerId = aerztekammerId;
        this.subject = subject;
        this.position = position;
        this.salary = salary;
        this.department=department;
    }

    public String getDoctor() {
        return "Doctor{" +
                "name: " + getName() +
                "gender: " + getGender() +
                "phonenumber: " + getPhonenumber() +
                "title: " + getTitle() +
                "address: " + getAddress() +
                ", birthDate: " + getBirthDate() +
                ", abbrevitation: '" + abbreviation + '\'' +
                ", aerztekammerId: " + aerztekammerId +
                "subject: " + subject +
                "position: " + position +
                "salary: " + salary +
                ", created_at=" + created +
                ", replaced_at=" + updated +
                '}';
        }
}



