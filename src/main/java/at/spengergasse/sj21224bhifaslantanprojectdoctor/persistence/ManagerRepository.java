package at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Address;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Doctor;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Hospital;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, ManagerRepositoryCustom>, QuerydslPredicateExecutor<Manager>, ManagerRepositoryCustom {
    //Optional<Manager> findManagerByAge(Integer age);
    Optional<Manager> findManagerByAddress(Address address);
    Optional<Manager> findManagerById(Long id);
}
