package at.spengergasse.sj21224bhifaslantanprojectdoctor.services;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.*;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence.OrdinationRepository;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence.SecretaryRepository;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.exceptions.ServiceException;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.MutateSecretaryCommand;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.SecretaryDto;
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

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = false)
public class SecretaryService {

    private final SecretaryRepository secretaryRepository;
    private  OrdinationRepository ordinationRepository;
    private final TemporalValueFactory temporalValueFactory;
   // private final Logger logger;

    public String CANNOT_BE_NULL = "cannot be null!";
    public String CANNOT_BE_EMPTY_OR_BLANK = "cannot be empty or blank!";
    public String CANNOT_BE_BLANK = "cannot be blank!";
    public String CANNOT_BE_NULL_EMPTY_OR_BLANK = "cannot be null, empty or blank!";
    public String CANNOT_BE_AFTER_CURRENT_DATE = "cannot be after current date";

    public Optional<Secretary> findSecretary(LocalDate birthDate){
        return secretaryRepository.findSecretaryByBirthDate(birthDate);
    }



    public void deleteSecretary(){
        secretaryRepository.deleteAll();
    }

    public Secretary createSecretary( MutateSecretaryCommand command){
        LocalDateTime created_at= temporalValueFactory.created_at();
        Secretary newSecretary =null;
        Ordination ordination=null;

        try {
            var secretary = secretaryRepository.findSecretaryByBirthDate(command.getBirthDate());
            if (secretary.isPresent()) {
                log.warn("Secretary " +secretary.get().toString() +  " failed");
                throw new IllegalArgumentException("Deleting " +secretary.get().toString() +  " failed");
            }
            ordination=Ordination.builder()
                    .abbrevitation(command.getOrdination().getAbbrevitation())
                    .name(command.getOrdination().getName())
                    .address(command.getOrdination().getAddress())
                    .created_at(created_at)
                    .build();

            log.debug("Ordination has been saved : " + ordination.toString());

            newSecretary = Secretary.builder()
                    .name(command.getName())
                    .gender(command.getGender())
                    .phonenumber(command.getPhonenumber())
                    .title(command.getTitle())
                    .address(command.getAddress())
                    .birthDate(command.getBirthDate())
                    .created_at(created_at)
                    .ordination(ordination)
                    .build();
            log.info("Secretary  " +newSecretary.toString() + "created at" +created_at);
            return secretaryRepository.save(newSecretary);

        }catch(PersistenceException pe){
            throw ServiceException.cannotCreateEntity(newSecretary,pe);
        }
    }

    @Transactional(readOnly = false)
   public Secretary creatSecretaryFromDto(SecretaryDto command){
        LocalDateTime created_at= temporalValueFactory.created_at();
        Secretary newSecretary =null;
        try {
            var secretary = secretaryRepository.findSecretaryByBirthDate(command.birthDate());
            if (secretary.isPresent()) {
                secretary.get();
            log.warn("Deleting " +secretary.get().toString() +  " failed");
            }

            newSecretary = Secretary.builder()
                    .name(command.name())
                    .gender(command.gender())
                    .phonenumber(command.phonenumber())
                    .title(command.title())
                    .address(command.address())
                    .birthDate(command.birthDate())
                    .ordination(Ordination.builder()
                            .abbrevitation(command.ordination().abbreviation())
                            .name(command.ordination().name())
                            .address(command.ordination().address())
                            .created_at(created_at)
                            .build())
                    .created_at(created_at)
                    .build();
           log.info("Secretary  " +newSecretary.toString() + " created at" +created_at);
            return secretaryRepository.save(newSecretary);

        }catch(PersistenceException pe){
            throw ServiceException.cannotCreateEntity(newSecretary,pe);
        }
   }

   public Secretary replaceSecretary(Long id,  MutateSecretaryCommand command){
       LocalDateTime replaced_at = temporalValueFactory.replaced_at();
        Secretary secretary = null;
        Optional<Secretary> entity = secretaryRepository.findById(id);
        try{
            if(entity.isPresent()){
                secretary=entity.get();
                secretary.setName(command.getName());
                secretary.setGender(command.getGender());
                secretary.setPhonenumber(command.getPhonenumber());
                secretary.setTitle(command.getTitle());
                secretary.setAddress(command.getAddress());
                secretary.setBirthDate(command.getBirthDate());
            }
           log.info("Secretary  " +secretary.toString() + " replaced at" +replaced_at);
            return secretaryRepository.save(secretary);


        }catch (PersistenceException peX){
            throw ServiceException.cannotUpdateEntity(secretary,peX);
        }
   }

    public Secretary partiallyUpdateSecretary(Long id,  MutateSecretaryCommand command){
        Optional<Secretary> entity = secretaryRepository.findById(id);

        if(entity.isPresent()){
            Secretary secretary = entity.get();
            if(command.getName()!=null) {
                secretary.setName(command.getName());
                log.info("Changed Name from Secretary " + id);
            }
            if(command.getGender()!=null) {
                secretary.setGender(command.getGender());
                log.info("Changed Gender from Secretary " + id);
            }
            if(command.getPhonenumber()!=null) {
                secretary.setPhonenumber(command.getPhonenumber());
                log.info("Changed Phonenumber from Secretary " + id);
            }
            if(command.getTitle()!=null) {
                secretary.setTitle(command.getTitle());
                log.info("Changed Title from Secretary " + id);
            }
            if(command.getAddress()!=null) {
                secretary.setAddress(command.getAddress());
                log.info("Changed Address from Secretary " + id);
            }
            if(command.getBirthDate()!=null) {
                secretary.setBirthDate(command.getBirthDate());
                log.info("Changed Birthdate from Secretary " + id);
            }
            if(command.getOrdination().getAbbrevitation()!=null) {
                secretary.setOrdination(command.getOrdination());
                log.info("Changed Ordination  Abbreviation from Secretary " + id);
            }
            if(command.getOrdination().getName()!=null) {
                secretary.setOrdination(command.getOrdination());
                log.info("Changed Ordination  Name from Secretary " + id);
            }
            if(command.getOrdination().getAddress().getStreet()!=null) {
                secretary.setOrdination(command.getOrdination());
                log.info("Changed Ordination  Adress-Street from Secretary " + id);
            }
            if(command.getOrdination().getAddress().getPlace()!=null) {
                secretary.setOrdination(command.getOrdination());
                log.info("Changed Ordination  Place from Secretary " + id);
            }
            if(command.getOrdination().getAddress().getZipcode()!=null) {
                secretary.setOrdination(command.getOrdination());
                log.info("Changed Ordination  ZipCode from Secretary " + id);
            }
            return secretaryRepository.save(secretary);
        }
        else{
            log.error("Cannot find Secretary with id" + id);
            throw new IllegalStateException("Cannot find Secretary with id" + id);
        }

    }

    public void deleteSecretary(Long id){
        LocalDateTime deleted_at = temporalValueFactory.deleted_at();
        Secretary secretary=null;
        Optional<Secretary> entity = secretaryRepository.findById(id);
        try {
            if(entity.isPresent()) {
                secretary=entity.get();
                secretaryRepository.delete(secretary);
            }
           log.info("Secretary " +secretary.toString() + " deleted at" +deleted_at);
        }catch(PersistenceException pe){
            throw ServiceException.cannotDeleteEntity(secretary,pe);
        }
    }

    public void deleteSecretaries(){
        secretaryRepository.deleteAll();
    }

    public List<Secretary> getSecretaries() {
        return secretaryRepository.findAll();
    }

    public Optional<Secretary> getSecretary(Long id)
    {
        return secretaryRepository.findById(id);
    }

    public Optional<Secretary> getSecretaryByBirthDate(LocalDate birthDate){
        return secretaryRepository.findSecretaryByBirthDate(birthDate);
    }
}
