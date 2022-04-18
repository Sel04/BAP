package at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos;
        ;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.*;
import java.time.LocalDate;
public record DoctorDto(Long id, Name name, Gender gender, Phonenumber phonenumber, Title title, Address address, LocalDate date, String abbreviation, String aerztekammerId, Subject subject, Position position, double salary,Department department) {
public DoctorDto(Doctor doctor){
        this(doctor.getId(),doctor.getName(),doctor.getGender(),doctor.getPhonenumber(),doctor.getTitle(),doctor.getAddress(),doctor.getBirthDate(),doctor.getAbbreviation(),doctor.getAerztekammerId(),doctor.getSubject(),doctor.getPosition(),doctor.getSalary(),doctor.getDepartment());
 }
}