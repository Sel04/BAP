package at.spengergasse.sj21224bhifaslantanprojectdoctor.services;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.*;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence.OrdinationRepository;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence.SecretaryRepository;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.MutateOrdinationCommand;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.MutatePatientCommand;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.MutateSecretaryCommand;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.exceptions.ServiceException;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.SecretaryDto;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.foundation.TemporalValueFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.PersistenceException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DataJpaTest
public class SecretaryServiceTest {

    @Mock
    private SecretaryRepository secretaryRepository;
    private SecretaryService secretaryService;
    private TemporalValueFactory temporalValueFactory;


    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
        temporalValueFactory = new TemporalValueFactory();
        secretaryService=new SecretaryService(secretaryRepository,temporalValueFactory);
    }

    @Test
    void ensureAllSecretariesWorks(){
        LocalDateTime created = temporalValueFactory.created_at();
        Name name = Name.builder().firstname("Hans").lastname("Berger").subname("Burger").build();
        Name name2 = Name.builder().firstname("Karl").lastname("Jimenez").subname("Gabriel").build();
        Gender gender =Gender.MALE;
        Gender gender2 = Gender.MALE;
        Phonenumber phonenumber=Phonenumber.builder().serialnumber("3189781").areacode("667").countrycode("+43").build();
        Phonenumber phonenumber2=Phonenumber.builder().serialnumber("1178197").areacode("699").countrycode("+43").build();
        Grade grade =Grade.DR;
        Grade grade2 =Grade.DIPL;
        SubjectGrade subjectGrade= SubjectGrade.NAT_TECHN;
        SubjectGrade subjectGrade2= SubjectGrade.PHIL;
        Title title=Title.builder().grade(grade).subjectGrade(subjectGrade).build();
        Title title2=Title.builder().grade(grade2).subjectGrade(subjectGrade2).build();
        Address address =Address.builder().place("Wien").street("Markomannenstraße 12").zipcode("1220").build();
        Address address2 =Address.builder().place("Wien").street("Klauengasse 33").zipcode("1120").build();
        LocalDate birthDate =LocalDate.of(1998, Month.DECEMBER,18);
        LocalDate birthDate2 =LocalDate.of(2000, Month.AUGUST,8);

        String abbreviation ="FA";
        String abbreviation2 ="ZA";
        String nameOrd = "Facharztpraxis";
        String nameOrd2 = "Zahnarztpraxis";
        Address addressOrd1 = Address.builder().place("Wien").street("Spengergasse 12").zipcode("1220").build();
        Address addressOrd2 = Address.builder().place("Wien").street("Spengergasse 12").zipcode("1220").build();

        Secretary secretary = Secretary.builder()
                .name(name)
                .gender(gender)
                .phonenumber(phonenumber)
                .title(title)
                .address(address)
                .birthDate(birthDate)
                .ordination(Ordination.builder()
                        .abbrevitation(abbreviation)
                        .name(nameOrd)
                        .address(addressOrd1)
                        .build())
                .created_at(created)
                .build();

        Secretary secretary2 = Secretary.builder()
                .name(name2)
                .gender(gender2)
                .phonenumber(phonenumber2)
                .title(title2)
                .birthDate(birthDate2)
                .ordination(Ordination.builder()
                        .abbrevitation(abbreviation2)
                        .name(nameOrd2)
                        .address(addressOrd2)
                        .build())
                .created_at(created)
                .build();

        List<Secretary> secretaryList = List.of(secretary,secretary2);

        when(secretaryRepository.findAll()).thenReturn(secretaryList);

        //when
        List<Secretary> expected = secretaryService.getSecretaries();

        //then
        assertEquals(expected,secretaryList);
        verify(secretaryRepository).findAll();
    }

    @Test
    void ensureDatabaseErrorWorksRight(){
        Ordination ordination = Ordination.builder()
                .abbrevitation(null)
                .address(null)
                .name(null)
                .build();

        MutateSecretaryCommand mutateSecretaryCommand = new MutateSecretaryCommand(null,null,null,null,null,null,ordination);
        Exception pEx = new PersistenceException("connection lost");
        when(secretaryRepository.save(any())).thenThrow(pEx);

        //when
        var ex = assertThrows(ServiceException.class,()->secretaryService.createSecretary(mutateSecretaryCommand));

        //then
        assertThat(ex)
                .hasMessageContaining("Secretary")
                .hasMessageContaining("database problems")
                .hasRootCause(pEx);
    }

    @Test
    void ensureCreatingSecretaryWithExceptionHandlingIsRight(){
        LocalDateTime created = temporalValueFactory.created_at();
        Name name = Name.builder().firstname("Hans").lastname("Berger").subname("Burger").build();
        Gender gender =Gender.MALE;
        Phonenumber phonenumber=Phonenumber.builder().serialnumber("3189781").areacode("667").countrycode("+43").build();
        Grade grade =Grade.DR;
        SubjectGrade subjectGrade= SubjectGrade.NAT_TECHN;
        Title title=Title.builder().grade(grade).subjectGrade(subjectGrade).build();
        Address address =Address.builder().place("Wien").street("Markomannenstraße 12").zipcode("1220").build();
        LocalDate birthDate =LocalDate.of(1998, Month.DECEMBER,18);

        String abbreviation ="FA";
        String nameOrd = "Facharztpraxis";
        Address addressOrd1 = Address.builder().place("Wien").street("Spengergasse 12").zipcode("1220").build();

        Secretary secretary = Secretary.builder()
                .name(name)
                .gender(gender)
                .phonenumber(phonenumber)
                .title(title)
                .address(address)
                .birthDate(birthDate)
                .ordination(Ordination.builder()
                        .abbrevitation(abbreviation)
                        .name(nameOrd)
                        .address(addressOrd1)
                        .build())
                .created_at(created)
                .build();

        SecretaryDto secretaryDto=new SecretaryDto(secretary);

        Exception pEX = new PersistenceException("connection lost");
        when(secretaryRepository.findSecretaryByBirthDate(birthDate)).thenReturn(Optional.empty());
        when(secretaryRepository.save(any())).thenThrow(pEX);

        var ex= assertThrows(ServiceException.class, () -> secretaryService.creatSecretaryFromDto(secretaryDto));

        assertThat(ex).hasMessageContaining("Secretary")
                .hasMessageContaining("Secretary",secretaryDto.name(),secretaryDto.gender(),secretaryDto.phonenumber(),
                        secretaryDto.title(),secretaryDto.address(),secretaryDto.birthDate(),secretaryDto.created_at())
                .hasMessageContaining("database problem")
                .hasRootCause(pEX);
    }

    @Test
    void ensureCreateWithExistingSecretaryIsRight() {
        LocalDateTime created = temporalValueFactory.created_at();
        Name name = Name.builder().firstname("Hans").lastname("Berger").subname("Burger").build();
        Gender gender =Gender.MALE;
        Phonenumber phonenumber=Phonenumber.builder().serialnumber("3189781").areacode("667").countrycode("+43").build();
        Grade grade =Grade.DR;
        SubjectGrade subjectGrade= SubjectGrade.NAT_TECHN;
        Title title=Title.builder().grade(grade).subjectGrade(subjectGrade).build();
        Address address =Address.builder().place("Wien").street("Markomannenstraße 12").zipcode("1220").build();
        LocalDate birthDate =LocalDate.of(1998, Month.DECEMBER,18);

        String abbreviation ="FA";
        String nameOrd = "Facharztpraxis";
        Address addressOrd1 = Address.builder().place("Wien").street("Spengergasse 12").zipcode("1220").build();

        Ordination ordination= Ordination.builder()
                .abbrevitation(abbreviation)
                .name(nameOrd)
                .address(addressOrd1)
                .build();

        Secretary secretary = Secretary.builder()
                .name(name)
                .gender(gender)
                .phonenumber(phonenumber)
                .title(title)
                .address(address)
                .birthDate(birthDate)
                .ordination(ordination)
                .created_at(created)
                .build();

        MutateSecretaryCommand command = new MutateSecretaryCommand(
                name,gender,phonenumber,title,address,birthDate,ordination);

        when(secretaryRepository.findSecretaryByBirthDate(birthDate)).thenReturn(Optional.of(secretary));

        //when
        var ex = assertThrows(IllegalArgumentException.class, () -> secretaryService.createSecretary(command));

        assertThat(ex)
                .hasMessageContaining("failed");
    }

    @Test
    void ensureDeletingSecretarytWithExceptionHandlingIsRight(){
        LocalDateTime created = temporalValueFactory.created_at();
        Name name = Name.builder().firstname("Hans").lastname("Berger").subname("Burger").build();
        Gender gender =Gender.MALE;
        Phonenumber phonenumber=Phonenumber.builder().serialnumber("3189781").areacode("667").countrycode("+43").build();
        Grade grade =Grade.DR;
        SubjectGrade subjectGrade= SubjectGrade.NAT_TECHN;
        Title title=Title.builder().grade(grade).subjectGrade(subjectGrade).build();
        Address address =Address.builder().place("Wien").street("Markomannenstraße 12").zipcode("1220").build();
        LocalDate birthDate =LocalDate.of(1998, Month.DECEMBER,18);

        String abbreviation ="FA";
        String nameOrd = "Facharztpraxis";
        Address addressOrd1 = Address.builder().place("Wien").street("Spengergasse 12").zipcode("1220").build();

        Ordination ordination= Ordination.builder()
                .abbrevitation(abbreviation)
                .name(nameOrd)
                .address(addressOrd1)
                .build();

        Secretary secretary = Secretary.builder()
                .name(name)
                .gender(gender)
                .phonenumber(phonenumber)
                .title(title)
                .address(address)
                .birthDate(birthDate)
                .ordination(ordination)
                .created_at(created)
                .build();

        when(secretaryRepository.findById(1L)).thenReturn(Optional.of(secretary));

        secretaryService.deleteSecretary(1L);

        verify(secretaryRepository).delete(secretary);
    }

    @Test
    void ensureReplacingSecretarytWithExceptionHandlingIsRight2() {
        LocalDateTime created = temporalValueFactory.created_at();
        Address address2 =Address.builder().place("Wien").street("Klaugasse 1").zipcode("1220").build();
        Name name = Name.builder().firstname("Hans").lastname("Berger").subname("Burger").build();
        Gender gender =Gender.MALE;
        Phonenumber phonenumber=Phonenumber.builder().serialnumber("3189781").areacode("667").countrycode("+43").build();
        Grade grade =Grade.DR;
        SubjectGrade subjectGrade= SubjectGrade.NAT_TECHN;
        Title title=Title.builder().grade(grade).subjectGrade(subjectGrade).build();
        Address address =Address.builder().place("Wien").street("Markomannenstraße 12").zipcode("1220").build();
        LocalDate birthDate =LocalDate.of(1998, Month.DECEMBER,18);

        String abbreviation ="FA";
        String nameOrd = "Facharztpraxis";
        Address addressOrd1 = Address.builder().place("Wien").street("Spengergasse 12").zipcode("1220").build();

        Ordination ordination= Ordination.builder()
                .abbrevitation(abbreviation)
                .name(nameOrd)
                .address(addressOrd1)
                .build();

        Secretary secretary = Secretary.builder()
                .name(name)
                .gender(gender)
                .phonenumber(phonenumber)
                .title(title)
                .address(address)
                .birthDate(birthDate)
                .ordination(ordination)
                .created_at(created)
                .build();

        MutateSecretaryCommand command = new MutateSecretaryCommand(
                name,gender,phonenumber,title,address,birthDate,ordination);

        when(secretaryRepository.findById(1L)).thenReturn(Optional.of(secretary));

        secretaryService.replaceSecretary(1L,command);

        verify(secretaryRepository).save(secretary);
    }

    @Test
    void ensurePartiallyUpdateIsWorking() {
        LocalDateTime created = temporalValueFactory.created_at();
        Name name = Name.builder().firstname("Hans").lastname("Berger").subname("Burger").build();
        Name name2 = Name.builder().firstname("Hans").lastname("Becker").subname("Burger").build();
        Title title2=Title.builder().grade(Grade.DR).subjectGrade(SubjectGrade.NAT_TECHN).build();
        Address address2 =Address.builder().place("Wien").street("Klaugasse 1").zipcode("1220").build();
        Ordination ordination2= Ordination.builder()
                .abbrevitation("Ha")
                .name("Hausarzt")
                .address(address2)
                .build();
        Gender gender =Gender.MALE;
        MutateSecretaryCommand command = new MutateSecretaryCommand(
                name2,null,null,title2,address2,null,ordination2);
        Phonenumber phonenumber=Phonenumber.builder().serialnumber("3189781").areacode("667").countrycode("+43").build();
        Grade grade =Grade.DR;
        SubjectGrade subjectGrade= SubjectGrade.NAT_TECHN;
        Title title=Title.builder().grade(grade).subjectGrade(subjectGrade).build();
        Address address =Address.builder().place("Wien").street("Markomannenstraße 12").zipcode("1220").build();
        LocalDate birthDate =LocalDate.of(1998, Month.DECEMBER,18);

        String abbreviation ="FA";
        String nameOrd = "Facharztpraxis";
        Address addressOrd1 = Address.builder().place("Wien").street("Spengergasse 12").zipcode("1220").build();

        Ordination ordination= Ordination.builder()
                .abbrevitation(abbreviation)
                .name(nameOrd)
                .address(addressOrd1)
                .build();

        Secretary secretary = Secretary.builder()
                .name(name)
                .gender(gender)
                .phonenumber(phonenumber)
                .title(title)
                .address(address)
                .birthDate(birthDate)
                .ordination(ordination)
                .created_at(created)
                .build();

        when(secretaryRepository.findById(secretary.getId())).thenReturn(Optional.of(secretary));
        when(secretaryRepository.save(secretary)).thenReturn(secretary);

        //when
        Secretary replaceSecretary = secretaryService.partiallyUpdateSecretary(secretary.getId(),command);

        //then
        assertThat(secretary).extracting(Secretary::getName).isEqualTo(secretary.getName());
        assertThat(secretary).extracting(Secretary::getGender).isEqualTo(secretary.getGender());
        assertThat(secretary).extracting(Secretary::getPhonenumber).isEqualTo(secretary.getPhonenumber());
        assertThat(secretary).extracting(Secretary::getTitle).isEqualTo(secretary.getTitle());
        assertThat(secretary).extracting(Secretary::getAddress).isEqualTo(secretary.getAddress());
        assertThat(secretary).extracting(Secretary::getBirthDate).isEqualTo(secretary.getBirthDate());
        assertThat(secretary).extracting(Secretary::getOrdination).isEqualTo(secretary.getOrdination());
        assertThat(ordination).extracting(Ordination::getAbbrevitation).isEqualTo(ordination.getAbbrevitation());
        assertThat(ordination).extracting(Ordination::getName).isEqualTo(ordination.getName());
        assertThat(ordination).extracting(Ordination::getAddress).isEqualTo(ordination.getAddress());
    }
}
