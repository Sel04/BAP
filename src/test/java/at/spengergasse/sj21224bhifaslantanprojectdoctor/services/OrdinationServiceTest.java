package at.spengergasse.sj21224bhifaslantanprojectdoctor.services;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.*;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence.OrdinationRepository;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.exceptions.ServiceException;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.MutateOrdinationCommand;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.OrdinationDto;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.foundation.TemporalValueFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.PersistenceException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DataJpaTest
public class OrdinationServiceTest {

    @Mock
    private OrdinationRepository ordinationRepository;
    private OrdinationService ordinationService;
    private TemporalValueFactory temporalValueFactory;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        temporalValueFactory = new TemporalValueFactory();
        ordinationService = new OrdinationService(ordinationRepository, temporalValueFactory);

    }

    @Test
    void ensureAllOrdinationsWorks() {
        LocalDateTime created = temporalValueFactory.created_at();
        Address address = Address.builder().place("Wien").street("Rennstrasse 19").zipcode("1220").build();
        Address address2 = Address.builder().place("Wien").street("Murrstrasse 1").zipcode("1100").build();
        String abbreviation = "ZE";
        String abbreviation2 = "AE";
        String name = "Zahnarzt";
        String name2 = "Arztpraxis";

        Ordination ordination = Ordination.builder()
                .abbrevitation(abbreviation)
                .name(name)
                .address(address)
                .created_at(created)
                .build();

        Ordination ordination2 = Ordination.builder()
                .abbrevitation(abbreviation2)
                .name(name2)
                .address(address2)
                .created_at(created)
                .build();

        List<Ordination> ordinations = Arrays.asList(ordination, ordination2);

        when(ordinationRepository.findAll()).thenReturn(ordinations);

        // when
        List<Ordination> expected = ordinationService.getOrdinations();

        // then
        assertEquals(expected, ordinations);
        verify(ordinationRepository).findAll();
    }

    @Test
    void ensureDatabaseErrorsWorksRight() {
        MutateOrdinationCommand command = new MutateOrdinationCommand("ZA", null, null);
        Exception pEx = new PersistenceException("connection lost");
        when(ordinationRepository.save(any())).thenThrow(pEx);

        // when
        var ex = assertThrows(ServiceException.class, () -> ordinationService.createOrdination(command));

        // then
        assertThat(ex)
                .hasMessageContaining("Ordination")
                .hasMessageContaining("database problems")
                .hasRootCause(pEx);
    }

    @Test
    void ensureCreatingOrdinationWithExceptionHandlingIsRight() {
        LocalDateTime created = temporalValueFactory.created_at();
        Address address = Address.builder().place("Wien").street("Rennstrasse 19").zipcode("1220").build();
        String abbreviation = "ZE";
        String name = "Zahnarzt";

        Ordination ordination = Ordination.builder()
                .abbrevitation(abbreviation)
                .name(name)
                .address(address)
                .created_at(created)
                .build();

        OrdinationDto ordinationDto = new OrdinationDto(ordination);

        Exception pEx = new PersistenceException("connection lost");
        when(ordinationRepository.findOrdinationByAbbrevitation(abbreviation)).thenReturn(Optional.empty());
        when(ordinationRepository.save(any())).thenThrow(pEx);

        var ex = assertThrows(ServiceException.class, () -> ordinationService.createOrdinationFromDto(ordinationDto));

        assertThat(ex).hasMessageContaining("Ordination")
                .hasMessageContaining("Ordination", ordinationDto.abbreviation(), ordinationDto.address(),
                        ordinationDto.name(), ordinationDto.created())
                .hasMessageContaining("database problem")
                .hasRootCause(pEx);
    }

    @Test
    void ensureCreateWithExistingOrdinationThrowsException() {
        LocalDateTime created = temporalValueFactory.created_at();
        Address address = Address.builder().place("Wien").street("Rennstrasse 19").zipcode("1220").build();
        String abbreviation = "ZE";
        String name = "Zahnarzt";

        Ordination ordination = Ordination.builder()
                .abbrevitation(abbreviation)
                .name(name)
                .address(address)
                .created_at(created)
                .build();

        MutateOrdinationCommand command = new MutateOrdinationCommand(abbreviation, address, name);
        when(ordinationRepository.findOrdinationByAbbrevitation(abbreviation)).thenReturn(Optional.of(ordination));

        // when
        var ex = assertThrows(IllegalArgumentException.class, () -> ordinationService.createOrdination(command));

        // then
        assertThat(ex)
                .hasMessageContaining("already exists");
    }

    @Test
    void ensurePartiallyUpdateIsWorking() {
        MutateOrdinationCommand mutateMessage = new MutateOrdinationCommand("test", null, null);
        LocalDateTime created = temporalValueFactory.created_at();
        Address address = Address.builder().place("Wien").street("Rennstrasse 19").zipcode("1220").build();
        String abbreviation = "ZE";
        String name = "Zahnarzt";

        Ordination ordination = Ordination.builder()
                .abbrevitation(abbreviation)
                .name(name)
                .address(address)
                .created_at(created)
                .build();
        when(ordinationRepository.findById(ordination.getId())).thenReturn(Optional.of(ordination));
        when(ordinationRepository.save(ordination)).thenReturn(ordination);

        // when
        Ordination replaceOrdination = ordinationService.partiallyUpdateOrdination(ordination.getId(), mutateMessage);

        // then
        assertThat(ordination).extracting(Ordination::getAbbrevitation).isEqualTo(ordination.getAbbrevitation());
        assertThat(ordination).extracting(Ordination::getName).isEqualTo(ordination.getName());
        assertThat(ordination).extracting(Ordination::getAddress).isEqualTo(ordination.getAddress());
        assertThat(ordination).extracting(Ordination::getId).isEqualTo(ordination.getId());
    }

    @Test
    void ensureDeletingOrdinationWithExceptionHandlingisRight() {
        LocalDateTime created = temporalValueFactory.created_at();
        Address address = Address.builder().place("Wien").street("Rennstrasse 19").zipcode("1220").build();
        String abbreviation = "ZE";
        String name = "Zahnarzt";

        Ordination ordination = Ordination.builder()
                .abbrevitation(abbreviation)
                .name(name)
                .address(address)
                .created_at(created)
                .build();

        when(ordinationRepository.findById(1L)).thenReturn(Optional.of(ordination));

        ordinationService.deleteOrdination(1L);

        verify(ordinationRepository).deleteById(1L);

    }

    @Test
    void ensureReplacingOrdinationWithExceptionHandlingisRight() {
        LocalDateTime created = temporalValueFactory.created_at();
        Address address = Address.builder().place("Wien").street("Rennstrasse 19").zipcode("1220").build();
        String abbreviation = "ZE";
        String name = "Zahnarzt";

        Ordination ordination = Ordination.builder()
                .abbrevitation(abbreviation)
                .name(name)
                .address(address)
                .created_at(created)
                .build();

        MutateOrdinationCommand command = new MutateOrdinationCommand("AE", address, "Arztpraxis");

        when(ordinationRepository.findById(1L)).thenReturn(Optional.of(ordination));

        ordinationService.replaceOrdination(1L, command);

        verify(ordinationRepository).save(ordination);

    }

}
