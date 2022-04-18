package at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Address;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Ordination;

import java.time.LocalDateTime;


public record OrdinationDto(Long id, String abbreviation, Address address, String name, LocalDateTime created, LocalDateTime replaced){
    public OrdinationDto(Ordination ordination){
        this(ordination.getId(),ordination.getAbbrevitation(),ordination.getAddress(),ordination.getName(),ordination.getCreated_at(),
                ordination.getReplaced_at());
    }
}
