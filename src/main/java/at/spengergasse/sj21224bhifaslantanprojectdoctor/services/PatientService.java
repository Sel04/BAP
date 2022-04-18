package at.spengergasse.sj21224bhifaslantanprojectdoctor.services;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.*;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.foundation.TemporalValueFactory;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence.PatientRepository;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.exceptions.ServiceException;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = false)
public class PatientService {

    private final PatientRepository patientRepository;
    private  final TemporalValueFactory temporalValueFactory;
    //private final Logger logger;

    public String CANNOT_BE_NULL = "cannot be null!";
    public String CANNOT_BE_EMPTY_OR_BLANK = "cannot be empty or blank!";
    public String CANNOT_BE_BLANK = "cannot be blank!";
    public String CANNOT_BE_NULL_EMPTY_OR_BLANK = "cannot be null, empty or blank!";
    public String CANNOT_BE_AFTER_CURRENT_DATE = "cannot be after current date";



    public Optional<Patient> findPatient(String svnn){
        return patientRepository.findBySvNumber(svnn);
    }

    public List<Patient> getPatients(){
       return patientRepository.findAll();
    }

    public Patient createPatient(MutatePatientCommand command){
        LocalDateTime created_at = temporalValueFactory.created_at();
        Patient newPatient =null;
        try {
            var patient = patientRepository.findBySvNumber(command.getSvNumber());
            if (patient.isPresent()) {
                log.warn("Deleting {}" +patient.get().toString() +  " failed, because it already exists");
                throw new IllegalArgumentException("Patient " + patient.get().toString() + " failed, because it already exists");
            }
            else {

                newPatient = Patient.builder()
                        .name(command.getName())
                        .gender(command.getGender())
                        .phonenumber(command.getPhonenumber())
                        .title(command.getTitle())
                        .address(command.getAddress())
                        .birthDate(command.getBirthDate())
                        .svNumber(command.getSvNumber())
                        .created_at(created_at)
                        .build();
                log.info("Patient {}" + newPatient.toString() + " created at" + created_at);
                return patientRepository.save(newPatient);
            }


        }catch(PersistenceException pe){
            throw ServiceException.cannotCreateEntity(newPatient,pe);
        }
    }

    @Transactional(readOnly = false)
    public Patient createPatientFromDto(  PatientDto command){
        LocalDateTime creationS = temporalValueFactory.created_at();
        Patient newPatient =null;

        try {
            var patient = patientRepository.findBySvNumber(command.svNumber());
            if (patient.isPresent()) {
                patient.get();
              //  logger.warning("Deleting {}" +patient.get().toString() +  "failed");
            }

             newPatient = Patient.builder()
                     .name(command.name())
                     .gender(command.gender())
                     .phonenumber(command.phonenumber())
                     .title(command.title())
                     .address(command.address())
                     .birthDate(command.birthDate())
                     .svNumber(command.svNumber())
                     .created_at(creationS)
                     .build();
          //  logger.info("Patient {}" +patient.toString() + "created at" +creationS);
            return patientRepository.save(newPatient);

        }catch(PersistenceException pe){
            throw ServiceException.cannotCreateEntity(newPatient,pe);
        }
    }

    public Patient replacePatient(Long id, MutatePatientCommand command){
        LocalDateTime replaced_at = temporalValueFactory.replaced_at();
        Patient patient=null;
        Optional<Patient> entity= patientRepository.findById(id);
     try {
         if (entity.isPresent()){
             patient=entity.get();
             patient.setName(command.getName());
             patient.setGender(command.getGender());
             patient.setPhonenumber(command.getPhonenumber());
             patient.setTitle(command.getTitle());
             patient.setAddress(command.getAddress());
             patient.setSvNumber(command.getSvNumber());
             patient.setBirthDate(command.getBirthDate());
             patient.setReplaced_at(replaced_at);
         }
         log.info("Patient {}" +patient.toString() + " replaced_at" +replaced_at);
         return patientRepository.save(patient);


     }catch(PersistenceException peX){
         throw ServiceException.cannotUpdateEntity(patient,peX);
     }
    }

    public Patient partiallyUpdatePatient(Long id,   MutatePatientCommand command){
        Patient patient=null;
        Optional<Patient> entity = patientRepository.findById(id);

        if(entity.isPresent()){
             patient = entity.get();
             if(command.getName().getFirstname()!=null){
                 patient.getName().setFirstname(command.getName().getFirstname());
                 log.info("Changed FirstName from Patient " +patient.toString());
             }
            if(command.getName().getSubname()!=null){
                patient.getName().setSubname(command.getName().getSubname());
                log.info("Changed SubName from Patient " +patient.toString());
            }
            if(command.getName().getLastname()!=null){
                patient.getName().setLastname(command.getName().getLastname());
                log.info("Changed LastName from Patient " +patient.toString());
            }
            if(command.getGender()!=null){
                patient.setGender(command.getGender());
                log.info("Changed Gender from Patient " +patient.toString());
            }

            if(command.getTitle().getGrade()!=null){
                patient.getTitle().setGrade(command.getTitle().getGrade());
                log.info("Changed Grade from Patient " +patient.toString());
            }
            if(command.getTitle().getSubjectGrade()!=null){
                patient.getTitle().setSubjectGrade(command.getTitle().getSubjectGrade());
                log.info("Changed SubjectGrade from Patient " +patient.toString());
            }

            if(command.getAddress().getStreet()!=null){
                patient.getAddress().setStreet(command.getAddress().getStreet());
                log.info("Changed Street from Patient " +patient.toString());
            }
            if(command.getAddress().getPlace()!=null){
                patient.getAddress().setPlace(command.getAddress().getPlace());
                log.info("Changed Place from Patient " +patient.toString());
            }
            if(command.getAddress().getZipcode()!=null){
                patient.getAddress().setZipcode(command.getAddress().getZipcode());
                log.info("Changed ZipCode from Patient " +patient.toString());
            }

            if(command.getBirthDate()!=null){
                patient.setBirthDate(command.getBirthDate());
                log.info("Changed BirthDate from Patient " +patient.toString());
            }

            if(command.getSvNumber()!=null){
                patient.setSvNumber(command.getSvNumber());
                log.info("Changed SvNumber from Patient " +patient.toString());
            }
            return patientRepository.save(patient);
        }

        else{

            log.error("Cannot find Patient with id" + id);
            throw new IllegalStateException("Cannot find Patient with id" + id);
        }
    }


    public void deletePatient(Long id){
        LocalDateTime deleted_at = temporalValueFactory.deleted_at();
        Patient patient=null;
        Optional<Patient> entity= patientRepository.findById(id);
        try{
            if(entity.isPresent()) {
                patient=entity.get();
                patientRepository.delete(patient);
            }
          log.info("Patient {}" +patient + " deleted_at" +deleted_at);
        }catch (PersistenceException pe){
            log.error("Cannot delete Patient !");
            throw  ServiceException.cannotDeleteEntity(patient,pe);
        }
    }

    public Optional<Patient> getPatient(long id) {
        return patientRepository.findById(id);
    }

    public Optional<Patient> getPatientBySv(String sv) {
        return patientRepository.findBySvNumber(sv);
    }

    public void deletePatients() {
        patientRepository.deleteAll();
    }

    public void deletePatientById(long id) {
        patientRepository.deletebyId(id);
    }
}
