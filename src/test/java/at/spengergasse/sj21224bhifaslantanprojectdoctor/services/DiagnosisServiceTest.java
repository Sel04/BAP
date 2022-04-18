package at.spengergasse.sj21224bhifaslantanprojectdoctor.services;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.*;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.foundation.TemporalValueFactory;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence.DiagnosisRepository;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.DiagnoseDto;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.MutateDiagnosisCommand;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.exceptions.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.PersistenceException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DataJpaTest
public class DiagnosisServiceTest {

    @Mock
    private DiagnosisRepository diagnosisRepository;
    private DiagnoseService diagnoseService;
    private TemporalValueFactory temporalValueFactory;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
        temporalValueFactory = new TemporalValueFactory();
        diagnoseService = new DiagnoseService(diagnosisRepository,temporalValueFactory);
    }

    @Test
    void ensureCreatingDiagnosisWithExcetpionHandlingIsRight(){
        LocalDate date = LocalDate.now();
        String diagnosis = "Ajksdkjsadjsajdsadjsa";
        Name selTan = Name.builder().firstname("Sel").subname("Ilyas").lastname("Tan").build();
        Phonenumber selTanPhoneNumber = Phonenumber.builder().areacode("+43").countrycode("664").serialnumber("918329081").build();
        Title title = Title.builder().grade(Grade.DR).subjectGrade(SubjectGrade.PHD).build();
        Address address = Address.builder().street("Pernerstorfergasse 60/3/18").zipcode("1100").place("Wien").build();


        Doctor doctor1 = Doctor.builder()
                .name(selTan)
                .gender(Gender.MALE)
                .phonenumber(selTanPhoneNumber)
                .title(title)
                .birthDate(LocalDate.of(1990,3,20))
                .abbreviation("ST")
                .aerztekammerId("DO1")
                .subject(Subject.ALLGEMEINMEDIZIN)
                .position(Position.HEAD_OF_DEPARTMENT)
                .salary(2000.00)
                .build();

        Grade grade =Grade.DIPL;
        SubjectGrade subjectGrade=SubjectGrade.ENGINEERING;
        Title title1 = Title.builder().grade(grade).subjectGrade(subjectGrade).build();
        Name name = Name.builder().firstname("Sel").lastname("Tan").subname("Ilyas").build();
        Gender gender = Gender.MALE;
        Phonenumber phonenumber=Phonenumber.builder().serialnumber("4529229").areacode("677").countrycode("+43").build();
        Address address1 = Address.builder().place("Wien").street("Spengergasse 20").zipcode("1050").build();
        Patient patient = Patient.builder().name(name).gender(gender).phonenumber(phonenumber).title(title1).address(address1).birthDate(LocalDate.of(2004, Month.APRIL,9)).svNumber("4030561616").build();

        Diagnosis diagnosis1 = Diagnosis.builder().date(date).diagnosis(diagnosis).doctor(doctor1).patient(patient).build();

        DiagnoseDto diagnoseDto = new DiagnoseDto(diagnosis1);

        Exception pEx = new PersistenceException("connection lost");
        when(diagnosisRepository.findDiagnosisByDate(date)).thenReturn(Optional.empty());
        when(diagnosisRepository.save(any())).thenThrow(pEx);

        var ex = assertThrows(ServiceException.class,() -> diagnoseService.createDiagnosisFromDto(diagnoseDto));

        assertThat(ex).hasMessageContaining("Diagnosis")
                .hasMessageContaining(diagnosis,doctor1,patient)
                .hasMessageContaining("database problem")
                .hasRootCause(pEx);
    }

    @Test
    void ensureGetAllWorks(){
        LocalDate date = LocalDate.now();
        String diagnosis = "Ajksdkjsadjsajdsadjsa";
        Name selTan = Name.builder().firstname("Sel").subname("Ilyas").lastname("Tan").build();
        Phonenumber selTanPhoneNumber = Phonenumber.builder().areacode("+43").countrycode("664").serialnumber("918329081").build();
        Title title = Title.builder().grade(Grade.DR).subjectGrade(SubjectGrade.PHD).build();
        Address address = Address.builder().street("Pernerstorfergasse 60/3/18").zipcode("1100").place("Wien").build();


        Doctor doctor1 = Doctor.builder()
                .name(selTan)
                .gender(Gender.MALE)
                .phonenumber(selTanPhoneNumber)
                .title(title)
                .birthDate(LocalDate.of(1990,3,20))
                .abbreviation("ST")
                .aerztekammerId("DO1")
                .subject(Subject.ALLGEMEINMEDIZIN)
                .position(Position.HEAD_OF_DEPARTMENT)
                .salary(2000.00)
                .build();

        Grade grade =Grade.DIPL;
        SubjectGrade subjectGrade=SubjectGrade.ENGINEERING;
        Title title1 = Title.builder().grade(grade).subjectGrade(subjectGrade).build();
        Name name = Name.builder().firstname("Sel").lastname("Tan").subname("Ilyas").build();
        Gender gender = Gender.MALE;
        Phonenumber phonenumber=Phonenumber.builder().serialnumber("4529229").areacode("677").countrycode("+43").build();
        Address address1 = Address.builder().place("Wien").street("Spengergasse 20").zipcode("1050").build();
        Patient patient = Patient.builder().name(name).gender(gender).phonenumber(phonenumber).title(title1).address(address1).birthDate(LocalDate.of(2004, Month.APRIL,9)).svNumber("4030561616").build();

        Diagnosis diagnosis1 = Diagnosis.builder().date(date).diagnosis(diagnosis).doctor(doctor1).patient(patient).build();
        Diagnosis diagnosis2 = Diagnosis.builder().date(date).diagnosis(diagnosis).doctor(doctor1).patient(patient).build();

        List<Diagnosis> diagnoses = Arrays.asList(diagnosis2,diagnosis1);
        when(diagnosisRepository.findAll()).thenReturn(diagnoses);
        //when
        List<Diagnosis> expected = diagnoseService.getDiagnosis();

        //then
        assertEquals(expected,diagnoses);
        verify(diagnosisRepository).findAll();
    }

    @Test
    void ensureUpdateWorksProperly(){
        LocalDate date = LocalDate.now();
        String diagnosis = "Ajksdkjsadjsajdsadjsa";
        Name selTan = Name.builder().firstname("Sel").subname("Ilyas").lastname("Tan").build();
        Phonenumber selTanPhoneNumber = Phonenumber.builder().areacode("+43").countrycode("664").serialnumber("918329081").build();
        Title title = Title.builder().grade(Grade.DR).subjectGrade(SubjectGrade.PHD).build();
        Address address = Address.builder().street("Pernerstorfergasse 60/3/18").zipcode("1100").place("Wien").build();


        Doctor doctor1 = Doctor.builder()
                .name(selTan)
                .gender(Gender.MALE)
                .phonenumber(selTanPhoneNumber)
                .title(title)
                .birthDate(LocalDate.of(1990,3,20))
                .abbreviation("ST")
                .aerztekammerId("DO1")
                .subject(Subject.ALLGEMEINMEDIZIN)
                .position(Position.HEAD_OF_DEPARTMENT)
                .salary(2000.00)
                .build();

        Grade grade =Grade.DIPL;
        SubjectGrade subjectGrade=SubjectGrade.ENGINEERING;
        Title title1 = Title.builder().grade(grade).subjectGrade(subjectGrade).build();
        Name name = Name.builder().firstname("Sel").lastname("Tan").subname("Ilyas").build();
        Gender gender = Gender.MALE;
        Phonenumber phonenumber=Phonenumber.builder().serialnumber("4529229").areacode("677").countrycode("+43").build();
        Address address1 = Address.builder().place("Wien").street("Spengergasse 20").zipcode("1050").build();
        Patient patient = Patient.builder().name(name).gender(gender).phonenumber(phonenumber).title(title1).address(address1).birthDate(LocalDate.of(2004, Month.APRIL,9)).svNumber("4030561616").build();

        Diagnosis diagnosis1 = Diagnosis.builder().date(date).diagnosis(diagnosis).doctor(doctor1).patient(patient).build();
        MutateDiagnosisCommand mutateDiagnosisCommand = new MutateDiagnosisCommand(date,"miss gilbert",doctor1,patient);
        when(diagnosisRepository.findDiagnosisById(diagnosis1.getId())).thenReturn(Optional.of(diagnosis1));
        when(diagnosisRepository.save(diagnosis1)).thenReturn(diagnosis1);

        Diagnosis replaceDiagnoses  = diagnoseService.replaceDiagnosis(diagnosis1.getId(),mutateDiagnosisCommand);
        Optional<Diagnosis> diagnosiss = diagnosisRepository.findDiagnosisById(diagnosis1.getId());

        assertThat(replaceDiagnoses).isEqualTo(diagnosiss.get());
    }

    @Test
    void ensurepartiallyUpdateWorksProperly(){
        LocalDate date = LocalDate.now();
        String diagnosis = "Ajksdkjsadjsajdsadjsa";
        Name selTan = Name.builder().firstname("Sel").subname("Ilyas").lastname("Tan").build();
        Phonenumber selTanPhoneNumber = Phonenumber.builder().areacode("+43").countrycode("664").serialnumber("918329081").build();
        Title title = Title.builder().grade(Grade.DR).subjectGrade(SubjectGrade.PHD).build();
        Address address = Address.builder().street("Pernerstorfergasse 60/3/18").zipcode("1100").place("Wien").build();


        Doctor doctor1 = Doctor.builder()
                .name(selTan)
                .gender(Gender.MALE)
                .phonenumber(selTanPhoneNumber)
                .title(title)
                .birthDate(LocalDate.of(1990,3,20))
                .abbreviation("ST")
                .aerztekammerId("DO1")
                .subject(Subject.ALLGEMEINMEDIZIN)
                .position(Position.HEAD_OF_DEPARTMENT)
                .salary(2000.00)
                .build();

        Grade grade =Grade.DIPL;
        SubjectGrade subjectGrade=SubjectGrade.ENGINEERING;
        Title title1 = Title.builder().grade(grade).subjectGrade(subjectGrade).build();
        Name name = Name.builder().firstname("Sel").lastname("Tan").subname("Ilyas").build();
        Gender gender = Gender.MALE;
        Phonenumber phonenumber=Phonenumber.builder().serialnumber("4529229").areacode("677").countrycode("+43").build();
        Address address1 = Address.builder().place("Wien").street("Spengergasse 20").zipcode("1050").build();
        Patient patient = Patient.builder().name(name).gender(gender).phonenumber(phonenumber).title(title1).address(address1).birthDate(LocalDate.of(2004, Month.APRIL,9)).svNumber("4030561616").build();

        Diagnosis diagnosis1 = Diagnosis.builder().date(date).diagnosis(diagnosis).doctor(doctor1).patient(patient).build();
        MutateDiagnosisCommand mutateDiagnosisCommand = new MutateDiagnosisCommand(date,"miss gilbert",doctor1,patient);
        when(diagnosisRepository.findDiagnosisById(diagnosis1.getId())).thenReturn(Optional.of(diagnosis1));
        when(diagnosisRepository.save(diagnosis1)).thenReturn(diagnosis1);

        Diagnosis partiallyDiagnoses  = diagnoseService.partiallyUpdate(diagnosis1.getId(),mutateDiagnosisCommand);
        Optional<Diagnosis> diagnosiss = diagnosisRepository.findDiagnosisById(diagnosis1.getId());

        assertThat(partiallyDiagnoses).isEqualTo(diagnosiss.get());
    }

    @Test
    void ensureDeleteWorksProperly(){
        LocalDate date = LocalDate.now();
        String diagnosis = "Ajksdkjsadjsajdsadjsa";
        Name selTan = Name.builder().firstname("Sel").subname("Ilyas").lastname("Tan").build();
        Phonenumber selTanPhoneNumber = Phonenumber.builder().areacode("+43").countrycode("664").serialnumber("918329081").build();
        Title title = Title.builder().grade(Grade.DR).subjectGrade(SubjectGrade.PHD).build();
        Address address = Address.builder().street("Pernerstorfergasse 60/3/18").zipcode("1100").place("Wien").build();


        Doctor doctor1 = Doctor.builder()
                .name(selTan)
                .gender(Gender.MALE)
                .phonenumber(selTanPhoneNumber)
                .title(title)
                .birthDate(LocalDate.of(1990,3,20))
                .abbreviation("ST")
                .aerztekammerId("DO1")
                .subject(Subject.ALLGEMEINMEDIZIN)
                .position(Position.HEAD_OF_DEPARTMENT)
                .salary(2000.00)
                .build();

        Grade grade =Grade.DIPL;
        SubjectGrade subjectGrade=SubjectGrade.ENGINEERING;
        Title title1 = Title.builder().grade(grade).subjectGrade(subjectGrade).build();
        Name name = Name.builder().firstname("Sel").lastname("Tan").subname("Ilyas").build();
        Gender gender = Gender.MALE;
        Phonenumber phonenumber=Phonenumber.builder().serialnumber("4529229").areacode("677").countrycode("+43").build();
        Address address1 = Address.builder().place("Wien").street("Spengergasse 20").zipcode("1050").build();
        Patient patient = Patient.builder().name(name).gender(gender).phonenumber(phonenumber).title(title1).address(address1).birthDate(LocalDate.of(2004, Month.APRIL,9)).svNumber("4030561616").build();

        Diagnosis diagnosis1 = Diagnosis.builder().date(date).diagnosis(diagnosis).doctor(doctor1).patient(patient).build();
        when(diagnosisRepository.findDiagnosisById(diagnosis1.getId())).thenReturn(Optional.of(diagnosis1));

        diagnoseService.deleteDiagnosis(diagnosis1.getId());

        verify(diagnosisRepository).findDiagnosisById(diagnosis1.getId());
    }


}
