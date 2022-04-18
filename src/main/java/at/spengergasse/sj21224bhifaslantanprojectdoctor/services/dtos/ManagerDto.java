package at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.*;

import java.time.LocalDate;


public record ManagerDto (Long id,Name name,Gender gender,Phonenumber phonenumber,Title title,Address address,LocalDate date,Ordination ordination) {
    public ManagerDto(Manager manager){
     this(manager.getId(), manager.getName(),manager.getGender(),manager.getPhonenumber(),manager.getTitle(),manager.getAddress(),manager.getBirthDate(),manager.getOrdination());
    }
}
