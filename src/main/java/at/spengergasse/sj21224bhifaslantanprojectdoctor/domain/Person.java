package at.spengergasse.sj21224bhifaslantanprojectdoctor.domain;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.*;
import lombok.*;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)


@Table(name = "person")
@MappedSuperclass
public abstract class Person  extends AbstractPersistable<Long> {

    @Embedded
    @NotNull
    private Name name;
    @NotNull
    private Gender gender;
    @Embedded
    @NotNull
    private Phonenumber phonenumber;
    @Embedded
    @NotNull
    private Title title;
    @Embedded
    @NotNull
    private Address address;
    @NotNull
    private LocalDate birthDate;

    @Override
    public String toString() {
        return "Person{" +
                "name=" + name +
                ", gender=" + gender +
                ", phonenumber=" + phonenumber +
                ", title=" + title +
                ", address=" + address +
                ", birthDate=" + birthDate +
                '}';
    }
}