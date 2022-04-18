package at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Department;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Diagnosis;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiagnosisRepository extends JpaRepository<Diagnosis, DiagnosisRepositoryCustom>, QuerydslPredicateExecutor<Diagnosis>, DiagnosisRepositoryCustom {
   // Diagnosis findDiagnosisByPatient(Patient patient);
   Optional<Diagnosis> findDiagnosisById(Long id);
    Optional<Diagnosis> findDiagnosisByDate(LocalDate date);
    //List<Diagnosis> findDiagnosesByPatient(Patient patient);
}
