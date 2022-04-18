package at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Hospital;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Component
public class HospitalRepositoryCustomImpl extends QuerydslRepositorySupport implements HospitalRepositoryCustom {

    private EntityManager entityManager ;
    private final JdbcTemplate jdbcTemplate;

    public HospitalRepositoryCustomImpl(EntityManager entityManager, JdbcTemplate jdbcTemplate) {
        super(Hospital.class);
        this.entityManager = entityManager;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Hospital addHospital(Hospital hospital) {
        entityManager.persist(hospital);
        return hospital;
    }

    @Override
    public Hospital getByPhonenumber(String phonenumber) {
        return jdbcTemplate.queryForObject("Select * from Hospital where phonenumber=" +phonenumber,Hospital.class);
    }

    @Override
    public Hospital getByAbbreviation(String abbreviation) {
        return jdbcTemplate.queryForObject("Select * from Hospital where abbreviation =" +abbreviation,Hospital.class);
    }
}
