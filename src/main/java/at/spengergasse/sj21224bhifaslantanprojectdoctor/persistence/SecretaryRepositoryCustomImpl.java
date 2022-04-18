package at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Gender;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Ordination;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Secretary;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class SecretaryRepositoryCustomImpl extends QuerydslRepositorySupport implements SecretaryRepositoryCustom {

    private EntityManager entityManager ;
    private final JdbcTemplate jdbcTemplate;

    public SecretaryRepositoryCustomImpl(EntityManager entityManager, JdbcTemplate jdbcTemplate) {
        super(Secretary.class);
        this.entityManager = entityManager;
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Secretary addSecretary(Secretary secretary) {
        entityManager.persist(secretary);
        return  secretary;
    }

    @Override
    public List<Secretary> getSecretaryByOrdination(Ordination ordination) {
        List<Secretary> secretaries=jdbcTemplate.query("Select * from Secretary where ordination=" + ordination, new RowMapper<Secretary>() {
            @Override
            public Secretary mapRow(ResultSet rs, int rowNum) throws SQLException {
                Secretary secretary=new Secretary();
                secretary.setBirthDate(rs.getDate("LocalDate").toLocalDate());
                secretary.setGender(Gender.valueOf(rs.getString("Gender")));

                return secretary;
            }
        });
        return  secretaries;
    }

    @Override
    public Secretary getByAge(Integer age) {
        return jdbcTemplate.queryForObject("Select * from Secretary where age=" +age,Secretary.class);
    }
}
