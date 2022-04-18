package at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Doctor;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Patient;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Therapy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TherapyRepository extends JpaRepository<Therapy, Long>, QuerydslPredicateExecutor<Therapy>, TherapyRepositoryCustom {
    Optional<Therapy> findTherapyByBeginAndEnd(LocalDate begin, LocalDate end);
    List<Therapy> findTherapiesByDoctor(Doctor doctor);
    Optional<Therapy> findTherapyByArt(String art);
    Optional<Therapy> findById(Long id);
    @Query(
            "SELECT CASE WHEN COUNT(s) > 0 THEN TRUE ELSE FALSE END FROM Therapy s WHERE s.art= ?1")
    Boolean existsByArt(String art);
}
