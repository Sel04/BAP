package at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Department;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Doctor;

import java.time.LocalDateTime;

public record DepartmentDto(Long id,String abbreviation, String name, Doctor doctor, LocalDateTime created,DoctorDto  head) {
    public DepartmentDto(Department department){
        this(department.getId(),department.getAbbreviation(),department.getName(),department.getHead(),department.getCreated(),
                new DoctorDto(department.getHead()));
    }
}
