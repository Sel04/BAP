package at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Address;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Doctor;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Manager;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Phonenumber;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MutateHospitalCommand {
    private String abbreviation;
    private String name;
    private Phonenumber phonenumber;
    private Address address;
    private Doctor head;
    private Manager manager;

}
