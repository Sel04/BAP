package at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PatientRepositoryTest {

    @Autowired
    private PatientRepository patientRepository;

    @Test
    void ensureSavesWorksProbably(){
        Grade grade =Grade.DIPL;
        SubjectGrade subjectGrade=SubjectGrade.ENGINEERING;
        Title title = Title.builder().grade(grade).subjectGrade(subjectGrade).build();
        Name name = Name.builder().firstname("Sel").lastname("Tan").subname("Ilyas").build();
        Gender gender = Gender.MALE;
        Phonenumber phonenumber=Phonenumber.builder().serialnumber("4529229").areacode("677").countrycode("+43").build();
        Address address = Address.builder().place("Wien").street("Spengergasse 20").zipcode("1050").build();
        Patient patient = Patient.builder().name(name).gender(gender).phonenumber(phonenumber).title(title).address(address).birthDate(LocalDate.of(2004, Month.APRIL,9)).svNumber("4030561616").build();

        var saved =patientRepository.save(patient);

        assertThat(saved).isSameAs(patient);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getSvNumber()).isSameAs(patient.getSvNumber());

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

        var saved =patientRepository.save(patient);

        patientRepository.delete(patient);

        assertThat(patientRepository.count()).isEqualTo(0);
    }

    @Test
    void ensureFindingPatientBySvNumberWorksProbably(){
        Grade grade =Grade.DIPL;
        SubjectGrade subjectGrade=SubjectGrade.ENGINEERING;
        Title title = Title.builder().grade(grade).subjectGrade(subjectGrade).build();
        Name name = Name.builder().firstname("Sel").lastname("Tan").subname("Ilyas").build();
        Gender gender = Gender.MALE;
        Phonenumber phonenumber=Phonenumber.builder().serialnumber("4529229").areacode("677").countrycode("+43").build();
        Address address = Address.builder().place("Wien").street("Spengergasse 20").zipcode("1050").build();
        String svNumber ="4030561616";
        Patient patient = Patient.builder().name(name).gender(gender).phonenumber(phonenumber).title(title).address(address).birthDate(LocalDate.of(2004, Month.APRIL,9)).svNumber(svNumber).build();

        var saved =patientRepository.save(patient);

        var finding = patientRepository.findBySvNumber(svNumber);

        assertThat(saved).isSameAs(patient);
        assertThat(finding.get().getSvNumber()).isEqualTo(svNumber);
    }

    @Test
    void isPatientExistsBySvNumber(){
        Grade grade =Grade.DIPL;
        SubjectGrade subjectGrade=SubjectGrade.ENGINEERING;
        Title title = Title.builder().grade(grade).subjectGrade(subjectGrade).build();
        Name name = Name.builder().firstname("Sel").lastname("Tan").subname("Ilyas").build();
        Gender gender = Gender.MALE;
        Phonenumber phonenumber=Phonenumber.builder().serialnumber("4529229").areacode("677").countrycode("+43").build();
        Address address = Address.builder().place("Wien").street("Spengergasse 20").zipcode("1050").build();
        String svNumber ="4030561616";
        Patient patient = Patient.builder().name(name).gender(gender).phonenumber(phonenumber).title(title).address(address).birthDate(LocalDate.of(2004, Month.APRIL,9)).svNumber(svNumber).build();
        patientRepository.save(patient);

        Boolean actualResult = patientRepository.existsBySvNumber(patient.getSvNumber());
        assertThat(actualResult).isTrue();
    }
}
