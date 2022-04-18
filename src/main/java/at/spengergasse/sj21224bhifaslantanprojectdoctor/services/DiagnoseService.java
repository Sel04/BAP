package at.spengergasse.sj21224bhifaslantanprojectdoctor.services;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Diagnosis;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Doctor;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Patient;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence.DiagnosisRepository;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.exceptions.ServiceException;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.DiagnoseDto;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.MutateDiagnosisCommand;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.foundation.TemporalValueFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class DiagnoseService {

    private final DiagnosisRepository diagnosisRepository;
    private final TemporalValueFactory temporalValueFactory;

    public String CANNOT_BE_NULL = "cannot be null!";
    public String CANNOT_BE_EMPTY_OR_BLANK = "cannot be empty or blank!";
    public String CANNOT_BE_BLANK = "cannot be blank!";
    public String CANNOT_BE_NULL_EMPTY_OR_BLANK = "cannot be null, empty or blank!";

    public Diagnosis createDiagnosis(MutateDiagnosisCommand mutateDiagnosisCommand){
        Diagnosis newDiagnosis = null;
        Doctor doctor = null;
        Patient patient = null;
        LocalDateTime created = temporalValueFactory.created_at();
        /*if(mutateDiagnosisCommand.getDate() == null) {
            log.error("Date " + CANNOT_BE_NULL);
            throw new IllegalArgumentException("Date " + CANNOT_BE_NULL);
        }
        else if(mutateDiagnosisCommand.getDoctor() == null){
            log.error("Doctor: " + CANNOT_BE_NULL);
            throw new IllegalArgumentException("Doctor: " + CANNOT_BE_NULL);
        }
        else if(mutateDiagnosisCommand.getPatient() == null){
            log.error("Patient: " + CANNOT_BE_NULL);
            throw new IllegalArgumentException("Patient: " + CANNOT_BE_NULL);
        }
        else if(mutateDiagnosisCommand.getDiagnosis().isEmpty() || mutateDiagnosisCommand.getDiagnosis().isBlank()){
            log.error("Diagnoses: " + CANNOT_BE_EMPTY_OR_BLANK);
            throw new IllegalArgumentException("Diagnoses: " + CANNOT_BE_EMPTY_OR_BLANK);
        }
         */
        try{
            var diagnose = diagnosisRepository.findDiagnosisByDate(mutateDiagnosisCommand.getDate());
            if(diagnose.isPresent()){
                return diagnose.get();
            }

             doctor= mutateDiagnosisCommand.getDoctor();
             patient = mutateDiagnosisCommand.getPatient();
             newDiagnosis = Diagnosis.builder().created(created).diagnosis(mutateDiagnosisCommand.getDiagnosis()).date(mutateDiagnosisCommand.getDate()).doctor(mutateDiagnosisCommand.getDoctor()).patient(mutateDiagnosisCommand.getPatient()).build();
                log.info("Diagnoses {} " + newDiagnosis.getDiagnoses() + "created " + created);
             return diagnosisRepository.save(newDiagnosis);
        }
        catch(PersistenceException pEx){
            log.error("Cannot create Diagnoses " +  newDiagnosis.getDiagnosis());
            throw ServiceException.cannotCreateEntity(newDiagnosis,pEx);
        }

    }

    public Diagnosis createDiagnosisFromDto(DiagnoseDto diagnoseDto){
        Diagnosis newDiagnosis = null;
        LocalDateTime created = temporalValueFactory.created_at();
        /*if(diagnoseDto.date() == null) {
            log.error("Date " + CANNOT_BE_NULL);
            throw new IllegalArgumentException("Date " + CANNOT_BE_NULL);
        }
        else if(diagnoseDto.doctor() == null){
            log.error("Doctor: " + CANNOT_BE_NULL);
            throw new IllegalArgumentException("Doctor: " + CANNOT_BE_NULL);
        }
        else if(diagnoseDto.patient() == null){
            log.error("Patient: " + CANNOT_BE_NULL);
            throw new IllegalArgumentException("Patient: " + CANNOT_BE_NULL);
        }
        else if(diagnoseDto.diagnosis().isEmpty() || diagnoseDto.diagnosis().isBlank()){
            log.error("Diagnoses: " + CANNOT_BE_EMPTY_OR_BLANK);
            throw new IllegalArgumentException("Diagnoses: " + CANNOT_BE_EMPTY_OR_BLANK);
        }
         */
        try{
            var diagnose = diagnosisRepository.findDiagnosisByDate(diagnoseDto.date());
            if(diagnose.isPresent()){
                return diagnose.get();
            }
            newDiagnosis = Diagnosis.builder().created(created).diagnosis(diagnoseDto.diagnosis()).date(diagnoseDto.date()).doctor(diagnoseDto.doctor()).patient(diagnoseDto.patient()).build();
            log.info("Diagnoses {} " + newDiagnosis.getDiagnoses() + "created " + created);
            return diagnosisRepository.save(newDiagnosis);
        }
        catch(PersistenceException pEx){
            log.error("Cannot create Diagnoses " +  newDiagnosis.getDiagnosis());
            throw ServiceException.cannotCreateEntity(newDiagnosis,pEx);
        }

    }

    public void  deleteDiagnosis(Long id){
        Diagnosis diagnosis = null;
        Optional<Diagnosis> diagnosis1 = diagnosisRepository.findDiagnosisById(id);
        try{
            if(diagnosis1.isPresent()){
                diagnosis = diagnosis1.get();
                diagnosisRepository.delete(diagnosis);
                log.info("Diagnoses: " + diagnosis.getDiagnoses() + "deleted: " + LocalDateTime.now());
            }
        }catch(PersistenceException ex){
            log.error("Cannot delete Diagnoses!" +  diagnosis.getDiagnoses());
            throw ServiceException.cannotDeleteEntity(diagnosis,ex);
        }

    }

    public void  deleteDiagnoses()
    {
        diagnosisRepository.deleteAll();
        log.info("Diagnoses deleted");
    }


    public List<Diagnosis> getDiagnosis(){
        var diagnoses =diagnosisRepository.findAll();
        log.info("Found {} diagnoses", diagnoses.size());
        return diagnoses;
    }

    public Optional<Diagnosis> getDiagnosisbyId(Long id){
        return diagnosisRepository.findDiagnosisById(id);
    }

    public Optional<Diagnosis> getDiagnosisbyDate(LocalDate time){
        return diagnosisRepository.findDiagnosisByDate(time);
    }

    public Diagnosis replaceDiagnosis(Long id, MutateDiagnosisCommand mutateDiagnosisCommand){
        Optional<Diagnosis> entity = diagnosisRepository.findDiagnosisById(id);
        LocalDateTime replace = temporalValueFactory.replaced_at();
        Diagnosis diagnosis = null;
        try{
            if(entity.isPresent()){
                diagnosis = entity.get();
                diagnosis.setDate(mutateDiagnosisCommand.getDate());
                diagnosis.setDiagnosis(mutateDiagnosisCommand.getDiagnosis());
                diagnosis.setDoctor(mutateDiagnosisCommand.getDoctor());
                diagnosis.setPatient(mutateDiagnosisCommand.getPatient());
                diagnosis.setUpdated(replace);

            }
            log.info("Diagnoses {} " + diagnosis.getDiagnoses() + "replaced: " + replace);
             return diagnosisRepository.save(diagnosis);
        }catch(PersistenceException ex){
            log.error("Cannot delete Diagnoses " + diagnosis.getDiagnoses());
            throw ServiceException.cannotUpdateEntity(diagnosis,ex);
        }
    }

    public Diagnosis partiallyUpdate(Long id,MutateDiagnosisCommand mutateDiagnosisCommand){
        Optional<Diagnosis> diagnosises = diagnosisRepository.findDiagnosisById(id);
        LocalDateTime replace = temporalValueFactory.replaced_at();
        if(diagnosises.isPresent()){
            Diagnosis diagnosis = diagnosises.get();
            if(mutateDiagnosisCommand.getDiagnosis() != null){
                diagnosis.setDiagnosis(mutateDiagnosisCommand.getDiagnosis());
            }
            if(mutateDiagnosisCommand.getDate() != null){
                diagnosis.setDate(mutateDiagnosisCommand.getDate());
            }
            if(mutateDiagnosisCommand.getDoctor() != null){
                diagnosis.setDoctor(mutateDiagnosisCommand.getDoctor());
            }
            diagnosis.setUpdated(replace);
            log.info("Diagnoses {} " + diagnosis.getDiagnoses() + "replaced at: " + replace);
            return diagnosisRepository.save(diagnosis);
        }
        return diagnosises.get();
    }

}
