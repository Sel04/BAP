package at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Gender;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Ordination;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long>, QuerydslPredicateExecutor<Patient>, PatientRepositoryCustom {
    List<Patient> findPatientsByGenderLike(Gender gender);
    Optional<Patient> findBySvNumber(String svn);
    @Query(
            "SELECT CASE WHEN COUNT(s) > 0 THEN TRUE ELSE FALSE END FROM Patient s WHERE s.svNumber = ?1")
    Boolean existsBySvNumber(String svn);
}
