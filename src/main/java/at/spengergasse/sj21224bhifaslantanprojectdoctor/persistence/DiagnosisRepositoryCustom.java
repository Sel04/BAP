package at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Diagnosis;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Doctor;
import jdk.jshell.Diag;

import java.util.List;


public interface DiagnosisRepositoryCustom {
    Diagnosis addDiagnosis(Diagnosis diagnosis);
    Diagnosis getByDiagnosis(String diagnosis);
    List<Diagnosis> getDiagnoisbyDoctor(Doctor doctor);
}
