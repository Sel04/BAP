package at.spengergasse.sj21224bhifaslantanprojectdoctor.services;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Ordination;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence.OrdinationRepository;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.exceptions.ServiceException;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.MutateOrdinationCommand;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.OrdinationDto;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.foundation.TemporalValueFactory;
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
public class OrdinationService {

    private  final OrdinationRepository ordinationRepository;
    private final TemporalValueFactory temporalValueFactory;
    //private final Logger logger;
    public String CANNOT_BE_NULL = "cannot be null!";
    public String CANNOT_BE_EMPTY_OR_BLANK = "cannot be empty or blank!";
    public String CANNOT_BE_BLANK = "cannot be blank!";
    public String CANNOT_BE_NULL_EMPTY_OR_BLANK = "cannot be null, empty or blank!";





    public Optional<Ordination> findOrdination(String abbreviation){
        return  ordinationRepository.findOrdinationByAbbrevitation(abbreviation);
    }

    public List<Ordination> getOrdinations(){
       return ordinationRepository.findAll();
    }

    public void deleteOrdinations(){
         ordinationRepository.deleteAll();
         log.info("All Ordinations has been deleted " + "Anzahl = " +ordinationRepository.count());
    }

    public void deleteOrdination(long id){
         ordinationRepository.deleteById(id);
    }


    public Ordination saveOrdination(Ordination ordination){
        return ordinationRepository.save(ordination);
    }


    public Optional<Ordination> getOrdination( long id){
        return  ordinationRepository.findById(id);
    }



    public Ordination createOrdination(MutateOrdinationCommand command){
        Ordination newOrdination=null;
        LocalDateTime creationTs = temporalValueFactory.created_at();

       try {
            var ordination = ordinationRepository.findOrdinationByAbbrevitation(command.getAbbreviation());
            if (ordination.isPresent()) {
                log.warn("Ordination {}" + ordination.get().toString() + " already exists");
                throw new IllegalArgumentException("Ordination " + ordination.get().toString() + " already exists");
            }

            else {

                newOrdination = Ordination.builder()
                        .abbrevitation(command.getAbbreviation())
                        .name(command.getName())
                        .address(command.getAddress())
                        .created_at(creationTs)
                        .build();
                log.info("Ordination {}" + newOrdination.toString() + " created_at :" +creationTs);
                return ordinationRepository.save(newOrdination);
            }

        }catch(PersistenceException pe){
            throw ServiceException.cannotCreateEntity(newOrdination,pe);
        }
    }

    @Transactional(readOnly = false)
    public Ordination createOrdinationFromDto( OrdinationDto command){
         LocalDateTime creationTs = temporalValueFactory.created_at();
         Ordination newOrdination = null;

         try {
             var ordination = ordinationRepository.findOrdinationByAbbrevitation(command.abbreviation());
             if (ordination.isPresent()) {
                 ordination.get();
           log.warn("Ordination {}" + ordination.get().toString() + " already exists");
             }

              newOrdination = Ordination.builder()
                      .abbrevitation(command.abbreviation())
                      .name(command.name())
                      .address(command.address())
                      .created_at(creationTs)
                      .build();
         log.info("Ordination {}" + newOrdination.toString() + " created_at :" +creationTs);
             return ordinationRepository.save(newOrdination);

         }catch(PersistenceException pe){
             throw ServiceException.cannotCreateEntity(newOrdination,pe);
         }
    }

    public Ordination replaceOrdination(Long id,  MutateOrdinationCommand command){
            Ordination ordination=null;
            LocalDateTime replaced_at = temporalValueFactory.replaced_at();
            Optional<Ordination> entity = ordinationRepository.findById(id);
        try {
            if (entity.isPresent()) {
                ordination = entity.get();
                ordination.setAbbrevitation(command.getAbbreviation());
                ordination.setName(command.getName());
                ordination.setAddress(command.getAddress());
                ordination.setReplaced_at(replaced_at);

                log.info("Ordination {}" + ordination.toString() + " replaced at :" +replaced_at);
            }

            return ordinationRepository.save(ordination);


        }catch(PersistenceException pEx){
            log.error("Cannot update Ordination = " +ordination.toString());
            throw ServiceException.cannotUpdateEntity(ordination,pEx);
        }
    }

    public Ordination partiallyUpdateOrdination(Long id,  MutateOrdinationCommand command){
        Optional<Ordination> entity = ordinationRepository.findById(id);

        if(entity.isPresent()){
            Ordination ordination = entity.get();
            if(command.getAbbreviation()!=null){
                ordination.setAbbrevitation(command.getAbbreviation());
                log.info("Changed Abbreviation from Ordination" + ordination.toString());
               }
            if(command.getName()!=null){
                ordination.setName(command.getName());
                log.info("Changed Name from Ordination" + ordination.toString());
            }
            if(command.getAddress()!=null){
                ordination.setAddress(command.getAddress());
                log.info("Changed Address from Ordination" + ordination.toString());
            }
            return ordinationRepository.save(ordination);
        }
        else {
            log.error("Cannot find ordination with id" + " : " + id);
            return entity.get();
        }
    }

    public Ordination deleteOrdination(Long  id){
        LocalDateTime deleted_at = temporalValueFactory.deleted_at();
        Ordination ordination=null;
        Optional<Ordination> entity = ordinationRepository.findById(id);
        try{
            if(entity.isPresent()){
                ordination=entity.get();
                ordinationRepository.delete(ordination);
                return ordination;
            }
            log.info("Ordination {} " +ordination.toString() + " deleted :" +deleted_at);
        }catch (PersistenceException pe){
            throw  ServiceException.cannotDeleteEntity(ordination,pe);
        }


        return ordination;
    }

}
