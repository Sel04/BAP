package at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Address;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Gender;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Patient;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Secretary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SecretaryRepository extends JpaRepository<Secretary, Long>, QuerydslPredicateExecutor<Secretary>, SecretaryRepositoryCustom {
    Optional<Secretary> findSecretaryByBirthDate(LocalDate birthDate);
    List<Secretary> findSecretariesByGender(Gender gender);

    @Query(
            "SELECT CASE WHEN COUNT(s) > 0 THEN TRUE ELSE FALSE END FROM Secretary s WHERE s.birthDate = ?1")
    Boolean existsByBirthDate(LocalDate birthDate);
}
