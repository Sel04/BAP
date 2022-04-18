package at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Diagnosis;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Doctor;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class DiagnosisRepositoryCustomImpl extends QuerydslRepositorySupport implements DiagnosisRepositoryCustom {

    private EntityManager entityManager ;
    private final JdbcTemplate jdbcTemplate;
    public DiagnosisRepositoryCustomImpl(JdbcTemplate jdbcTemplate) {
        super(Diagnosis.class);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Diagnosis addDiagnosis(Diagnosis diagnosis) {
        entityManager.persist(diagnosis);
        return diagnosis;
    }

    @Override
    public Diagnosis getByDiagnosis(String diagnosis) {
        return jdbcTemplate.queryForObject("Select * from diagnosis where diagnosis=" +diagnosis,Diagnosis.class);
    }

    @Override
    public List<Diagnosis> getDiagnoisbyDoctor(Doctor doctor) {
        List<Diagnosis> diagnosis= jdbcTemplate.query("Select * Diagnosis from where doctor=" + doctor, new RowMapper<Diagnosis>() {
            @Override
            public Diagnosis mapRow(ResultSet rs, int rowNum) throws SQLException {
                Diagnosis diagnosis=new Diagnosis();
                diagnosis.setDate(rs.getDate("LocalDate").toLocalDate());
                diagnosis.setDiagnosis(rs.getString("Diagnosis"));
                return diagnosis;
            }
        });
        return diagnosis;
    }
}
