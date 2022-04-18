package at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Therapy;

import java.time.LocalDate;
import java.time.LocalDateTime;


public record TheraphyDto(Long id,String art, LocalDate begin, LocalDate end, LocalDateTime created, LocalDateTime replaced, PatientDto patient,DoctorDto doctor){
   public TheraphyDto(Therapy therapy){
       this(therapy.getId(),therapy.getArt(),therapy.getBegin(),therapy.getEnd(),therapy.getCreated_at(),therapy.getReplaced_at(), new PatientDto(therapy.getPatient()),
               new DoctorDto(therapy.getDoctor()) );
   }
}
