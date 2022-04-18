package at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Address;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Ordination;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public interface OrdinationRepositoryCustom {
    Ordination addOrdination(Ordination ordination);
    Ordination getByAbbreviation(String abbreviation);
    Ordination getByName(String name);
    Ordination update(long id, Ordination newOrdination);
    void deleteById(long id);
}
