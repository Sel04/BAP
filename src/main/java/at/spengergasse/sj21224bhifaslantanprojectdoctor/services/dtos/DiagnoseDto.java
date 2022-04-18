package at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Diagnosis;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Doctor;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Patient;

import java.time.LocalDate;

public record DiagnoseDto(Long id,LocalDate date, String diagnosis, Doctor doctor, Patient patient) {
    public DiagnoseDto(Diagnosis diagnosis){
        this(diagnosis.getId(),diagnosis.getDate(),diagnosis.getDiagnosis(),diagnosis.getDoctor(),diagnosis.getPatient());
    }
}
