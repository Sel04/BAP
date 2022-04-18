package at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public record PatientDto(Long id, Name name, Gender gender, Phonenumber phonenumber, Title title, String svNumber, Address address, LocalDate birthDate
        , LocalDateTime created, LocalDateTime replaced){
    public PatientDto(Patient patient){
        this(patient.getId(),patient.getName(),patient.getGender(),patient.getPhonenumber(),patient.getTitle(),patient.getSvNumber(),patient.getAddress(),patient.getBirthDate(),patient.getCreated_at()
                ,patient.getReplaced_at());
    }
}
