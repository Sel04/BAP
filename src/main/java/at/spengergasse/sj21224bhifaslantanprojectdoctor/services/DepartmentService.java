package at.spengergasse.sj21224bhifaslantanprojectdoctor.services;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Department;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence.DepartmentRepository;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.exceptions.ServiceException;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.DepartmentDto;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.MutateDepartmentCommand;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.foundation.TemporalValueFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final TemporalValueFactory temporalValueFactory;

    public String CANNOT_BE_NULL = "cannot be null!";
    public String CANNOT_BE_EMPTY_OR_BLANK = "cannot be empty or blank!";
    public String CANNOT_BE_BLANK = "cannot be blank!";
    public String CANNOT_BE_NULL_EMPTY_OR_BLANK = "cannot be null, empty or blank!";


    public Department createDepartment(MutateDepartmentCommand mutateDepartmentCommand){
        Department newDepartment = null;
        LocalDateTime created = temporalValueFactory.created_at();
       /* if(mutateDepartmentCommand.getAbbreviation() == null || mutateDepartmentCommand.getAbbreviation().isEmpty() || mutateDepartmentCommand.getAbbreviation().isBlank()){
            log.error("Abbreviation " + CANNOT_BE_NULL_EMPTY_OR_BLANK);
            throw new IllegalArgumentException("Abbreviation " + CANNOT_BE_NULL_EMPTY_OR_BLANK);
        }
        else if(mutateDepartmentCommand.getName() == null || mutateDepartmentCommand.getName().isEmpty() || mutateDepartmentCommand.getName().isBlank()){
            log.error("Name " + CANNOT_BE_NULL_EMPTY_OR_BLANK);
            throw new IllegalArgumentException("Name " + CANNOT_BE_NULL_EMPTY_OR_BLANK);
        }
        else if(mutateDepartmentCommand.getHead() == null){
            log.error("Head " + CANNOT_BE_NULL);
            throw new IllegalArgumentException("Head " + CANNOT_BE_NULL);
       }
       */

        try {
            var department = departmentRepository.findDepartmentByName(mutateDepartmentCommand.getName());
            if (department.isPresent()) {
                return department.get();
            }
             newDepartment = Department.builder().created(created).name(mutateDepartmentCommand.getName()).abbreviation(mutateDepartmentCommand.getAbbreviation()).head(mutateDepartmentCommand.getHead()).build();
            log.info("Department " + newDepartment.getDepartment() + " created_at: " + created);
            return departmentRepository.save(newDepartment);
        }
        catch(PersistenceException pEx){
            //if(dep.getName().isEmpty() || dep.getName().isBlank() || dep.getAbbreviation().isEmpty() || dep.getAbbreviation().isBlank() || dep.getHead() == null) {
            log.error("Cannot create Department " + newDepartment.getDepartment());
            throw ServiceException.cannotCreateEntity(newDepartment, pEx);
            //}
        }

    }

    @Transactional
    public Department createDepartmentfromDto(DepartmentDto departmentDto){
        Department newDepartment = null;
        LocalDateTime created = temporalValueFactory.created_at();
       /* if(departmentDto.abbreviation() == null || departmentDto.abbreviation().isEmpty() || departmentDto.abbreviation().isBlank()){
            log.error("Abbreviation " + CANNOT_BE_NULL_EMPTY_OR_BLANK);
            throw new IllegalArgumentException("Abbreviation " + CANNOT_BE_NULL_EMPTY_OR_BLANK);
        }
        else if(departmentDto.name() == null || departmentDto.name().isEmpty() || departmentDto.name().isBlank()){
            log.error("Name " + CANNOT_BE_NULL_EMPTY_OR_BLANK);
            throw new IllegalArgumentException("Name " + CANNOT_BE_NULL_EMPTY_OR_BLANK);
        }
        else if(departmentDto.doctor() == null){
            log.error("Head " + CANNOT_BE_NULL);
            throw new IllegalArgumentException("Head " + CANNOT_BE_NULL);
        }
        */
        try {
            var department = departmentRepository.findDepartmentByName(departmentDto.name());
            if (department.isPresent()) {
                return department.get();
            }
            newDepartment = Department.builder().name(departmentDto.name()).abbreviation(departmentDto.abbreviation()).head(departmentDto.doctor()).created(created).build();
            log.info("Department " + newDepartment.getDepartment() + " created_at: " + created);
            return departmentRepository.save(newDepartment);
        }
        catch(PersistenceException pEx){
            //if(dep.getName().isEmpty() || dep.getName().isBlank() || dep.getAbbreviation().isEmpty() || dep.getAbbreviation().isBlank() || dep.getHead() == null) {
            log.error("Cannot create Department " + newDepartment.getDepartment());
            throw ServiceException.cannotCreateEntity(newDepartment, pEx);
            //}
        }

    }

    @Transactional
    public Department partiallyUpdate(Long id,MutateDepartmentCommand mutateDepartmentCommand){
        Optional<Department> departmentes = departmentRepository.findDepartmentById(id);
        LocalDateTime replaced = temporalValueFactory.replaced_at();
        if(departmentes.isPresent()){
            Department department = departmentes.get();
            if(mutateDepartmentCommand.getAbbreviation() != null){
                department.setAbbreviation(mutateDepartmentCommand.getAbbreviation());
            }
            if(mutateDepartmentCommand.getHead() != null){
                department.setHead(mutateDepartmentCommand.getHead());
            }
            if(mutateDepartmentCommand.getName() != null){
                department.setName(mutateDepartmentCommand.getName());
            }
            if(mutateDepartmentCommand.getHospital() != null){
                department.setHospital(mutateDepartmentCommand.getHospital());
            }
            department.setUpdated(replaced);
            log.info("Department " + department.getDepartment() + " updated: " + replaced);
            return departmentRepository.save(department);
        }
        return departmentes.get();
    }

    public void  deleteDepartment(Long id){
       Department department= null;
        Optional<Department> entity = departmentRepository.findDepartmentById(id);
        try{
            if(entity.isPresent()){
                department = entity.get();
                departmentRepository.delete(department);
                log.info("Department " + department.getDepartment() + " deleted: " + LocalDateTime.now());

            }
        }catch(PersistenceException pe){
            log.error("Cannot delete Department: " + department.getDepartment());
            throw ServiceException.cannotDeleteEntity(department, pe);
        }
    }

    public void deleteDepartments(){
        log.info("Departments deleted");
        departmentRepository.deleteAll();
    }

    public List<Department> getDepartments(){
        var departments =departmentRepository.findAll();
        log.info("Found departments",departments.size());
        return departments;
    }

    public  Optional<Department> getDepartmentById(Long id){
        var department = departmentRepository.findDepartmentById(id);
        return department;
    }

    public Department replaceDepartment(Long id, MutateDepartmentCommand mutateDepartmentCommand){
        Optional<Department> entity = departmentRepository.findDepartmentById(id);
        Department department = null;
        LocalDateTime replaced = temporalValueFactory.replaced_at();
        try{
            if(entity.isPresent()){
                department = entity.get();
                department.setAbbreviation(mutateDepartmentCommand.getAbbreviation());
                department.setName(mutateDepartmentCommand.getName());
                department.setHead(mutateDepartmentCommand.getHead());
                department.setHospital(mutateDepartmentCommand.getHospital());
                department.setUpdated(replaced);
            }
            log.info("Department " + department.getDepartment() + "replaced at: " + replaced);
            return departmentRepository.save(department);
        }catch(PersistenceException ex){
            log.error("Cannot update Department: " + department.getDepartment());
            throw ServiceException.cannotUpdateEntity(department,ex);
        }
    }





}
