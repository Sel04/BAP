package at.spengergasse.sj21224bhifaslantanprojectdoctor.domain;
import lombok.*;


import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)


@Entity
@Table(name = "secretary")
public class Secretary extends Person {
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Ordination ordination;

    private LocalDateTime created_at;
    private LocalDateTime replaced_at;

    @Builder
    public Secretary(Name name, Gender gender, Phonenumber phonenumber, Title title, Address address, LocalDate birthDate,LocalDateTime created_at, LocalDateTime replaced_at,Ordination ordination){
        super(name,gender,phonenumber,title,address,birthDate);
        this.created_at=created_at;
        this.replaced_at=replaced_at;
        this.ordination=ordination;
    }

    @Override
    public String toString() {
        return super.toString()+ "Secretary{" +
                "ordination=" + ordination +
                ", created_at=" + created_at +
                ", replaced_at=" + replaced_at +
                '}';
    }
}
