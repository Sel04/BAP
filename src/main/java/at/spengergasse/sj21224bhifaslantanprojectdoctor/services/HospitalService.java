package at.spengergasse.sj21224bhifaslantanprojectdoctor.services;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.*;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence.HospitalRepository;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.exceptions.ServiceException;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.HospitalDto;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.MutateHospitalCommand;
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
public class HospitalService {

    private final HospitalRepository hospitalRepository;
    private final TemporalValueFactory temporalValueFactory;

    private String CANNOT_BE_NULL = "cannot be null!";
    private String CANNOT_BE_EMPTY_OR_BLANK = "cannot be empty or blank!";
    private String CANNOT_BE_BLANK = "cannot be blank!";
    private String CANNOT_BE_NULL_EMPTY_OR_BLANK = "cannot be null, empty or blank!";

    public Hospital createHospital(MutateHospitalCommand hospital){
        Hospital newHospital = null;
        Doctor doctor = null;
        Manager manager = null;
        LocalDateTime created = temporalValueFactory.created_at();
       /* if(hospital.getName() == null || hospital.getName().isEmpty() || hospital.getName().isBlank()){
            log.error("Name " + CANNOT_BE_NULL_EMPTY_OR_BLANK);
            throw new IllegalArgumentException("Name " + CANNOT_BE_NULL_EMPTY_OR_BLANK);
        }
        else if(hospital.getPhonenumber() == null){
            log.error("Phonenumber " + CANNOT_BE_NULL);
            throw new IllegalArgumentException("Phonenumber " + CANNOT_BE_NULL);
        }
        else if(hospital.getAbbreviation() == null || hospital.getAbbreviation() .isBlank() || hospital.getAbbreviation() .isEmpty()){
            log.error("Abbreviation " + CANNOT_BE_NULL_EMPTY_OR_BLANK);
            throw new IllegalArgumentException("Abbreviation " + CANNOT_BE_NULL_EMPTY_OR_BLANK);
        }
        else if(hospital.getAddress() == null){
            log.error("Address " + CANNOT_BE_NULL);
            throw new IllegalArgumentException("Address " + CANNOT_BE_NULL);
        }
        else if(hospital.getAddress().getPlace().isBlank() || hospital.getAddress().getPlace().isEmpty()){
            log.error("Place of address" + CANNOT_BE_EMPTY_OR_BLANK);
            throw new IllegalArgumentException("Place of address" + CANNOT_BE_EMPTY_OR_BLANK);
        }
        else if(hospital.getAddress().getStreet().isBlank() || hospital.getAddress().getStreet().isEmpty()) {
            log.error("Street of address " + CANNOT_BE_EMPTY_OR_BLANK);
            throw new IllegalArgumentException("Street of address " + CANNOT_BE_EMPTY_OR_BLANK);
        }
        */
       try{
           var ahospital = hospitalRepository.findHospitalByAbbreviation(hospital.getAbbreviation());
           if(ahospital.isPresent()){
             return ahospital.get();
           }
           manager = Manager.builder()
                   .ordination(hospital.getManager().getOrdination())
                   .birthDate(hospital.getManager().getBirthDate())
                   .gender(hospital.getManager().getGender())
                   .phonenumber(hospital.getManager().getPhonenumber())
                   .title(hospital.getManager().getTitle())
                   .name(hospital.getManager().getName())
                   .address(hospital.getManager().getAddress())
                   .build();
           doctor = Doctor.builder()
                   .abbreviation(hospital.getHead().getAbbreviation())
                   .aerztekammerId(hospital.getHead().getAerztekammerId())
                   .gender(hospital.getHead().getGender())
                   .name(hospital.getHead().getName())
                   .phonenumber(hospital.getHead().getPhonenumber())
                   .position(hospital.getHead().getPosition())
                   .salary(hospital.getHead().getSalary())
                   .subject(hospital.getHead().getSubject())
                   .title(hospital.getHead().getTitle())
                   .birthDate(hospital.getHead().getBirthDate())
                   .address(hospital.getHead().getAddress())
                   .build();
            newHospital = Hospital.builder().abbreviation(hospital.getAbbreviation()).created(created).address(hospital.getAddress()).head(doctor).manager(manager).name(hospital.getName()).phoneNumber(hospital.getPhonenumber()).build();
           log.info("Hospital " + newHospital.getHospital() + " created at " + created);
            return hospitalRepository.save(newHospital);
       }
       catch(PersistenceException pEx){
           log.error("Hospital " + newHospital.getHospital() + " created at" + created);
           throw ServiceException.cannotCreateEntity(newHospital, pEx);
       }

    }

    public Hospital createHospitalFromDto(HospitalDto hospital){
        Hospital newHospital = null;
        LocalDateTime created = temporalValueFactory.created_at();

       /* if(hospital.name() == null || hospital.name().isEmpty() || hospital.name().isBlank()){
            log.error("Name " + CANNOT_BE_NULL_EMPTY_OR_BLANK);
            throw new IllegalArgumentException("Name " + CANNOT_BE_NULL_EMPTY_OR_BLANK);
        }
        else if(hospital.phonenumber() == null){
            log.error("Phonenumber " + CANNOT_BE_NULL);
            throw new IllegalArgumentException("Phonenumber " + CANNOT_BE_NULL);
        }
        else if(hospital.abbreviation() == null || hospital.abbreviation() .isBlank() || hospital.abbreviation() .isEmpty()){
            log.error("Abbreviation " + CANNOT_BE_NULL_EMPTY_OR_BLANK);
            throw new IllegalArgumentException("Abbreviation " + CANNOT_BE_NULL_EMPTY_OR_BLANK);
        }
        else if(hospital.address() == null){
            log.error("Address " + CANNOT_BE_NULL);
            throw new IllegalArgumentException("Address " + CANNOT_BE_NULL);
        }
        else if(hospital.address().getPlace().isBlank() || hospital.address().getPlace().isEmpty()){
            log.error("Place of address " + CANNOT_BE_NULL_EMPTY_OR_BLANK);
            throw new IllegalArgumentException("Place of address" + CANNOT_BE_EMPTY_OR_BLANK);
        }
        else if(hospital.address().getStreet().isBlank() || hospital.address().getStreet().isEmpty()) {
            log.error("Street of address " + CANNOT_BE_NULL_EMPTY_OR_BLANK);
            throw new IllegalArgumentException("Street of address " + CANNOT_BE_EMPTY_OR_BLANK);
        }
        */
        try{
            var ahospital = hospitalRepository.findHospitalByAbbreviation(hospital.abbreviation());
            if(ahospital.isPresent()){
                return ahospital.get();
            }

            newHospital = Hospital.builder().abbreviation(hospital.abbreviation()).created(created).address(hospital.address()).head(hospital.head())
                    .manager(hospital.manager()).name(hospital.name()).phoneNumber(hospital.phonenumber()).build();
            log.info("Hospital " + newHospital.getHospital() + " created at " + created);
            return hospitalRepository.save(newHospital);
        }
        catch(PersistenceException pEx){
            log.error("Hospital cannot be created!");
            throw ServiceException.cannotCreateEntity(newHospital, pEx);
        }

    }

    public void  deleteHospital(Long id){
        Hospital hospital= null;
        Optional<Hospital> entity = hospitalRepository.findHospitalById(id);
        try{
            if(entity.isPresent()){
                hospital = entity.get();
                hospitalRepository.delete(hospital);
                log.info("Hospital " + hospital.getHospital() + " deleted " + LocalDateTime.now());
            }
        }catch(PersistenceException pe){
            log.error("Cannot be delete Hospital: " + hospital.getHospital());
            throw ServiceException.cannotDeleteEntity(hospital, pe);
        }

        hospitalRepository.delete(hospital);
    }
    public void deleteHospitals(){
        hospitalRepository.deleteAll();
        log.info("Hospitals deleted");
    }


    public Hospital replaceHospital(Long id, MutateHospitalCommand hospitalCommand){
        Optional<Hospital> entity = hospitalRepository.findHospitalById(id);
        Hospital hospital = null;
        LocalDateTime replace = temporalValueFactory.replaced_at();
        try {
            if (entity.isPresent()) {
                hospital = entity.get();
                hospital.setAbbreviation(hospitalCommand.getAbbreviation());
                hospital.setAddress(hospitalCommand.getAddress());
                hospital.setHead(hospitalCommand.getHead());
                hospital.setName(hospitalCommand.getName());
                hospital.setManager(hospitalCommand.getManager());
            }
            hospital.setUpdated(temporalValueFactory.replaced_at());
            log.info("Hospital " + hospital.getHospital() + "replace " + replace);
            return hospitalRepository.save(hospital);
        }catch(PersistenceException ex) {
            log.error("Cannot update Hospital: " + hospital.getHospital());
            throw ServiceException.cannotUpdateEntity(hospital,ex);
        }
    }
    public Hospital partiallyUpdate(Long id, MutateHospitalCommand hospitalCommand){
        Optional<Hospital> hospitals = hospitalRepository.findHospitalById(id);
        LocalDateTime replace = temporalValueFactory.replaced_at();

        if(hospitals.isPresent()){
            Hospital hospital = hospitals.get();
            if(hospitalCommand.getName() != null){
                hospital.setName(hospitalCommand.getName());
            }
            if(hospitalCommand.getAddress() != null){
                hospital.setAddress(hospitalCommand.getAddress());
            }
            if(hospitalCommand.getAbbreviation() != null){
                hospital.setManager(hospitalCommand.getManager());
            }
            if(hospitalCommand.getPhonenumber() != null){
                hospital.setPhoneNumber(hospitalCommand.getPhonenumber());
            }
            if(hospitalCommand.getHead() != null){
                hospital.setHead(hospitalCommand.getHead());
            }
            if(hospitalCommand.getManager() != null){
                hospital.setManager(hospitalCommand.getManager());
            }
            hospital.setUpdated(replace);
            log.info("Hospital " + hospital.getHospital() + " replaced " + replace);
            return hospitalRepository.save(hospital);
        }
        return hospitals.get();
    }

    public List<Hospital> getHospitals(){
        var hospitals = hospitalRepository.findAll();
        log.info("Found hospitals: ", hospitals.size());
        return hospitals;
    }

    public Optional<Hospital> getHospitalById(Long id){
        return hospitalRepository.findHospitalById(id);
    }
}
