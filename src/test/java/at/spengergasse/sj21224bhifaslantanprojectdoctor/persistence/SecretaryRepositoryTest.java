package at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class SecretaryRepositoryTest {

    @Autowired
    private SecretaryRepository secretaryRepository;

    @Test
    void ensureSaveRight(){
        Grade grade =Grade.MAG;
        SubjectGrade subjectGrade=SubjectGrade.MED_VET;
        Title title = Title.builder().grade(grade).subjectGrade(subjectGrade).build();
        Name name = Name.builder().firstname("Aslan").lastname("Cemil").subname("koyun").build();
        Gender gender = Gender.DIV;
        Phonenumber phonenumber=Phonenumber.builder().serialnumber("454227893").areacode("678").countrycode("+43").build();
        Address address = Address.builder().place("Wien").street("Pernerstoffergasse 20").zipcode("1100").build();

        Secretary secretary = Secretary.builder().name(name).gender(gender).phonenumber(phonenumber).title(title).address(address).birthDate(LocalDate.of(2004, Month.APRIL,9)).build();

        var saved =secretaryRepository.save(secretary);

        assertThat(saved).isSameAs(secretary);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getGender()).isSameAs(secretary.getGender());


    }

    @Test
    void ensureDeletingWorksProbably(){
        Grade grade =Grade.MAG;
        SubjectGrade subjectGrade=SubjectGrade.MED_VET;
        Title title = Title.builder().grade(grade).subjectGrade(subjectGrade).build();
        Name name = Name.builder().firstname("Aslan").lastname("Cemil").subname("koyun").build();
        Gender gender = Gender.DIV;
        Phonenumber phonenumber=Phonenumber.builder().serialnumber("454227893").areacode("678").countrycode("+43").build();
        Address address = Address.builder().place("Wien").street("Pernerstoffergasse 20").zipcode("1100").build();

        Secretary secretary = Secretary.builder().name(name).gender(gender).phonenumber(phonenumber).title(title).address(address).birthDate(LocalDate.of(2004, Month.APRIL,9)).build();

        var saved =secretaryRepository.save(secretary);

        secretaryRepository.delete(secretary);

        assertThat(secretaryRepository.count()).isEqualTo(0);
    }

    @Test
    void ensureFindingSecretaryByBirthDateWorksProbably(){
        Grade grade =Grade.MAG;
        SubjectGrade subjectGrade=SubjectGrade.MED_VET;
        Title title = Title.builder().grade(grade).subjectGrade(subjectGrade).build();
        Name name = Name.builder().firstname("Aslan").lastname("Cemil").subname("koyun").build();
        Gender gender = Gender.DIV;
        Phonenumber phonenumber=Phonenumber.builder().serialnumber("454227893").areacode("678").countrycode("+43").build();
        Address address = Address.builder().place("Wien").street("Pernerstoffergasse 20").zipcode("1100").build();
        LocalDate birthDate=LocalDate.of(2004, Month.APRIL,9);

        Secretary secretary = Secretary.builder().name(name).gender(gender).phonenumber(phonenumber).title(title).address(address).birthDate(birthDate).build();

        var saved =secretaryRepository.save(secretary);

        var finding = secretaryRepository.findSecretaryByBirthDate(birthDate);

        assertThat(saved).isSameAs(secretary);
        assertThat(finding.get().getBirthDate()).isEqualTo(birthDate);
    }

    @Test
    void isSecretaryExistsByBirthDate(){
        Grade grade =Grade.MAG;
        SubjectGrade subjectGrade=SubjectGrade.MED_VET;
        Title title = Title.builder().grade(grade).subjectGrade(subjectGrade).build();
        Name name = Name.builder().firstname("Aslan").lastname("Cemil").subname("koyun").build();
        Gender gender = Gender.DIV;
        Phonenumber phonenumber=Phonenumber.builder().serialnumber("454227893").areacode("678").countrycode("+43").build();
        Address address = Address.builder().place("Wien").street("Pernerstoffergasse 20").zipcode("1100").build();
        LocalDate birthDate=LocalDate.of(2004, Month.APRIL,9);

        Secretary secretary = Secretary.builder().name(name).gender(gender).phonenumber(phonenumber).title(title).address(address).birthDate(birthDate).build();

        secretaryRepository.save(secretary);

        Boolean actualResult = secretaryRepository.existsByBirthDate(secretary.getBirthDate());
        assertThat(actualResult).isTrue();
    }

}
