package at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Department;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.support.QuerydslJpaPredicateExecutor;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;


@Component
public class DepartmentRepositoryCustomImpl extends QuerydslRepositorySupport implements DepartmentRepositoryCustom {

    private EntityManager entityManager ;
    private final JdbcTemplate jdbcTemplate;


    public DepartmentRepositoryCustomImpl(EntityManager entityManager, JdbcTemplate jdbcTemplate) {
        super(Department.class);
        this.entityManager = entityManager;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Department addDepartment(Department department) {
        entityManager.persist(department);
        return  department;
    }

    @Override
    public Department getByAbbrevation(String abbreviation) {
         return jdbcTemplate.queryForObject("Select * from department where abbrevation=" +abbreviation,Department.class);
    }

    @Override
    public Department getById(int id) {
        return jdbcTemplate.queryForObject("Select * from department where id=" +id,Department.class);
    }


}
