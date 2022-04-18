package at.spengergasse.sj21224bhifaslantanprojectdoctor.services;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.*;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence.ManagerRepository;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.MutateManagerCommand;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.exceptions.ServiceException;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.ManagerDto;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.foundation.TemporalValueFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.PersistenceException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DataJpaTest
public class ManagerServiceTest {
    @Autowired
    private ManagerRepository managerRepository;
    private ManagerService managerService;
    private TemporalValueFactory temporalValueFactory;
    @BeforeEach
    void setup(){
        managerRepository = mock(ManagerRepository.class);
        temporalValueFactory = mock(TemporalValueFactory.class);
        managerService = new ManagerService(managerRepository,temporalValueFactory);
    }

    @Test
    void ensureCreatingManagerWithExceptionHandlingIsRight(){
        Name selTan = Name.builder().firstname("Sel").subname("Ilyas").lastname("Tan").build();
        Phonenumber selTanPhoneNumber = Phonenumber.builder().areacode("+43").countrycode("664").serialnumber("918329081").build();
        Title title = Title.builder().grade(Grade.DR).subjectGrade(SubjectGrade.PHD).build();
        Address address = Address.builder().street("Pernerstorfergasse 60/3/18").zipcode("1100").place("Wien").build();
        Gender gender = Gender.MALE;
        LocalDate date = LocalDate.of(1990,3,20);

        Ordination ordination= Ordination.builder().abbrevitation("ZA").name("Zahnarzt").address(address).build();

        Manager manager = Manager.builder()
                .name(selTan)
                .gender(gender)
                .phonenumber(selTanPhoneNumber)
                .title(title)
                .ordination(ordination)
                .birthDate(date)
                .build();

        ManagerDto managerDto = new ManagerDto(manager);





        Exception pEx = new PersistenceException("connection lost");
        when(managerRepository.findManagerByAddress(address)).thenReturn(Optional.empty());
        when(managerRepository.save(any())).thenThrow(pEx);

        var ex = assertThrows(ServiceException.class, ()-> managerService.createManagerFromDto(managerDto));

        assertThat(ex).hasMessageContaining("Manager")
        .hasMessageContaining("",selTan,gender,selTanPhoneNumber,title,date,address,ordination)
                .hasMessageContaining("database problem")
                .hasRootCause(pEx);
    }

    @Test
    void ensureGetAllWorksProperly(){
        Name selTan = Name.builder().firstname("Sel").subname("Ilyas").lastname("Tan").build();
        Phonenumber selTanPhoneNumber = Phonenumber.builder().areacode("+43").countrycode("664").serialnumber("918329081").build();
        Title title = Title.builder().grade(Grade.DR).subjectGrade(SubjectGrade.PHD).build();
        Address address = Address.builder().street("Pernerstorfergasse 60/3/18").zipcode("1100").place("Wien").build();
        Gender gender = Gender.MALE;
        LocalDate date = LocalDate.of(1990,3,20);

        Ordination ordination= Ordination.builder().abbrevitation("ZA").name("Zahnarzt").address(address).build();

        Manager manager = Manager.builder()
                .name(selTan)
                .gender(gender)
                .phonenumber(selTanPhoneNumber)
                .title(title)
                .ordination(ordination)
                .birthDate(date)
                .build();

        Manager manager1 = Manager.builder()
                .name(selTan)
                .gender(gender)
                .phonenumber(selTanPhoneNumber)
                .title(title)
                .ordination(ordination)
                .birthDate(date)
                .build();

        List<Manager> managers = Arrays.asList(manager,manager1);
        when(managerRepository.findAll()).thenReturn(managers);

        List<Manager> expected = managerService.getManager();

        assertEquals(expected,managers);
        verify(managerRepository).findAll();

    }

    @Test
    void ensureUpdateWorksProperly(){
        Name selTan = Name.builder().firstname("Sel").subname("Ilyas").lastname("Tan").build();
        Phonenumber selTanPhoneNumber = Phonenumber.builder().areacode("+43").countrycode("664").serialnumber("918329081").build();
        Title title = Title.builder().grade(Grade.DR).subjectGrade(SubjectGrade.PHD).build();
        Address address = Address.builder().street("Pernerstorfergasse 60/3/18").zipcode("1100").place("Wien").build();
        Gender gender = Gender.MALE;
        LocalDate date = LocalDate.of(1990,3,20);

        Ordination ordination= Ordination.builder().abbrevitation("ZA").name("Zahnarzt").address(address).build();

        Manager manager = Manager.builder()
                .name(selTan)
                .gender(gender)
                .phonenumber(selTanPhoneNumber)
                .title(title)
                .ordination(ordination)
                .birthDate(date)
                .build();

        MutateManagerCommand mutateManagerCommand = new MutateManagerCommand(selTan,gender,selTanPhoneNumber,title,address,date,ordination);
        when(managerRepository.findManagerById(manager.getId())).thenReturn(Optional.of(manager));
        when(managerRepository.save(manager)).thenReturn(manager);

        Manager replacedManager = managerService.replaceManager(manager.getId(),mutateManagerCommand);

        Optional<Manager> manager1 = managerRepository.findManagerById(manager.getId());

        assertThat(replacedManager).isEqualTo(manager1.get());

    }

    @Test
    void ensurepartiallyUpdateWorksProperly(){
        Name selTan = Name.builder().firstname("Sel").subname("Ilyas").lastname("Tan").build();
        Phonenumber selTanPhoneNumber = Phonenumber.builder().areacode("+43").countrycode("664").serialnumber("918329081").build();
        Title title = Title.builder().grade(Grade.DR).subjectGrade(SubjectGrade.PHD).build();
        Address address = Address.builder().street("Pernerstorfergasse 60/3/18").zipcode("1100").place("Wien").build();
        Gender gender = Gender.MALE;
        LocalDate date = LocalDate.of(1990,3,20);

        Ordination ordination= Ordination.builder().abbrevitation("ZA").name("Zahnarzt").address(address).build();

        Manager manager = Manager.builder()
                .name(selTan)
                .gender(gender)
                .phonenumber(selTanPhoneNumber)
                .title(title)
                .ordination(ordination)
                .birthDate(date)
                .build();

        MutateManagerCommand mutateManagerCommand = new MutateManagerCommand(selTan,gender,selTanPhoneNumber,title,address,date,ordination);
        when(managerRepository.findManagerById(manager.getId())).thenReturn(Optional.of(manager));
        when(managerRepository.save(manager)).thenReturn(manager);

        Manager replacedManager = managerService.partiallyUpdate(manager.getId(),mutateManagerCommand);

        Optional<Manager> manager1 = managerRepository.findManagerById(manager.getId());

        assertThat(replacedManager).isEqualTo(manager1.get());

    }

    @Test
    void ensureDeleteWorks(){
        Name selTan = Name.builder().firstname("Sel").subname("Ilyas").lastname("Tan").build();
        Phonenumber selTanPhoneNumber = Phonenumber.builder().areacode("+43").countrycode("664").serialnumber("918329081").build();
        Title title = Title.builder().grade(Grade.DR).subjectGrade(SubjectGrade.PHD).build();
        Address address = Address.builder().street("Pernerstorfergasse 60/3/18").zipcode("1100").place("Wien").build();
        Gender gender = Gender.MALE;
        LocalDate date = LocalDate.of(1990,3,20);

        Ordination ordination= Ordination.builder().abbrevitation("ZA").name("Zahnarzt").address(address).build();

        Manager manager = Manager.builder()
                .name(selTan)
                .gender(gender)
                .phonenumber(selTanPhoneNumber)
                .title(title)
                .ordination(ordination)
                .birthDate(date)
                .build();
        when(managerRepository.findManagerById(manager.getId())).thenReturn(Optional.of(manager));

        managerService.deleteManager(manager.getId());
        verify(managerRepository).findManagerById(manager.getId());

    }

}
