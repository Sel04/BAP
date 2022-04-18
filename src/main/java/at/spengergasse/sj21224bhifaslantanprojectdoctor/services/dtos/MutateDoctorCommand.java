package at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MutateDoctorCommand {
    private Name name;
    private Gender gender;
    private Phonenumber phonenumber;
    private Title title;
    private Address address;
    private LocalDate birthDate;
    private String abbreviation;
    private String aerztekammerId;
    private Subject subject;
    private Position position;
    private double salary;
    private Department department;
}
