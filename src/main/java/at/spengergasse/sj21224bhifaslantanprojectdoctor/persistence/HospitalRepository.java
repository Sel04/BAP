package at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Doctor;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Hospital;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Phonenumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, HospitalRepositoryCustom>, QuerydslPredicateExecutor<Doctor>, HospitalRepositoryCustom{
    Optional<Hospital> findHospitalById (Long id);
    Optional<Hospital> findHospitalByAbbreviation(String abbreviation);
    //List<Hospital> findHospitalsByAddress(Phonenumber phonenumber);
}
