package at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Manager;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public interface ManagerRepositoryCustom {
    Manager addManager(Manager manager);
    Manager getLocalDate(LocalDate date);
    Manager getAge(Integer age);
}
