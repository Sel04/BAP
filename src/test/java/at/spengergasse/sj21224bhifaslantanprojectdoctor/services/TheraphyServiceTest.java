package at.spengergasse.sj21224bhifaslantanprojectdoctor.services;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.*;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence.DepartmentRepository;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence.DoctorRepository;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence.PatientRepository;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence.TherapyRepository;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.MutateTherapyCommand;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.exceptions.ServiceException;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.foundation.TemporalValueFactory;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.TheraphyDto;
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
public class TheraphyServiceTest {

    @Mock
    private TherapyRepository therapyRepository;
    private TherapyService therapyService;
    private TemporalValueFactory temporalValueFactory;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
        temporalValueFactory = new TemporalValueFactory();
        therapyService=new TherapyService(therapyRepository,temporalValueFactory);
    }

    @Test
    void ensureAllTherapiesWorks(){
        LocalDateTime created = temporalValueFactory.created_at();
        String abbreviation = "T1";
        String abbreviation2 = "T2";
        String kammerId = "DA7";
        String kammerId2 = "DA8";
        double salary = 145.7;
        double salary2 = 145.8;
        Subject subject= Subject.ALLGEMEINMEDIZIN;
        Subject subject2= Subject.ANAESTHESIOLOGIE_UND_INTENSIVMEDIZIN;
        Position position=Position.SENIOR_RESIDENT;
        Position position2=Position.HEAD_OF_DEPARTMENT;
        String art ="Chemotherapie";
        String art2 ="Kardiologie";
        LocalDate start= LocalDate.of(2017, Month.JANUARY,1);
        LocalDate end2= LocalDate.of(2017, Month.JANUARY,2);
        LocalDate start2= LocalDate.of(2017, Month.JANUARY,3);
        LocalDate end = LocalDate.of(2017,Month.APRIL,23);
        Name name = Name.builder().firstname("Volker").lastname("Hofbauer").subname("Hofi").build();
        Name name2 = Name.builder().firstname("Volker").lastname("Lokok").subname("abebabba").build();
        Gender gender =Gender.MALE;
        Gender gender2 = Gender.FEMALE;
        Phonenumber phonenumber=Phonenumber.builder().serialnumber("3190145").areacode("688").areacode("+43").build();
        Phonenumber phonenumber2=Phonenumber.builder().serialnumber("133233").areacode("611").areacode("+43").build();
        Grade grade =Grade.DR;
        Grade grade2 =Grade.MAG;
        SubjectGrade subjectGrade=SubjectGrade.MONT;
        SubjectGrade subjectGrade2=SubjectGrade.THEOL;
        Title title=Title.builder().grade(grade).subjectGrade(subjectGrade).build();
        Title title2=Title.builder().grade(grade2).subjectGrade(subjectGrade2).build();
        Address address =Address.builder().place("Wien").street("Kraygasse 89").zipcode("1220").build();
        Address address2 =Address.builder().place("Wien").street("Mailgasse 10").zipcode("1220").build();

        Doctor doctor =Doctor.builder()
                .name(name)
                .gender(gender)
                .phonenumber(phonenumber)
                .title(title)
                .address(address)
                .abbreviation(abbreviation)
                .aerztekammerId(kammerId)
                .subject(subject)
                .position(position)
                .salary(salary)
                .created(created)
                .build();

        //Patient
        Name namePa = Name.builder().firstname("Melissa").lastname("Stall").subname("Mel").build();
        Name namePa2 = Name.builder().firstname("Shervin").lastname("Ettefagh").subname("Alien").build();
        Gender genderPa =Gender.FEMALE;
        Gender genderPa2 =Gender.DIV;
        Phonenumber phonenumberPa=Phonenumber.builder().serialnumber("3490178").areacode("668").areacode("+43").build();
        Phonenumber phonenumberPa2=Phonenumber.builder().serialnumber("3198178").areacode("677").areacode("+43").build();
        Grade gradePa =Grade.MAG;
        Grade gradePa2 =Grade.DR;
        SubjectGrade subjectGradePa=SubjectGrade.SCIENCE;
        SubjectGrade subjectGradePa2=SubjectGrade.THEOL;
        Title titlePa=Title.builder().grade(grade).subjectGrade(subjectGrade).build();
        Title titlePa2=Title.builder().grade(grade2).subjectGrade(subjectGrade2).build();
        Address addressPa =Address.builder().place("Wien").street("Attemsgasse 9").zipcode("1220").build();
        Address addressPa2 =Address.builder().place("Wien").street("Saikogasse 12").zipcode("1220").build();
        String svnNumberPa ="4030181202";
        String svnNumberPa2 ="4030181203";
        LocalDate birthDatePa =LocalDate.of(2002, Month.DECEMBER,18);
        LocalDate birthDatePa2 =LocalDate.of(2002, Month.JANUARY,1);

        Patient patient =Patient.builder()
                .name(namePa)
                .gender(genderPa)
                .phonenumber(phonenumberPa)
                .title(titlePa)
                .address(addressPa)
                .birthDate(birthDatePa)
                .svNumber(svnNumberPa)
                .build();

        Therapy therapy = Therapy.builder()
                .art(art)
                .begin(start)
                .end(end)
                .doctor(doctor)
                .patient(patient)
                .created_at(created)
                .build();

        //Therapy 2

        Doctor doctor2 =Doctor.builder()
                .name(name2)
                .gender(gender2)
                .phonenumber(phonenumber2)
                .title(title2)
                .address(address2)
                .abbreviation(abbreviation2)
                .aerztekammerId(kammerId2)
                .subject(subject2)
                .position(position2)
                .salary(salary2)
                .created(created)
                .build();

        Patient patient2 =Patient.builder()
                .name(namePa2)
                .gender(genderPa2)
                .phonenumber(phonenumberPa2)
                .title(titlePa2)
                .address(addressPa2)
                .birthDate(birthDatePa2)
                .svNumber(svnNumberPa2)
                .build();

        Therapy therapy2 = Therapy.builder()
                .art(art2)
                .begin(start2)
                .end(end2)
                .doctor(doctor2)
                .patient(patient2)
                .created_at(created)
                .build();

        List<Therapy> therapies= List.of(therapy,therapy2);

        when(therapyRepository.findAll()).thenReturn(therapies);

        //when
        List<Therapy> expected = therapyService.getTheraphies();

        //then
        assertEquals(expected, therapies);
        verify(therapyRepository).findAll();
    }

    @Test
    void ensureDatabaseErrorWorksRight(){
        Department department = Department.builder()
                .name(null)
                .abbreviation(null)
                .head(null)
                .hospital(null)
                .build();
       Doctor d=  Doctor.builder().name(null)
                .gender(null)
                .phonenumber(null)
                .title(null)
                .address(null)
                .abbreviation(null)
                .aerztekammerId(null)
                .subject(null)
                .position(null)
                .department(department)
                .salary(0)
                .build();

        Patient p= Patient.builder()
                .name(null)
                .gender(null)
                .phonenumber(null)
                .title(null)
                .address(null)
                .birthDate(null)
                .svNumber(null)
                .build();

        MutateTherapyCommand command = new MutateTherapyCommand(
                null,null,null,p,d);
        Exception pEx=new PersistenceException("connection lost");
        when(therapyRepository.save(any())).thenThrow(pEx);

        //when
        var ex = assertThrows(ServiceException.class, () -> therapyService.createTheraphy(command));

        assertThat(ex)
                .hasMessageContaining("Cannot create entity of type Therapy")
                .hasRootCause(pEx);
    }

    @Test
    void ensureCreatingTherapyWithExceptionHandlingIsRight(){
        LocalDateTime created = temporalValueFactory.created_at();
        String abbreviation = "T1";
        String kammerId = "DA7";
        double salary = 145.7;
        Subject subject= Subject.ALLGEMEINMEDIZIN;
        Position position=Position.SENIOR_RESIDENT;
        String art ="Chemotherapie";
        LocalDate start= LocalDate.of(2017, Month.JANUARY,1);
        LocalDate end = LocalDate.of(2017,Month.APRIL,23);
        Name name = Name.builder().firstname("Volker").lastname("Hofbauer").subname("Hofi").build();
        Gender gender =Gender.MALE;
        Phonenumber phonenumber=Phonenumber.builder().serialnumber("3190145").areacode("688").areacode("+43").build();
        Grade grade =Grade.DR;
        SubjectGrade subjectGrade=SubjectGrade.MONT;
        Title title=Title.builder().grade(grade).subjectGrade(subjectGrade).build();
        Address address =Address.builder().place("Wien").street("Kraygasse 89").zipcode("1220").build();

        Doctor doctor =Doctor.builder()
                .name(name)
                .gender(gender)
                .phonenumber(phonenumber)
                .title(title)
                .address(address)
                .abbreviation(abbreviation)
                .aerztekammerId(kammerId)
                .subject(subject)
                .position(position)
                .salary(salary)
                .created(created)
                .build();

        //Patient
        Name namePa = Name.builder().firstname("Melissa").lastname("Stall").subname("Mel").build();
        Gender genderPa =Gender.FEMALE;
        Phonenumber phonenumberPa=Phonenumber.builder().serialnumber("3490178").areacode("668").areacode("+43").build();
        Grade gradePa =Grade.MAG;
        SubjectGrade subjectGradePa=SubjectGrade.SCIENCE;
        Title titlePa=Title.builder().grade(grade).subjectGrade(subjectGrade).build();
        Address addressPa =Address.builder().place("Wien").street("Attemsgasse 9").zipcode("1220").build();
        String svnNumberPa ="4030181202";
        LocalDate birthDatePa =LocalDate.of(2002, Month.DECEMBER,18);

        Patient patient =Patient.builder().name(namePa).gender(genderPa).phonenumber(phonenumberPa).title(titlePa).address(addressPa).birthDate(birthDatePa).svNumber(svnNumberPa).build();

        Therapy therapy = Therapy.builder()
                .art(art)
                .begin(start)
                .end(end)
                .doctor(doctor)
                .patient(patient)
                .created_at(created)
                .build();

        TheraphyDto theraphyDto=new TheraphyDto(therapy);

        Exception pEX = new PersistenceException("connection lost");
        when(therapyRepository.findTherapyByBeginAndEnd(start,end)).thenReturn(Optional.empty());
        when(therapyRepository.save(any())).thenThrow(pEX);

        var ex= assertThrows(ServiceException.class, () -> therapyService.createTheraphyFromDto(theraphyDto));

        assertThat(ex).hasMessageContaining("Therapy")
                .hasMessageContaining("Therapy",art,start,end,doctor,patient)
                .hasMessageContaining("database problem")
                .hasRootCause(pEX);


    }

    @Test
    void ensureCreateWithExistingTherapyIsRight(){
        LocalDateTime created = temporalValueFactory.created_at();
        String abbreviation = "T1";
        String kammerId = "DA7";
        double salary = 145.7;
        Subject subject= Subject.ALLGEMEINMEDIZIN;
        Position position=Position.SENIOR_RESIDENT;
        String art ="Chemotherapie";
        LocalDate start= LocalDate.of(2017, Month.JANUARY,1);
        LocalDate end = LocalDate.of(2017,Month.APRIL,23);
        Name name = Name.builder().firstname("Volker").lastname("Hofbauer").subname("Hofi").build();
        Gender gender =Gender.MALE;
        Phonenumber phonenumber=Phonenumber.builder().serialnumber("3190145").areacode("688").areacode("+43").build();
        Grade grade =Grade.DR;
        SubjectGrade subjectGrade=SubjectGrade.MONT;
        Title title=Title.builder().grade(grade).subjectGrade(subjectGrade).build();
        Address address =Address.builder().place("Wien").street("Kraygasse 89").zipcode("1220").build();

        Department department = Department.builder()
                .name("Kardiologie")
                .abbreviation("KARD")
                .head(null)
                .hospital(null)
                .build();

        Doctor doctor =Doctor.builder()
                .name(name)
                .gender(gender)
                .phonenumber(phonenumber)
                .title(title)
                .address(address)
                .abbreviation(abbreviation)
                .aerztekammerId(kammerId)
                .subject(subject)
                .position(position)
                .salary(salary)
                .department(department)
                .created(created)
                .build();

        //Patient
        Name namePa = Name.builder().firstname("Melissa").lastname("Stall").subname("Mel").build();
        Gender genderPa =Gender.FEMALE;
        Phonenumber phonenumberPa=Phonenumber.builder().serialnumber("3490178").areacode("668").areacode("+43").build();
        Grade gradePa =Grade.MAG;
        SubjectGrade subjectGradePa=SubjectGrade.SCIENCE;
        Title titlePa=Title.builder().grade(grade).subjectGrade(subjectGrade).build();
        Address addressPa =Address.builder().place("Wien").street("Attemsgasse 9").zipcode("1220").build();
        String svnNumberPa ="4030181202";
        LocalDate birthDatePa =LocalDate.of(2002, Month.DECEMBER,18);

        Patient patient =Patient.builder().name(namePa).gender(genderPa).phonenumber(phonenumberPa).title(titlePa).address(addressPa).birthDate(birthDatePa).svNumber(svnNumberPa).build();

        Therapy therapy = Therapy.builder()
                .art(art)
                .begin(start)
                .end(end)
                .doctor(doctor)
                .patient(patient)
                .created_at(created)
                .build();

        MutateTherapyCommand command = new MutateTherapyCommand(
                art,start,end,patient,doctor);

        when(therapyRepository.findTherapyByBeginAndEnd(start,end)).thenReturn(Optional.of(therapy));

        //when
        var ex = assertThrows(IllegalArgumentException.class, () -> therapyService.createTheraphy(command));

        //then
        assertThat(ex)
                .hasMessageContaining("failed");

    }

    @Test
    void ensureDeletingTherapytWithExceptionHandlingIsRight(){
        LocalDateTime created = temporalValueFactory.created_at();
        String abbreviation = "T1";
        String kammerId = "DA7";
        double salary = 145.7;
        Subject subject= Subject.ALLGEMEINMEDIZIN;
        Position position=Position.SENIOR_RESIDENT;
        String art ="Chemotherapie";
        LocalDate start= LocalDate.of(2017, Month.JANUARY,1);
        LocalDate end = LocalDate.of(2017,Month.APRIL,23);
        Name name = Name.builder().firstname("Volker").lastname("Hofbauer").subname("Hofi").build();
        Gender gender =Gender.MALE;
        Phonenumber phonenumber=Phonenumber.builder().serialnumber("3190145").areacode("688").areacode("+43").build();
        Grade grade =Grade.DR;
        SubjectGrade subjectGrade=SubjectGrade.MONT;
        Title title=Title.builder().grade(grade).subjectGrade(subjectGrade).build();
        Address address =Address.builder().place("Wien").street("Kraygasse 89").zipcode("1220").build();

        Doctor doctor =Doctor.builder()
                .name(name)
                .gender(gender)
                .phonenumber(phonenumber)
                .title(title)
                .address(address)
                .abbreviation(abbreviation)
                .aerztekammerId(kammerId)
                .subject(subject)
                .position(position)
                .salary(salary)
                .created(created)
                .build();

        //Patient
        Name namePa = Name.builder().firstname("Melissa").lastname("Stall").subname("Mel").build();
        Gender genderPa =Gender.FEMALE;
        Phonenumber phonenumberPa=Phonenumber.builder().serialnumber("3490178").areacode("668").areacode("+43").build();
        Grade gradePa =Grade.MAG;
        SubjectGrade subjectGradePa=SubjectGrade.SCIENCE;
        Title titlePa=Title.builder().grade(grade).subjectGrade(subjectGrade).build();
        Address addressPa =Address.builder().place("Wien").street("Attemsgasse 9").zipcode("1220").build();
        String svnNumberPa ="4030181202";
        LocalDate birthDatePa =LocalDate.of(2002, Month.DECEMBER,18);

        Patient patient =Patient.builder().name(namePa).gender(genderPa).phonenumber(phonenumberPa).title(titlePa).address(addressPa).birthDate(birthDatePa).svNumber(svnNumberPa).build();

        Therapy therapy = Therapy.builder()
                .art(art)
                .begin(start)
                .end(end)
                .doctor(doctor)
                .patient(patient)
                .created_at(created)
                .build();

        when(therapyRepository.findById(1L)).thenReturn(Optional.of(therapy));

        therapyService.deleteTheraphy(1L);

        verify(therapyRepository).delete(therapy);
    }

    @Test
    void ensureReplacingTherapytWithExceptionHandlingIsRight(){
        LocalDateTime created = temporalValueFactory.created_at();
        String abbreviation = "T1";
        String kammerId = "DA7";
        double salary = 145.7;
        Subject subject= Subject.ALLGEMEINMEDIZIN;
        Position position=Position.SENIOR_RESIDENT;
        String art ="Chemotherapie";
        LocalDate start= LocalDate.of(2017, Month.JANUARY,1);
        LocalDate end = LocalDate.of(2017,Month.APRIL,23);
        Name name = Name.builder().firstname("Volker").lastname("Hofbauer").subname("Hofi").build();
        Gender gender =Gender.MALE;
        Phonenumber phonenumber=Phonenumber.builder().serialnumber("3190145").areacode("688").areacode("+43").build();
        Grade grade =Grade.DR;
        SubjectGrade subjectGrade=SubjectGrade.MONT;
        Title title=Title.builder().grade(grade).subjectGrade(subjectGrade).build();
        Address address =Address.builder().place("Wien").street("Kraygasse 89").zipcode("1220").build();

        Doctor doctor =Doctor.builder()
                .name(name)
                .gender(gender)
                .phonenumber(phonenumber)
                .title(title)
                .address(address)
                .abbreviation(abbreviation)
                .aerztekammerId(kammerId)
                .subject(subject)
                .position(position)
                .salary(salary)
                .created(created)
                .build();

        //Patient
        Name namePa = Name.builder().firstname("Melissa").lastname("Stall").subname("Mel").build();
        Gender genderPa =Gender.FEMALE;
        Phonenumber phonenumberPa=Phonenumber.builder().serialnumber("3490178").areacode("668").areacode("+43").build();
        Grade gradePa =Grade.MAG;
        SubjectGrade subjectGradePa=SubjectGrade.SCIENCE;
        Title titlePa=Title.builder().grade(grade).subjectGrade(subjectGrade).build();
        Address addressPa =Address.builder().place("Wien").street("Attemsgasse 9").zipcode("1220").build();
        String svnNumberPa ="4030181202";
        LocalDate birthDatePa =LocalDate.of(2002, Month.DECEMBER,18);

        Patient patient =Patient.builder().name(namePa).gender(genderPa).phonenumber(phonenumberPa).title(titlePa).address(addressPa).birthDate(birthDatePa).svNumber(svnNumberPa).build();

        Therapy therapy = Therapy.builder()
                .art(art)
                .begin(start)
                .end(end)
                .doctor(doctor)
                .patient(patient)
                .created_at(created)
                .build();

        MutateTherapyCommand command =new MutateTherapyCommand(
                art,start,end,patient,doctor);

        when(therapyRepository.findById(1L)).thenReturn(Optional.of(therapy));

        therapyService.replaceTheraphy(1L, command);

        verify(therapyRepository).save(therapy);
    }

    @Test
    void ensurePartiallyUpdateIsWorking(){
        LocalDateTime created = temporalValueFactory.created_at();
        String abbreviation = "T1";
        String kammerId = "DA7";
        double salary = 145.7;
        Subject subject= Subject.ALLGEMEINMEDIZIN;
        Position position=Position.SENIOR_RESIDENT;
        String art ="Chemotherapie";
        String art2 ="Chemotherapie2";
        LocalDate start= LocalDate.of(2017, Month.JANUARY,1);
        LocalDate start2= LocalDate.of(2017, Month.JANUARY,1);
        LocalDate end = LocalDate.of(2017,Month.APRIL,23);
        LocalDate end2 = LocalDate.of(2017,Month.APRIL,23);
        Name name = Name.builder().firstname("Volker").lastname("Hofbauer").subname("Hofi").build();
        Name name2 = Name.builder().firstname("Hans").lastname("Becker").subname("Burger").build();
        Title title2=Title.builder().grade(Grade.DR).subjectGrade(SubjectGrade.NAT_TECHN).build();
        Address address2 =Address.builder().place("Wien").street("Klaugasse 1").zipcode("1220").build();
        Gender gender =Gender.MALE;
        Phonenumber phonenumber=Phonenumber.builder().serialnumber("3190145").areacode("688").areacode("+43").build();
        Grade grade =Grade.DR;
        SubjectGrade subjectGrade=SubjectGrade.MONT;
        Title title=Title.builder().grade(grade).subjectGrade(subjectGrade).build();
        Address address =Address.builder().place("Wien").street("Kraygasse 89").zipcode("1220").build();

        Doctor doctor =Doctor.builder()
                .name(name)
                .gender(gender)
                .phonenumber(phonenumber)
                .title(title)
                .address(address)
                .abbreviation(abbreviation)
                .aerztekammerId(kammerId)
                .subject(subject)
                .position(position)
                .salary(salary)
                .created(created)
                .build();

        //Patient
        Name namePa = Name.builder().firstname("Melissa").lastname("Stall").subname("Mel").build();
        Gender genderPa =Gender.FEMALE;
        Phonenumber phonenumberPa=Phonenumber.builder().serialnumber("3490178").areacode("668").areacode("+43").build();
        Grade gradePa =Grade.MAG;
        SubjectGrade subjectGradePa=SubjectGrade.SCIENCE;
        Title titlePa=Title.builder().grade(grade).subjectGrade(subjectGrade).build();
        Address addressPa =Address.builder().place("Wien").street("Attemsgasse 9").zipcode("1220").build();
        String svnNumberPa ="4030181202";
        LocalDate birthDatePa =LocalDate.of(2002, Month.DECEMBER,18);

        Patient patient =Patient.builder().name(namePa).gender(genderPa).phonenumber(phonenumberPa).title(titlePa).address(addressPa).birthDate(birthDatePa).svNumber(svnNumberPa).build();

        Therapy therapy = Therapy.builder()
                .art(art)
                .begin(start)
                .end(end)
                .doctor(doctor)
                .patient(patient)
                .created_at(created)
                .build();

        MutateTherapyCommand command =new MutateTherapyCommand(
                art2,start2,end2,patient,doctor);

        when(therapyRepository.findById(therapy.getId())).thenReturn(Optional.of(therapy));
        when(therapyRepository.save(therapy)).thenReturn(therapy);

        //when
        Therapy replace =therapyService.partiallyUpdateTheraphy(therapy.getId(), command);

        //then
        assertThat(therapy).extracting(Therapy::getArt).isEqualTo(therapy.getArt());
        assertThat(therapy).extracting(Therapy::getBegin).isEqualTo(therapy.getBegin());
        assertThat(therapy).extracting(Therapy::getEnd).isEqualTo(therapy.getEnd());
        assertThat(therapy).extracting(Therapy::getDoctor).isEqualTo(therapy.getDoctor());
        assertThat(therapy).extracting(Therapy::getPatient).isEqualTo(therapy.getPatient());

    }
}
