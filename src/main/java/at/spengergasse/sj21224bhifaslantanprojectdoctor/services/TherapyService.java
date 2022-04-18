package at.spengergasse.sj21224bhifaslantanprojectdoctor.services;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.*;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence.DepartmentRepository;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence.DoctorRepository;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence.PatientRepository;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence.TherapyRepository;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.exceptions.ServiceException;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.MutateTherapyCommand;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.foundation.TemporalValueFactory;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.TheraphyDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class TherapyService {

    private final TherapyRepository therapyRepository;
    private final TemporalValueFactory temporalValueFactory;
 //   private final Logger logger;

    public String CANNOT_BE_NULL = "cannot be null!";
    public String CANNOT_BE_EMPTY_OR_BLANK = "cannot be empty or blank!";
    public String CANNOT_BE_BLANK = "cannot be blank!";
    public String CANNOT_BE_NULL_EMPTY_OR_BLANK = "cannot be null, empty or blank!";
    public String CANNOT_BE_AFTER_CURRENT_DATE = "cannot be after current date";

    public Optional<Therapy> findTheraphy(LocalDate begin, LocalDate end){
        return  therapyRepository.findTherapyByBeginAndEnd(begin,end);
    }

    public void deleteTheraphy(){
        therapyRepository.deleteAll();
    }

    public List<Therapy> getTheraphies(){
        return therapyRepository.findAll();
    }

    public Optional<Therapy> getTheraphy( Long id)
    {

        return therapyRepository.findById(id);
    }


    public Therapy createTheraphy(MutateTherapyCommand command){
        LocalDateTime creationTS = temporalValueFactory.created_at();
        Department department=null;
        Doctor head=null;
        Patient patient=null;
        Doctor doctor=null;
        Therapy newTheraphy =null;


        try {
            var th = therapyRepository.findTherapyByBeginAndEnd(command.getBegin(),command.getEnd());
            if (th.isPresent()) {
                th.get();
                log.warn("Deleting " +th.get().toString() + " failed");
                throw new IllegalArgumentException("Theraphy " +th.get().toString() + " failed");

            }


            department= Department.builder()
                    .abbreviation(command.getDoctor().getDepartment().getAbbreviation())
                    .name(command.getDoctor().getDepartment().getName())
                    .created(creationTS)
                    .build();

            log.debug("Department has been saved : " + department.toString());

            patient=Patient.builder()
                    .name(command.getPatient().getName())
                    .gender(command.getPatient().getGender())
                    .phonenumber(command.getPatient().getPhonenumber())
                    .title(command.getPatient().getTitle())
                    .address(command.getPatient().getAddress())
                    .birthDate(command.getPatient().getBirthDate())
                    .svNumber(command.getPatient().getSvNumber())
                    .created_at(creationTS)
                    .build();

            log.debug("Patient has been saved : " + patient.toString());


            doctor=Doctor.builder()
                    .name(command.getDoctor().getName())
                    .gender(command.getDoctor().getGender())
                    .phonenumber(command.getDoctor().getPhonenumber())
                    .title(command.getDoctor().getTitle())
                    .address(command.getDoctor().getAddress())
                    .birthDate(command.getDoctor().getBirthDate())
                    .abbreviation(command.getDoctor().getAbbreviation())
                    .aerztekammerId(command.getDoctor().getAerztekammerId())
                    .subject(command.getDoctor().getSubject())
                    .position(command.getDoctor().getPosition())
                    .salary(command.getDoctor().getSalary())
                    .department(department)
                    .created(creationTS)
                    .build();

            log.debug("Doctor has been saved : " + doctor.toString());

            newTheraphy = Therapy.builder()
                    .art(command.getArt())
                    .begin(command.getBegin())
                    .end(command.getEnd())
                    .patient(patient)
                    .doctor(doctor)
                    .created_at(creationTS)
                    .build();
           log.info("Theraphy  " +newTheraphy.toString() + " deleted at" +creationTS);
            return therapyRepository.save(newTheraphy);

        }catch(PersistenceException pe){
            throw ServiceException.cannotCreateEntity(newTheraphy,pe);
        }
    }

    public Therapy createTheraphyFromDto(TheraphyDto command){
        LocalDateTime creationTS = temporalValueFactory.created_at();
        Therapy newTheraphy =null;

        try {
            var th = therapyRepository.findTherapyByBeginAndEnd(command.begin(),command.end());
            if (th.isPresent()) {
                log.error("Theraphy " +th.get().toString() + " failed");
                throw new IllegalArgumentException("Theraphy " +th.get().toString() + " failed");
            }
            else {

                newTheraphy = Therapy.builder().art(command.art()).begin(command.begin()).end(command.end())
                        .build();
                log.info("Theraphy  " + newTheraphy.toString() + " created at" + creationTS);
                return therapyRepository.save(newTheraphy);
            }

        }catch(PersistenceException pe){
            throw ServiceException.cannotCreateEntity(newTheraphy,pe);
        }
    }

    public Therapy replaceTheraphy(Long id, MutateTherapyCommand command){
        LocalDateTime replaced_at = temporalValueFactory.replaced_at();
        Therapy therapy=null;
        Optional<Therapy> entity = therapyRepository.findById(id);
        try{
            if(entity.isPresent()){
                therapy=entity.get();
                therapy.setArt(command.getArt());
                therapy.setBegin(command.getBegin());
                therapy.setEnd(command.getEnd());
                therapy.setDoctor(command.getDoctor());
                therapy.setPatient(command.getPatient());
            }
            log.info("Theraphy  " +therapy.toString() + " replaced at" +replaced_at);
            return therapyRepository.save(therapy);

        }catch(PersistenceException peX){
            throw ServiceException.cannotUpdateEntity(therapy,peX);
        }
    }

    public Therapy partiallyUpdateTheraphy(Long id,  MutateTherapyCommand command){
        Optional<Therapy> entity = therapyRepository.findById(id);

        if(entity.isPresent()){
            Therapy therapy=entity.get();
            if(command.getArt()!=null) {
                therapy.setArt(command.getArt());
                log.info("Changed Art from Therapy " +id);
            }

            if(command.getBegin()!=null) {
                therapy.setBegin(command.getBegin());
                log.info("Changed Begin from Therapy " +id);
            }

            if(command.getEnd()!=null) {
                therapy.setEnd(command.getEnd());
                log.info("Changed End from Therapy " +id);
            }

            if(command.getDoctor()!=null) {
                therapy.setDoctor(command.getDoctor());
                log.info("Changed Doctor from Therapy " +id);
            }

            if(command.getPatient()!=null){
                therapy.setPatient(command.getPatient());
                log.info("Changed Patient from Therapy " +id);
            }
           if(therapy.getDoctor().getAbbreviation()!=null) {
               therapy.getDoctor().setAbbreviation(command.getDoctor().getAbbreviation());
               log.info("Changed Doctor Abbreviaiton from Theraphy " + " " +id);
           }
           if(therapy.getDoctor().getAerztekammerId()!=null) {
               therapy.getDoctor().setAerztekammerId(command.getDoctor().getAerztekammerId());
               log.info("Changed Doctor's AerzteKammerId from Theraphy " + " " +id);
           }
           if(therapy.getDoctor().getSubject()!=null) {
               therapy.getDoctor().setSubject(command.getDoctor().getSubject());
               log.info("Changed Doctor's Subject from Theraphy " + " " +id);
           }
           if(therapy.getDoctor().getPosition()!=null) {
               therapy.getDoctor().setPosition(command.getDoctor().getPosition());
               log.info("Changed Doctor's Position from Theraphy " + " " +id);
           }
           if(therapy.getDoctor().getSalary()>0) {
               therapy.getDoctor().setSalary(command.getDoctor().getSalary());
               log.info("Changed Doctor's Salary from Theraphy " + " " +id);
           }
           if(therapy.getDoctor().getName().getFirstname()!=null) {
               therapy.getDoctor().getName().setFirstname(command.getDoctor().getName().getFirstname());
               log.info("Changed Doctor's Firstname from Theraphy " + " " +id);
           }
           if(therapy.getDoctor().getName().getSubname()!=null){
               therapy.getDoctor().getName().setSubname(command.getDoctor().getName().getSubname());
               log.info("Changed Doctor's SubName from Theraphy " + " " +id);
           }
           if(therapy.getDoctor().getName().getLastname()!=null){
                therapy.getDoctor().getName().setLastname(command.getDoctor().getName().getLastname());
                log.info("Changed Doctor's SubName from Theraphy " + " " +id);
           }
           if(therapy.getDoctor().getGender()!=null) {
               therapy.getDoctor().setGender(command.getDoctor().getGender());
               log.info("Changed Doctor's Gender from Theraphy " + " " +id);
           }
           if(therapy.getDoctor().getPhonenumber().getAreacode()!=null){
               therapy.getDoctor().getPhonenumber().setAreacode(command.getDoctor().getPhonenumber().getAreacode());
               log.info("Changed Doctor's AreaCode from Theraphy " + " " +id);
           }
           if(therapy.getDoctor().getPhonenumber().getCountrycode()!=null){
                therapy.getDoctor().getPhonenumber().setAreacode(command.getDoctor().getPhonenumber().getAreacode());
                log.info("Changed Doctor's CountryCode from Theraphy " + " " +id);
           }
           if(therapy.getDoctor().getPhonenumber().getSerialnumber()!=null){
                therapy.getDoctor().getPhonenumber().setAreacode(command.getDoctor().getPhonenumber().getAreacode());
                log.info("Changed Doctor's Serialnumber from Theraphy " + " " +id);
           }
           if(therapy.getDoctor().getTitle().getGrade()!=null){
               therapy.getDoctor().getTitle().setGrade(command.getDoctor().getTitle().getGrade());
               log.info("Changed Doctor's Grade from Theraphy " + " " +id);
           }
           if(therapy.getDoctor().getTitle().getSubjectGrade()!=null){
                therapy.getDoctor().getTitle().setSubjectGrade(command.getDoctor().getTitle().getSubjectGrade());
                log.info("Changed Doctor's Grade from Theraphy " + " " +id);
            }
            if(command.getDoctor().getAddress().getStreet()!=null) {
                therapy.getDoctor().getAddress().setStreet(command.getDoctor().getAddress().getStreet());
                log.info("Changed Doctor's  Adress-Street from Theraphy " + " " +id);
            }
            if(command.getDoctor().getAddress().getPlace()!=null) {
                therapy.getDoctor().getAddress().setPlace(command.getDoctor().getAddress().getPlace());
                log.info("Changed Doctor  Place from Theraphy " + " " +id);
            }
            if(command.getDoctor().getAddress().getZipcode()!=null) {
                therapy.getDoctor().getAddress().setZipcode(command.getDoctor().getAddress().getZipcode());
                log.info("Changed Doctor  ZipCode from Therapy " + " " +id);
            }
            if(command.getDoctor().getBirthDate()!=null && command.getDoctor().getBirthDate().isBefore(LocalDate.now())){
                log.info("Changed Doctor's Birthdate from Theraphy " + " " +id);
                therapy.getDoctor().setBirthDate(command.getDoctor().getBirthDate());
            }

            if(command.getPatient().getName().getFirstname()!=null){
                therapy.getPatient().getName().setFirstname(command.getPatient().getName().getFirstname());
                log.info("Changed Patient's FirstName from Theraphy " + " " +id);
            }
            if(command.getPatient().getName().getSubname()!=null){
                therapy.getPatient().getName().setSubname(command.getPatient().getName().getSubname());
                log.info("Changed Patient's SubName from Theraphy " + " " +id);
            }
            if(command.getPatient().getName().getLastname()!=null){
                therapy.getPatient().getName().setLastname(command.getPatient().getName().getLastname());
                log.info("Changed Patient's LastName from Theraphy " + " " +id);
            }

            if(command.getPatient().getGender()!=null){
                therapy.getPatient().setGender(command.getPatient().getGender());
                log.info("Changed Patient's Gender from Theraphy " + " " +id);
            }
            if(command.getPatient().getAddress().getStreet()!=null){
                therapy.getPatient().getAddress().setStreet(command.getPatient().getAddress().getStreet());
                log.info("Changed Patient's Street from Theraphy " + " " +id);
            }
            if(command.getPatient().getAddress().getPlace()!=null){
                therapy.getPatient().getAddress().setPlace(command.getPatient().getAddress().getPlace());
                log.info("Changed Patient's Place from Theraphy " + " " +id);
            }
            if(command.getPatient().getAddress().getZipcode()!=null){
                therapy.getPatient().getAddress().setZipcode(command.getPatient().getAddress().getZipcode());
                log.info("Changed Patient's ZipCode from Theraphy " + " " +id);
            }

            if(command.getPatient().getBirthDate()!=null){
                therapy.getPatient().setBirthDate(command.getPatient().getBirthDate());
                log.info("Changed Patient's BirthDate from Theraphy " +id);
            }

            if(command.getPatient().getSvNumber()!=null){
                therapy.getPatient().setSvNumber(command.getPatient().getSvNumber());
                log.info("Changed Patient's SvNumber from Theraphy " +id);
            }
        }
        else{
            log.error("Cannot find Theraphy with id" +id);
            throw new IllegalArgumentException("Cannot find Theraphy with id " +id);
        }
        return null;
    }

    public void deleteTheraphy(Long id){
        LocalDateTime deleted_at = temporalValueFactory.deleted_at();
        Therapy therapy= null;
        Optional<Therapy> entity = therapyRepository.findById(id);
        try{
            if(entity.isPresent()){
                therapy=entity.get();
                therapyRepository.delete(therapy);
            }
           log.info("Theraphy  " +therapy.toString() + " deleted at" +deleted_at);
        }catch(PersistenceException pe){
            log.error("Cannot Delete Theraphy!");
            throw ServiceException.cannotDeleteEntity(therapy,pe);
        }
    }

}
