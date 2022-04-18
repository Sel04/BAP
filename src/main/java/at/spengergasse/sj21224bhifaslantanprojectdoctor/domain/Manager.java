package at.spengergasse.sj21224bhifaslantanprojectdoctor.domain;

import lombok.*;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)

@Entity
@Table(name = "manager")
public class Manager extends  Person {
    private LocalDateTime created;
    private LocalDateTime updated;
    @OneToOne
   // @NotNull(message = "Ordination can not be null")
    private Ordination ordination;

    @Builder
    public Manager(Name name,LocalDateTime created,LocalDateTime updated, Gender gender, Phonenumber phonenumber, Title title, Address address, LocalDate birthDate, Ordination ordination){
        super(name,gender,phonenumber,title,address,birthDate);
        this.ordination = ordination;
        this.created = created;
        this.updated = updated;
    }

    public String getManager() {
        return "Manager{ " +
                "name: " + getName() +
                "gender: " + getGender() +
                "phonenumber: " + getPhonenumber() +
                "title: " + getTitle() +
                "address: " + getAddress() +
                ", birthDate: " + getBirthDate() +
                ", ordination: " + ordination +
                ", created_at=" + created +
                ", replaced_at=" + updated +
                '}';
    }
}
