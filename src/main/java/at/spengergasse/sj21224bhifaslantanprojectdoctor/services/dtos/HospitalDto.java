package at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos;


import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.*;

import java.time.LocalDateTime;

public record HospitalDto(String abbreviation, String name, LocalDateTime created, Phonenumber phonenumber, Address address, Doctor head, Manager manager) {
   public HospitalDto(Hospital hospital){
       this(hospital.getAbbreviation(),hospital.getName(),hospital.getCreated(),hospital.getPhoneNumber(),hospital.getAddress(),hospital.getHead(),hospital.getManager());
   }
}
