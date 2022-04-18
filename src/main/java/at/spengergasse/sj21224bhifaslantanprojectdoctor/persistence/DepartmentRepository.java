package at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, DepartmentRepositoryCustom>, QuerydslPredicateExecutor<Department>, DepartmentRepositoryCustom {
    //Department add(Department dep, String abbreviation);
    Optional<Department> findDepartmentByName(String name);
    Optional<Department> findDepartmentById(Long id);
    //List<Department> findDepartmentsByAbbreviation(String abbrevation);

}
