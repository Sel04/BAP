package at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Doctor;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Patient;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Therapy;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Component
public class TherapyRepositoryCustomImpl extends QuerydslRepositorySupport implements TherapyRepositoryCustom {

    private EntityManager entityManager ;
    private final JdbcTemplate jdbcTemplate;

    public TherapyRepositoryCustomImpl(EntityManager entityManager, JdbcTemplate jdbcTemplate) {
        super(Therapy.class);
        this.entityManager = entityManager;
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Therapy addTherapy(Therapy therapy) {
        entityManager.persist(therapy);
        return therapy;
    }

    @Override
    public Therapy getByBegin(LocalDate date) {
        return jdbcTemplate.queryForObject("Select * from Therapy where begin=" +date,Therapy.class);
    }

    @Override
    public List<Therapy> getByDoctor(Doctor doctor) {
        List<Therapy> therapies=jdbcTemplate.query("Select * from Therapy where doctor=" + doctor, new RowMapper<Therapy>() {
            @Override
            public Therapy mapRow(ResultSet rs, int rowNum) throws SQLException {
                Therapy therapy=new Therapy();
                therapy.setBegin(rs.getDate("LocalDate").toLocalDate());
                therapy.setEnd(rs.getDate("LocalDate").toLocalDate());

                return therapy;
            }
        });

        return therapies;
    }

    @Override
    public List<Therapy> getByPatient(Patient patient) {
       List<Therapy> therapies=jdbcTemplate.query("Select * from where patient=" + patient, new RowMapper<Therapy>() {
           @Override
           public Therapy mapRow(ResultSet rs, int rowNum) throws SQLException {
               Therapy therapy=new Therapy();
               therapy.setBegin(rs.getDate("LocalDate").toLocalDate());
               therapy.setEnd(rs.getDate("LocalDate").toLocalDate());

               return therapy;
           }
       });

       return therapies;
    }
}
