package at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Department;

public interface DepartmentRepositoryCustom {

    Department addDepartment(Department department);
    Department getByAbbrevation(String abbreviation);
    Department getById(int id);
}
