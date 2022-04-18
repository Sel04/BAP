package at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Patient;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Component
public class PatientRepositoryCustomImpl extends QuerydslRepositorySupport implements PatientRepositoryCustom {

    private EntityManager entityManager ;
    private final JdbcTemplate jdbcTemplate;

    public PatientRepositoryCustomImpl(EntityManager entityManager, JdbcTemplate jdbcTemplate) {
        super(Patient.class);
        this.entityManager = entityManager;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Patient addPatient(Patient patient) {
        entityManager.persist(patient);
        return patient;
    }

    @Override
    public Patient getBySvNumber(String svNumber) {
        return jdbcTemplate.queryForObject("Select * from Patient where svNumber=" +svNumber,Patient.class);
    }

    @Override
    public Patient getByAge(Integer age) {
        return jdbcTemplate.queryForObject("Select * from Patient where age=" +age,Patient.class);
    }

    @Override
    public void deletebyId(long id) {
         jdbcTemplate.queryForObject("Delete Patient where id=" +id,Patient.class);
    }
}
