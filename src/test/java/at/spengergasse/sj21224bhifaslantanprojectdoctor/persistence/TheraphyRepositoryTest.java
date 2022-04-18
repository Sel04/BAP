package at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TheraphyRepositoryTest {

    @Autowired
    private TherapyRepository therapyRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Test
    void ensureSaveWorksProbably(){
        Grade grade =Grade.DIPL;
        SubjectGrade subjectGrade=SubjectGrade.ENGINEERING;
        Title title = Title.builder().grade(grade).subjectGrade(subjectGrade).build();
        Name name = Name.builder().firstname("Sel").lastname("Tan").subname("Ilyas").build();
        Gender gender = Gender.MALE;
        Phonenumber phonenumber=Phonenumber.builder().serialnumber("4529229").areacode("677").countrycode("+43").build();
        Address address = Address.builder().place("Wien").street("Spengergasse 20").zipcode("1050").build();
        Patient patient = Patient.builder().name(name).gender(gender).phonenumber(phonenumber).title(title).address(address).birthDate(LocalDate.of(2004, Month.APRIL,9)).svNumber("4030561616").build();

        Grade grade2 =Grade.MAG;
        SubjectGrade subjectGrade2=SubjectGrade.THEOL;
        Title title2 = Title.builder().grade(grade).subjectGrade(subjectGrade).build();
        Name name2= Name.builder().firstname("Unger").lastname("Klaus").subname("Java-King").build();
        Gender gender2 = Gender.MALE;
        Phonenumber phonenumber2=Phonenumber.builder().serialnumber("4579149").areacode("609").countrycode("+43").build();
        Address address2 = Address.builder().place("Niederösterreich").street("Wetzdorf 20").zipcode("4501").build();
        Subject subject = Subject.ALLGEMEINMEDIZIN;
        Position position = Position.HEAD_OF_DEPARTMENT;

        Doctor doctor=Doctor.builder().name(name2).gender(gender2).phonenumber(phonenumber2).title(title2).address(address2).birthDate(LocalDate.of(1998, Month.AUGUST,2)).abbreviation("XY").aerztekammerId("12").salary(1200.78).subject(subject).position(position).build();

        Therapy therapy = Therapy.builder().art("Psycho").doctor(doctor).patient(patient).begin(LocalDate.of(2019,Month.AUGUST,12)).end(LocalDate.of(2019,Month.AUGUST,28)).build();

        var saved = therapyRepository.save(therapy);

        assertThat(saved).isSameAs(therapy);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getDoctor()).isSameAs(therapy.getDoctor());
        assertThat(saved.getPatient()).isSameAs(therapy.getPatient());
        assertThat(saved.getBegin()).isNotNull();
        assertThat(saved.getEnd()).isNotNull();
    }

    @Test
    void ensureDeletingWorksProbably(){
        Grade grade =Grade.DIPL;
        SubjectGrade subjectGrade=SubjectGrade.ENGINEERING;
        Title title = Title.builder().grade(grade).subjectGrade(subjectGrade).build();
        Name name = Name.builder().firstname("Sel").lastname("Tan").subname("Ilyas").build();
        Gender gender = Gender.MALE;
        Phonenumber phonenumber=Phonenumber.builder().serialnumber("4529229").areacode("677").countrycode("+43").build();
        Address address = Address.builder().place("Wien").street("Spengergasse 20").zipcode("1050").build();
        Patient patient = Patient.builder().name(name).gender(gender).phonenumber(phonenumber).title(title).address(address).birthDate(LocalDate.of(2004, Month.APRIL,9)).svNumber("4030561616").build();

        Grade grade2 =Grade.MAG;
        SubjectGrade subjectGrade2=SubjectGrade.THEOL;
        Title title2 = Title.builder().grade(grade).subjectGrade(subjectGrade).build();
        Name name2= Name.builder().firstname("Unger").lastname("Klaus").subname("Java-King").build();
        Gender gender2 = Gender.MALE;
        Phonenumber phonenumber2=Phonenumber.builder().serialnumber("4579149").areacode("609").countrycode("+43").build();
        Address address2 = Address.builder().place("Niederösterreich").street("Wetzdorf 20").zipcode("4501").build();
        Subject subject = Subject.ALLGEMEINMEDIZIN;
        Position position = Position.HEAD_OF_DEPARTMENT;

        Doctor doctor=Doctor.builder().name(name2).gender(gender2).phonenumber(phonenumber2).title(title2).address(address2).birthDate(LocalDate.of(1998, Month.AUGUST,2)).abbreviation("XY").aerztekammerId("12").salary(1200.78).subject(subject).position(position).build();

        Therapy therapy = Therapy.builder().art("Psycho").doctor(doctor).patient(patient).begin(LocalDate.of(2019,Month.AUGUST,12)).end(LocalDate.of(2019,Month.AUGUST,28)).build();

        var saved = therapyRepository.save(therapy);

        therapyRepository.delete(therapy);

        assertThat(therapyRepository.count()).isEqualTo(0);
    }

    @Test
    void ensureFindingTheraphyByIdWorksProbably(){
        Grade grade =Grade.DIPL;
        SubjectGrade subjectGrade=SubjectGrade.ENGINEERING;
        Title title = Title.builder().grade(grade).subjectGrade(subjectGrade).build();
        Name name = Name.builder().firstname("Sel").lastname("Tan").subname("Ilyas").build();
        Gender gender = Gender.MALE;
        Phonenumber phonenumber=Phonenumber.builder().serialnumber("4529229").areacode("677").countrycode("+43").build();
        Address address = Address.builder().place("Wien").street("Spengergasse 20").zipcode("1050").build();
        Patient patient = Patient.builder().name(name).gender(gender).phonenumber(phonenumber).title(title).address(address).birthDate(LocalDate.of(2004, Month.APRIL,9)).svNumber("4030561616").build();
        patientRepository.save(patient);
        Grade grade2 =Grade.MAG;
        SubjectGrade subjectGrade2=SubjectGrade.THEOL;
        Title title2 = Title.builder().grade(grade).subjectGrade(subjectGrade).build();
        Name name2= Name.builder().firstname("Unger").lastname("Klaus").subname("Java-King").build();
        Gender gender2 = Gender.MALE;
        Phonenumber phonenumber2=Phonenumber.builder().serialnumber("4579149").areacode("609").countrycode("+43").build();
        Address address2 = Address.builder().place("Niederösterreich").street("Wetzdorf 20").zipcode("4501").build();
        Subject subject = Subject.ALLGEMEINMEDIZIN;
        Position position = Position.HEAD_OF_DEPARTMENT;

        Doctor doctor=Doctor.builder().name(name2).gender(gender2).phonenumber(phonenumber2).title(title2).address(address2).birthDate(LocalDate.of(1998, Month.AUGUST,2)).abbreviation("XY").aerztekammerId("12").salary(1200.78).subject(subject).position(position).build();
        doctorRepository.save(doctor);
        Therapy therapy = Therapy.builder().art("Psycho").doctor(doctor).patient(patient).begin(LocalDate.of(2019,Month.AUGUST,12)).end(LocalDate.of(2019,Month.AUGUST,28)).build();

        var saved = therapyRepository.save(therapy);

        var finding = therapyRepository.findById(therapy.getId());

        assertThat(saved).isSameAs(therapy);
        assertThat(finding.get().getId()).isEqualTo(therapy.getId());
    }

    @Test
    void isTherapyExistsById(){
        Grade grade =Grade.DIPL;
        SubjectGrade subjectGrade=SubjectGrade.ENGINEERING;
        Title title = Title.builder().grade(grade).subjectGrade(subjectGrade).build();
        Name name = Name.builder().firstname("Sel").lastname("Tan").subname("Ilyas").build();
        Gender gender = Gender.MALE;
        Phonenumber phonenumber=Phonenumber.builder().serialnumber("4529229").areacode("677").countrycode("+43").build();
        Address address = Address.builder().place("Wien").street("Spengergasse 20").zipcode("1050").build();
        Patient patient = Patient.builder().name(name).gender(gender).phonenumber(phonenumber).title(title).address(address).birthDate(LocalDate.of(2004, Month.APRIL,9)).svNumber("4030561616").build();
        patientRepository.save(patient);
        Grade grade2 =Grade.MAG;
        SubjectGrade subjectGrade2=SubjectGrade.THEOL;
        Title title2 = Title.builder().grade(grade).subjectGrade(subjectGrade).build();
        Name name2= Name.builder().firstname("Unger").lastname("Klaus").subname("Java-King").build();
        Gender gender2 = Gender.MALE;
        Phonenumber phonenumber2=Phonenumber.builder().serialnumber("4579149").areacode("609").countrycode("+43").build();
        Address address2 = Address.builder().place("Niederösterreich").street("Wetzdorf 20").zipcode("4501").build();
        Subject subject = Subject.ALLGEMEINMEDIZIN;
        Position position = Position.HEAD_OF_DEPARTMENT;

        Doctor doctor=Doctor.builder().name(name2).gender(gender2).phonenumber(phonenumber2).title(title2).address(address2).birthDate(LocalDate.of(1998, Month.AUGUST,2)).abbreviation("XY").aerztekammerId("12").salary(1200.78).subject(subject).position(position).build();
        doctorRepository.save(doctor);
        Therapy therapy = Therapy.builder().art("Psycho").doctor(doctor).patient(patient).begin(LocalDate.of(2019,Month.AUGUST,12)).end(LocalDate.of(2019,Month.AUGUST,28)).build();

        therapyRepository.save(therapy);

        Boolean actualRestult = therapyRepository.existsByArt(therapy.getArt());
        assertThat(actualRestult).isTrue();
    }
}
