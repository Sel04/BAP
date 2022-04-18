package at.spengergasse.sj21224bhifaslantanprojectdoctor.domain;


import org.aspectj.lang.annotation.Before;
import org.springframework.data.jpa.domain.AbstractPersistable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@Builder

@Entity
@Table(name = "theraphy")
public class Therapy extends AbstractPersistable<Long> {
    @NotNull
    @NotBlank(message = "Art undefined")
    private String art;
    @NotNull
    private LocalDate begin;
    @NotNull
    private LocalDate end;

    private LocalDateTime created_at;
    private LocalDateTime replaced_at;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Doctor doctor;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Patient patient;




}
