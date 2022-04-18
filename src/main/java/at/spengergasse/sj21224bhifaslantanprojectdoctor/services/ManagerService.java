package at.spengergasse.sj21224bhifaslantanprojectdoctor.services;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.*;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence.ManagerRepository;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.exceptions.ServiceException;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.ManagerDto;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.MutateManagerCommand;
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
public class ManagerService {

    private final ManagerRepository managerRepository;
    private final TemporalValueFactory temporalValueFactory;

    private String CANNOT_BE_NULL = "cannot be null!";
    private String CANNOT_BE_EMPTY_OR_BLANK = "cannot be empty or blank!";
    private String CANNOT_BE_BLANK = "cannot be blank!";
    private String CANNOT_BE_NULL_EMPTY_OR_BLANK = "cannot be null, empty or blank!";

    public Manager createManager(MutateManagerCommand command){
        Manager newManager = null;
        LocalDateTime created = temporalValueFactory.created_at();


        /*if(command.getName() == null){
            throw new IllegalArgumentException("Name " + CANNOT_BE_NULL);
        }
        else if(command.getName().getFirstname().isBlank() || command.getName().getFirstname().isEmpty()){
            throw new IllegalArgumentException("Firstname " + CANNOT_BE_EMPTY_OR_BLANK);
        }
        else if(command.getName().getLastname().isBlank() || command.getName().getLastname().isEmpty()){
            throw new IllegalArgumentException("Lastname " + CANNOT_BE_EMPTY_OR_BLANK);
        }
        else if(command.getName().getSubname().isBlank() ){
            throw new IllegalArgumentException("Subname " + CANNOT_BE_BLANK);
        }
        else if(command.getGender() == null){
            throw new IllegalArgumentException("Gender " + CANNOT_BE_NULL);
        }
        else if(command.getPhonenumber() == null){
            throw new IllegalArgumentException("Phonenumber " + CANNOT_BE_NULL);
        }
        // Phonenumber String?

        else if(command.getAddress() == null){
            throw new IllegalArgumentException("Address " + CANNOT_BE_NULL);
        }
        else if(command.getAddress().getPlace().isBlank() || command.getAddress().getPlace().isEmpty()){
            throw new IllegalArgumentException("Place of address" + CANNOT_BE_EMPTY_OR_BLANK);
        }
        else if(command.getAddress().getStreet().isBlank() || command.getAddress().getStreet().isEmpty()) {
            throw new IllegalArgumentException("Street of address " + CANNOT_BE_EMPTY_OR_BLANK);
        }
        else if(command.getTitle() == null){
            throw new IllegalArgumentException("Title " + CANNOT_BE_NULL);
        }
        else if(command.getBirthDate() == null){
            throw new IllegalArgumentException("Birthdate " + CANNOT_BE_NULL);
        }
        else if (command.getBirthDate().isAfter(LocalDate.now())){
            throw new IllegalArgumentException("Birthdate " + CANNOT_BE_EMPTY_OR_BLANK);
        }
         */
        try{
           var manager = managerRepository.findManagerByAddress(command.getAddress());
           if(manager.isPresent()){
               return manager.get();
           }

            newManager = Manager.builder()
                    .name(command.getName())
                    .gender(command.getGender())
                    .phonenumber(command.getPhonenumber())
                    .title(command.getTitle())
                    .created(created)
                    .ordination(command.getOrdination())
                    .birthDate(command.getBirthDate())
                    .build();
           log.info("Manager " + newManager.getManager() + " created_at: " + created);
           return managerRepository.save(newManager);
        }
        catch(PersistenceException pEx){
            log.error("Cannot create Manager " + newManager.getManager());
            throw ServiceException.cannotCreateEntity(newManager, pEx);
        }
    }

    @Transactional
    public Manager createManagerFromDto(ManagerDto command){
        Manager newManager = null;
        LocalDateTime created = temporalValueFactory.created_at();
        /*if(command.name() == null){
            throw new IllegalArgumentException("Name " + CANNOT_BE_NULL);
        }
        else if(command.name().getFirstname().isBlank() || command.name().getFirstname().isEmpty()){
            throw new IllegalArgumentException("Firstname " + CANNOT_BE_EMPTY_OR_BLANK);
        }
        else if(command.name().getLastname().isBlank() || command.name().getLastname().isEmpty()){
            throw new IllegalArgumentException("Lastname " + CANNOT_BE_EMPTY_OR_BLANK);
        }
        else if(command.name().getSubname().isBlank() ){
            throw new IllegalArgumentException("Subname " + CANNOT_BE_BLANK);
        }
        else if(command.gender() == null){
            throw new IllegalArgumentException("Gender " + CANNOT_BE_NULL);
        }
        else if(command.phonenumber() == null){
            throw new IllegalArgumentException("Phonenumber " + CANNOT_BE_NULL);
        }
        // Phonenumber String?

        else if(command.address() == null){
            throw new IllegalArgumentException("Address " + CANNOT_BE_NULL);
        }
        else if(command.address().getPlace().isBlank() || command.address().getPlace().isEmpty()){
            throw new IllegalArgumentException("Place of address" + CANNOT_BE_EMPTY_OR_BLANK);
        }
        else if(command.address().getStreet().isBlank() || command.address().getStreet().isEmpty()) {
            throw new IllegalArgumentException("Street of address " + CANNOT_BE_EMPTY_OR_BLANK);
        }
        else if(command.title() == null){
            throw new IllegalArgumentException("Title " + CANNOT_BE_NULL);
        }
        else if(command.date() == null){
            throw new IllegalArgumentException("Birthdate " + CANNOT_BE_NULL);
        }
        else if (command.date().isAfter(LocalDate.now())){
            throw new IllegalArgumentException("Birthdate " + CANNOT_BE_EMPTY_OR_BLANK);
        }
         */
        try{
            var manager = managerRepository.findManagerByAddress(command.address());
            if(manager.isPresent()){
                return manager.get();
            }

            newManager = Manager.builder()
                    .name(command.name())
                    .gender(command.gender())
                    .phonenumber(command.phonenumber())
                    .title(command.title())
                    .created(created)
                    .ordination(command.ordination())
                    .birthDate(command.date())
                    .build();
            log.info("Manager " + newManager.getManager() + " created_at: " + created);
            return managerRepository.save(newManager);
        }
        catch(PersistenceException pEx){
            log.error("Cannot create Manager " + newManager.getManager());
            throw ServiceException.cannotCreateEntity(newManager, pEx);
        }
    }

    public Manager replaceManager(Long id, MutateManagerCommand mutateManagerCommand){
        Optional<Manager> entity = managerRepository.findManagerById(id);
        Manager manager = null;
        LocalDateTime replaced = temporalValueFactory.replaced_at();
        try {
            if (entity.isPresent()) {
                manager = entity.get();
                manager.setName(mutateManagerCommand.getName());
                manager.setBirthDate(mutateManagerCommand.getBirthDate());
                manager.setAddress(mutateManagerCommand.getAddress());
                manager.setGender(mutateManagerCommand.getGender());
                manager.setPhonenumber(mutateManagerCommand.getPhonenumber());
                manager.setTitle(mutateManagerCommand.getTitle());
            }
            manager.setUpdated(replaced);
            log.info("Manager " + manager.getManager() + " replaced_at: " + replaced);
            return managerRepository.save(manager);
        }catch(PersistenceException ex) {
            log.error("Cannot replace Manager: " + manager.getManager());
            throw ServiceException.cannotUpdateEntity(manager,ex);
        }
    }

    public Manager partiallyUpdate(Long id,MutateManagerCommand mutateManagerCommand){
        Optional<Manager> managers = managerRepository.findManagerById(id);
        Manager manager = managers.get();
        LocalDateTime replaced = temporalValueFactory.replaced_at();
        if(managers.isPresent()){

            if(mutateManagerCommand.getName() != null){
                manager.setName(mutateManagerCommand.getName());
            }
            if(mutateManagerCommand.getAddress() != null){
                manager.setAddress(mutateManagerCommand.getAddress());
            }
            if(mutateManagerCommand.getGender() != null){
                manager.setGender(mutateManagerCommand.getGender());
            }
            if(mutateManagerCommand.getBirthDate() != null){
                manager.setBirthDate(mutateManagerCommand.getBirthDate());
            }
            if(mutateManagerCommand.getTitle() != null){
                manager.setTitle(mutateManagerCommand.getTitle());
            }
            if(mutateManagerCommand.getPhonenumber() != null){
                manager.setPhonenumber(mutateManagerCommand.getPhonenumber());
            }
            manager.setUpdated(replaced);
            log.info("Manager " + manager.getManager() + " updated: " + replaced);
            return managerRepository.save(manager);
        }
        return managers.get();
    }

    public void deleteManagers(){
        managerRepository.deleteAll();
        log.info("Deleted all Managers");
    }

    public void deleteManager(Long id){
        Manager manager = null;
        Optional<Manager> entity = managerRepository.findManagerById(id);
        try{
            if(entity.isPresent()){
                manager = entity.get();
                managerRepository.delete(manager);
                log.info("Deleted Manager: " + manager.getManager());
            }
        }catch(PersistenceException ex){
            log.error("Cannot delete Manager" + manager.getManager());
            throw ServiceException.cannotDeleteEntity(manager,ex);
        }
    }

    public List<Manager> getManager(){
        var managers = managerRepository.findAll();
        log.info("Found Manager: " + managers.size());
        return managers;
    }
    public Optional<Manager> getManagerById(Long id){
        return managerRepository.findManagerById(id);
    }
}
