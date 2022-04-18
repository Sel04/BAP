package at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Doctor;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Patient;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Therapy;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public interface TherapyRepositoryCustom {
    Therapy addTherapy(Therapy therapy);
    Therapy getByBegin(LocalDate date);
    List<Therapy> getByDoctor(Doctor doctor);
    List<Therapy> getByPatient(Patient patient);
}
