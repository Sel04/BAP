package at.spengergasse.sj21224bhifaslantanprojectdoctor.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "diagnosis")
public class Diagnosis extends AbstractPersistable<Long> {
   @NotNull(message = "Date can not be null")
   private LocalDate date;

   @NotNull(message = "Diagnose can not be null")
   @NotEmpty(message = "Diagnose can not be empty")
   @NotBlank(message = "Diagnose can not be blank")
   private String diagnosis;

   private LocalDateTime created;
   private LocalDateTime updated;
   @ManyToOne(cascade =CascadeType.ALL)
  // @NotNull(message = "Doctor can not be null")
   private Doctor doctor;
   @ManyToOne(cascade =CascadeType.ALL)
   private Patient patient;

   public String getDiagnoses() {
      return "Ordination{" +
              "date: '" + date + '\'' +
              "diagnoses: " + diagnosis +
              ",doctor: " + doctor.getDoctor() +
              ", patient: " + patient.getPhonenumber() +
              ", created_at: " + created +
              ", replaced_at: " + updated +
              '}';
   }
}
