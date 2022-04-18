package at.spengergasse.sj21224bhifaslantanprojectdoctor.services;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.*;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence.PatientRepository;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.exceptions.ServiceException;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.MutatePatientCommand;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.PatientDto;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DataJpaTest
public class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;
    private PatientService patientService;
    private TemporalValueFactory temporalValueFactory;


    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
        temporalValueFactory = new TemporalValueFactory();
        patientService=new PatientService(patientRepository,temporalValueFactory);
    }

    @Test
    void ensureAllPatientWorks(){
        LocalDateTime creationTs = temporalValueFactory.created_at();
        Name name = Name.builder().firstname("Franz").lastname("Becker").subname("Franzi").build();
        Name name2 = Name.builder().firstname("Selina").lastname("Tan").subname("Ilyas").build();
        Gender gender =Gender.MALE;
        Gender gender2 =Gender.FEMALE;
        Phonenumber phonenumber =Phonenumber.builder().serialnumber("7811901").areacode("660").areacode("+43").build();
        Phonenumber phonenumber2 =Phonenumber.builder().serialnumber("3170155").areacode("660").areacode("+43").build();
        Grade grade =Grade.DIPL;
        Grade grade2 =Grade.MAG;
        SubjectGrade subjectGrade=SubjectGrade.SCIENT_MED;
        SubjectGrade subjectGrade2=SubjectGrade.ENGINEERING;
        Title title=Title.builder().grade(grade).subjectGrade(subjectGrade).build();
        Title title2=Title.builder().grade(grade2).subjectGrade(subjectGrade2).build();
        Address address =Address.builder().place("Wien").street("Klausgasse").zipcode("1210").build();
        Address address2 =Address.builder().place("Wien").street("Alfred-Kubin-Platz").zipcode("1220").build();
        String svnNumber ="4030080478";
        String svnNumber2 ="4030090404";
        LocalDate birthDate = LocalDate.of(2004,Month.APRIL,4);
        LocalDate birthDate2 = LocalDate.of(2004,Month.APRIL,9);
        Patient patient = Patient.builder()
                .name(name)
                .gender(gender)
                .phonenumber(phonenumber)
                .title(title)
                .address(address)
                .birthDate(birthDate)
                .svNumber(svnNumber)
                .created_at(creationTs)
                .build();

        Patient patient2 = Patient.builder()
                .name(name2)
                .gender(gender2)
                .phonenumber(phonenumber2)
                .title(title2)
                .address(address2)
                .birthDate(birthDate2)
                .svNumber(svnNumber2)
                .created_at(creationTs)
                .build();

        List<Patient> patients = List.of(patient,patient2);

        when(patientRepository.findAll()).thenReturn(patients);

        //when
        List<Patient> expectedPatients = patientService.getPatients();

        //then
        assertEquals(expectedPatients,patients);
        verify(patientRepository).findAll();

    }

    @Test
    void ensureDatabaseErrorWorksRight(){
        MutatePatientCommand mutatePatientCommand = new MutatePatientCommand(null,null,null,null,null,null,null);
        Exception pEx = new PersistenceException("connection lost");
        when(patientRepository.save(any())).thenThrow(pEx);

        //when
        var ex = assertThrows(ServiceException.class, () -> patientService.createPatient(mutatePatientCommand));

        //then
        assertThat(ex)
                .hasMessageContaining("Patient")
                .hasMessageContaining("database problems")
                .hasRootCause(pEx);
    }

    @Test
    void ensureCreatingPatientWithExceptionHandlingIsRight(){
        LocalDateTime creationTs = temporalValueFactory.created_at();
        Name name = Name.builder().firstname("Franz").lastname("Becker").subname("Franzi").build();
        Gender gender =Gender.MALE;
        Phonenumber phonenumber=Phonenumber.builder().serialnumber("7811901").areacode("660").areacode("+43").build();
        Grade grade =Grade.DIPL;
        SubjectGrade subjectGrade=SubjectGrade.SCIENT_MED;
        Title title=Title.builder().grade(grade).subjectGrade(subjectGrade).build();
        Address address =Address.builder().place("Wien").street("Klausgasse").zipcode("1210").build();
        String svnNumber ="4030080478";
        LocalDate birthDate = LocalDate.of(2004,Month.APRIL,4);
        Patient patient = Patient.builder()
                .name(name)
                .gender(gender)
                .phonenumber(phonenumber)
                .title(title)
                .address(address)
                .birthDate(birthDate)
                .svNumber(svnNumber)
                .created_at(creationTs)
                .build();

        PatientDto patientDto = new PatientDto(patient);

        Exception pEX = new PersistenceException("connection lost");
        when(patientRepository.findBySvNumber(svnNumber)).thenReturn(Optional.empty());
        when(patientRepository.save(any())).thenThrow(pEX);

        var ex= assertThrows(ServiceException.class, () -> patientService.createPatientFromDto(patientDto));

        assertThat(ex).hasMessageContaining("Patient")
                .hasMessageContaining("Patient",patientDto.name(),patientDto.gender(),patientDto.phonenumber(),patientDto.title(),
                        patientDto.address(),patientDto.birthDate(),patientDto.svNumber(),patientDto.created())
                .hasMessageContaining("database problem")
                .hasRootCause(pEX);
    }

    @Test
    void ensureCreateWithExistingPatientThrowsException(){
        LocalDateTime creationTs = temporalValueFactory.created_at();
        Name name = Name.builder().firstname("Franz").lastname("Becker").subname("Franzi").build();
        Gender gender =Gender.MALE;
        Phonenumber phonenumber=Phonenumber.builder().serialnumber("7811901").areacode("660").areacode("+43").build();
        Grade grade =Grade.DIPL;
        SubjectGrade subjectGrade=SubjectGrade.SCIENT_MED;
        Title title=Title.builder().grade(grade).subjectGrade(subjectGrade).build();
        Address address =Address.builder().place("Wien").street("Klausgasse").zipcode("1210").build();
        String svnNumber ="4030080478";
        LocalDate birthDate = LocalDate.of(2004,Month.APRIL,4);
        Patient patient = Patient.builder()
                .name(name)
                .gender(gender)
                .phonenumber(phonenumber)
                .title(title)
                .address(address)
                .birthDate(birthDate)
                .svNumber(svnNumber)
                .created_at(creationTs)
                .build();

        MutatePatientCommand mutatePatientCommand = new MutatePatientCommand(
                name,gender,phonenumber,title,svnNumber,address,birthDate);

        when(patientRepository.findBySvNumber(svnNumber)).thenReturn(Optional.of(patient));

        //when
        var ex = assertThrows(IllegalArgumentException.class, () -> patientService.createPatient(mutatePatientCommand));

        //then
        assertThat(ex)
                .hasMessageContaining("failed, because it already exists");
    }


    @Test
    void ensureDeletingPatientWithExceptionHandlingIsRight() {
        LocalDateTime creationTs = temporalValueFactory.created_at();
        Name name = Name.builder().firstname("Franz").lastname("Becker").subname("Franzi").build();
        Title title = Title.builder().grade(Grade.DIPL).subjectGrade(SubjectGrade.SCIENT_MED).build();
        Gender gender=Gender.MALE;
        Phonenumber phonenumber=Phonenumber.builder().serialnumber("7811901").areacode("660").areacode("+43").build();
        Grade grade =Grade.DIPL;
        SubjectGrade subjectGrade=SubjectGrade.SCIENT_MED;
        Address address =Address.builder().place("Wien").street("Klausgasse").zipcode("1210").build();
        String svnNumber ="4030080478";
        LocalDate birthDate = LocalDate.of(2004,Month.APRIL,4);

        Patient patient = Patient.builder()
                .name(name)
                .gender(gender)
                .phonenumber(phonenumber)
                .title(title)
                .address(address)
                .birthDate(birthDate)
                .svNumber(svnNumber)
                .created_at(creationTs)
                .build();

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        patientService.deletePatient(1L);

        verify(patientRepository).delete(patient);

    }

    @Test
    void ensureReplacingPatientWithExceptionHandlingIsRight() {
        LocalDateTime creationTs = temporalValueFactory.created_at();
        Name name = Name.builder().firstname("Franz").lastname("Becker").subname("Franzi").build();
        Title title = Title.builder().grade(Grade.DIPL).subjectGrade(SubjectGrade.SCIENT_MED).build();
        Gender gender = Gender.MALE;
        Phonenumber phonenumber = Phonenumber.builder().serialnumber("7811901").areacode("660").areacode("+43").build();
        Grade grade = Grade.DIPL;
        SubjectGrade subjectGrade = SubjectGrade.SCIENT_MED;
        Address address = Address.builder().place("Wien").street("Klausgasse").zipcode("1210").build();
        String svnNumber = "4030080478";
        LocalDate birthDate = LocalDate.of(2004, Month.APRIL, 4);

        Patient patient = Patient.builder()
                .name(name)
                .gender(gender)
                .phonenumber(phonenumber)
                .title(title)
                .address(address)
                .birthDate(birthDate)
                .svNumber(svnNumber)
                .created_at(creationTs)
                .build();


        MutatePatientCommand command = new MutatePatientCommand(name,gender,phonenumber,title,"4030090404",address,birthDate);


        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        patientService.replacePatient(1L, command);

        verify(patientRepository).save(patient);
    }

    @Test
    void ensurePartiallyUpdateIsWorking(){
        Name name2 = Name.builder().firstname("Franz").lastname("Beker").subname("Franzi").build();
        Title title2 = Title.builder().grade(Grade.B).subjectGrade(SubjectGrade.SCIENT_MED).build();
        Address address2 = Address.builder().place("Wien").street("Klaugasse").zipcode("1220").build();
        MutatePatientCommand command = new MutatePatientCommand(name2,null,null,title2,"4030090404",address2,LocalDate.of(2004, Month.APRIL, 4));
        LocalDateTime creationTs = temporalValueFactory.created_at();
        Name name = Name.builder().firstname("Franz").lastname("Becker").subname("Franzi").build();
        Title title = Title.builder().grade(Grade.DIPL).subjectGrade(SubjectGrade.SCIENT_MED).build();
        Gender gender = Gender.MALE;
        Phonenumber phonenumber = Phonenumber.builder().serialnumber("7811901").areacode("660").areacode("+43").build();
        Grade grade = Grade.DIPL;
        SubjectGrade subjectGrade = SubjectGrade.SCIENT_MED;
        Address address = Address.builder().place("Wien").street("Klausgasse").zipcode("1210").build();
        String svnNumber = "4030080478";
        LocalDate birthDate = LocalDate.of(2004, Month.APRIL, 4);

        Patient patient = Patient.builder()
                .name(name)
                .gender(gender)
                .phonenumber(phonenumber)
                .title(title)
                .address(address)
                .birthDate(birthDate)
                .svNumber(svnNumber)
                .created_at(creationTs)
                .build();

        when(patientRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        when(patientRepository.save(patient)).thenReturn(patient);

        //when
        Patient replacePatient = patientService.partiallyUpdatePatient(patient.getId(), command);

        //then
        assertThat(patient).extracting(Patient::getBirthDate).isEqualTo(patient.getBirthDate());
        assertThat(patient).extracting(Patient::getSvNumber).isEqualTo(patient.getSvNumber());
        assertThat(patient).extracting(Patient::getAddress).isEqualTo(patient.getAddress()).isEqualTo(patient.getAddress());
        assertThat(patient).extracting(Patient::getTitle).isEqualTo(patient.getTitle()).isEqualTo(patient.getTitle());
        assertThat(patient).extracting(Patient::getGender).isEqualTo(patient.getGender()).isEqualTo(patient.getGender());
        assertThat(patient).extracting(Patient::getPhonenumber).isEqualTo(patient.getPhonenumber()).isEqualTo(patient.getPhonenumber());
        assertThat(patient).extracting(Patient::getName).isEqualTo(patient.getName()).isEqualTo(patient.getName());

    }
}
