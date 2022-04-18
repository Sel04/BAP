package at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Address;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Manager;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Ordination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Repository
public interface OrdinationRepository extends JpaRepository<Ordination, Long>, QuerydslPredicateExecutor<Ordination>, OrdinationRepositoryCustom {
    Optional<Ordination> findOrdinationByAbbrevitation(String abbreviation);
    @Query(
            "SELECT CASE WHEN COUNT(s) > 0 THEN TRUE ELSE FALSE END FROM Ordination s WHERE s.abbrevitation = ?1")
    Boolean existsOrdinationById(String abbreviation);
}
