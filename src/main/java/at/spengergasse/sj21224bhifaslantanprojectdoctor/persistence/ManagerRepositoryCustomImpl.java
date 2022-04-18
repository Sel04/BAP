package at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Manager;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.time.LocalDate;

@Component
public class ManagerRepositoryCustomImpl extends QuerydslRepositorySupport implements ManagerRepositoryCustom {

    private EntityManager entityManager ;
    private final JdbcTemplate jdbcTemplate;

    public ManagerRepositoryCustomImpl(EntityManager entityManager, JdbcTemplate jdbcTemplate) {
        super(Manager.class);
        this.entityManager = entityManager;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Manager addManager(Manager manager) {
        entityManager.persist(manager);
        return manager;
    }

    @Override
    public Manager getLocalDate(LocalDate date) {
        return jdbcTemplate.queryForObject("Select * from Manager where date=" +date,Manager.class);
    }

    @Override
    public Manager getAge(Integer age) {
        return jdbcTemplate.queryForObject("Select * from Manager where age=" +age,Manager.class);
    }
}
