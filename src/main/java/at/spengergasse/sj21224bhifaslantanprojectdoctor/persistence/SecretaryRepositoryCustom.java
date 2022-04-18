package at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Ordination;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Secretary;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Component
public interface SecretaryRepositoryCustom {
    Secretary addSecretary(Secretary secretary);
    List<Secretary> getSecretaryByOrdination(Ordination ordination);
    Secretary getByAge(Integer age);
}
