package at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Department;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Doctor;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Position;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class DoctorRepositoryCustomImpl extends QuerydslRepositorySupport implements DoctorRepositoryCustom {

    private EntityManager entityManager ;
    private final JdbcTemplate jdbcTemplate;

    public DoctorRepositoryCustomImpl(EntityManager entityManager, JdbcTemplate jdbcTemplate) {
        super(Doctor.class);
        this.entityManager = entityManager;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Doctor addDoctor(Doctor doctor) {
       entityManager.persist(doctor);
       return doctor;
    }



    @Override
    public Doctor getByAbbreviation(String abbreviation) {
        return jdbcTemplate.queryForObject("Select * from Doctor where abbreviation =" +abbreviation,Doctor.class);
    }

    @Override
    public List<Doctor> getDoctorByDepartment(Department department) {
        List<Doctor> doctors=jdbcTemplate.query("Select * from doctor where department=" + department, new RowMapper<Doctor>() {
            @Override
            public Doctor mapRow(ResultSet rs, int rowNum) throws SQLException {
                Doctor doctor=new Doctor();
                doctor.setBirthDate(rs.getDate("LocalDate").toLocalDate());
                doctor.setAbbreviation(rs.getString("Abbreviation"));
                doctor.setPosition(Position.valueOf(rs.getString("Position")));

                return doctor;
            }
        });
        return doctors;
    }


}
