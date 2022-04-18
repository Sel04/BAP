package at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


public record SecretaryDto (Long id,Name name, Gender gender, Phonenumber phonenumber, Title title, Address address, LocalDate birthDate,
                            LocalDateTime created_at, LocalDateTime replaced_at, OrdinationDto ordination){
    public SecretaryDto(Secretary secretary){
        this(secretary.getId(),secretary.getName(),secretary.getGender(),secretary.getPhonenumber(),secretary.getTitle(),secretary.getAddress(),secretary.getBirthDate(),secretary.getCreated_at(),secretary.getReplaced_at(),new OrdinationDto(secretary.getOrdination()));
    }
}
