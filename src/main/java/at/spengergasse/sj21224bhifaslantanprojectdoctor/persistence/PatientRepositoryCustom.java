package at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Patient;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Component;

@Component
public interface PatientRepositoryCustom  {
    Patient addPatient(Patient patient);
    Patient getBySvNumber(String svNumber);
    Patient getByAge(Integer age);

    void deletebyId(long id);
}
