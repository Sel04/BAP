package at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Department;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Doctor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DoctorRepositoryCustom {
    Doctor addDoctor(Doctor doctor);
    Doctor getByAbbreviation(String abbreviation);
    List<Doctor> getDoctorByDepartment(Department department);



}
