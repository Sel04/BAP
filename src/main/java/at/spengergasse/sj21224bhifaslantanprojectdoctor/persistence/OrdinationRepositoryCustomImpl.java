package at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Ordination;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.Optional;

@Component
public class OrdinationRepositoryCustomImpl extends QuerydslRepositorySupport implements OrdinationRepositoryCustom {

    private final EntityManager entityManager ;
    private final JdbcTemplate jdbcTemplate;

    public OrdinationRepositoryCustomImpl(EntityManager entityManager, JdbcTemplate jdbcTemplate) {
        super(Ordination.class);
        this.entityManager = entityManager;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Ordination addOrdination(Ordination ordination) {
        entityManager.persist(ordination);
        return ordination;
    }

    @Override
    public Ordination getByAbbreviation(String abbreviation) {
        return jdbcTemplate.queryForObject("Select * from Ordination where abbreviation=" +abbreviation,Ordination.class);
    }

    @Override
    public Ordination getByName(String name) {
        return jdbcTemplate.queryForObject("Select * from Ordination where name=" +name,Ordination.class);
    }

    @Override
    public Ordination update(long id, Ordination newOrdination) {
      return null;
    }

    @Override
    public void deleteById(long id) {

    }


}
