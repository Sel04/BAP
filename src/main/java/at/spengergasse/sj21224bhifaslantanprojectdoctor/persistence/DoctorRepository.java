package at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Department;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Diagnosis;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, DiagnosisRepositoryCustom>, QuerydslPredicateExecutor<Doctor>, DoctorRepositoryCustom {
    Optional<Doctor> findDoctorByAbbreviation(String abbreviation);
    Optional<Doctor> findDoctorById(Long id);
   // List<Doctor> findDoctorsByDepartment(Department department);
}
