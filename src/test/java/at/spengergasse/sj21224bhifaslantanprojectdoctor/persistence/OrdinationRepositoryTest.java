package at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.*;
import com.mysema.commons.lang.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class OrdinationRepositoryTest {

    @Autowired
    private OrdinationRepository repository;

    @Autowired
    private ManagerRepository managerRepository;

    @Test
    void ensureSaveWorksProbably(){
        Grade grade =Grade.MAG;
        SubjectGrade subjectGrade=SubjectGrade.MED_VET;
        Title title = Title.builder().grade(grade).subjectGrade(subjectGrade).build();
        Name name = Name.builder().firstname("Max").lastname("Mustermann").subname("Maxi").build();
        Gender gender = Gender.MALE;
        Phonenumber phonenumber=Phonenumber.builder().serialnumber("781919").areacode("660").countrycode("+43").build();
        Address address = Address.builder().place("Wien").street("Spengergasse 20").zipcode("1050").build();
        Address addresse2 = Address.builder().place("Wien").street("Afritschagassen 129").zipcode("1220").build();

        Manager manager = Manager.builder().name(name).gender(gender).phonenumber(phonenumber).title(title).address(address).birthDate(LocalDate.of(2009, Month.APRIL,19)).build();
        var saved2=managerRepository.save(manager);

        Ordination ordination= Ordination.builder().abbrevitation("ZA").name("Zahnarzt").address(address).manager(manager).build();

        var saved = repository.save(ordination);

        assertThat(saved).isSameAs(ordination);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getAbbrevitation()).isSameAs(ordination.getAbbrevitation());
    }

    @Test
    void ensureDeletingWorksProbably(){
        Grade grade =Grade.MAG;
        SubjectGrade subjectGrade=SubjectGrade.MED_VET;
        Title title = Title.builder().grade(grade).subjectGrade(subjectGrade).build();
        Name name = Name.builder().firstname("Max").lastname("Mustermann").subname("Maxi").build();
        Gender gender = Gender.MALE;
        Phonenumber phonenumber=Phonenumber.builder().serialnumber("781919").areacode("660").countrycode("+43").build();
        Address address = Address.builder().place("Wien").street("Spengergasse 20").zipcode("1050").build();
        Address addresse2 = Address.builder().place("Wien").street("Afritschagassen 129").zipcode("1220").build();

        Manager manager = Manager.builder().name(name).gender(gender).phonenumber(phonenumber).title(title).address(address).birthDate(LocalDate.of(2009, Month.APRIL,19)).build();

        Ordination ordination= Ordination.builder().abbrevitation("ZA").name("Zahnarzt").address(address).manager(manager).build();

        var saved = repository.save(ordination);
        repository.delete(ordination);

        assertThat(repository.count()).isEqualTo(0);
    }

    @Test
    void ensureFindingOrdinationByAbbreviationWorksProbably(){
        Grade grade =Grade.MAG;
        SubjectGrade subjectGrade=SubjectGrade.MED_VET;
        Title title = Title.builder().grade(grade).subjectGrade(subjectGrade).build();
        Name name = Name.builder().firstname("Max").lastname("Mustermann").subname("Maxi").build();
        Gender gender = Gender.MALE;
        Phonenumber phonenumber=Phonenumber.builder().serialnumber("781919").areacode("660").countrycode("+43").build();
        Address address = Address.builder().place("Wien").street("Spengergasse 20").zipcode("1050").build();
        Address addresse2 = Address.builder().place("Wien").street("Afritschagassen 129").zipcode("1220").build();

        String abbreviation ="ZA";
        String nameAb ="Zahnarzt";

        Manager manager = Manager.builder().name(name).gender(gender).phonenumber(phonenumber).title(title).address(address).birthDate(LocalDate.of(2009, Month.APRIL,19)).build();
        var saved2 = managerRepository.save(manager);
        Ordination ordination= Ordination.builder().abbrevitation(abbreviation).name(nameAb).address(address).manager(manager).build();

        var saved = repository.save(ordination);

        var finding = repository.findOrdinationByAbbrevitation(abbreviation);

        assertThat(saved).isSameAs(ordination);
        assertThat(finding.get().getAbbrevitation()).isEqualTo(abbreviation);
    }

    @Test
    void isOrdinationExistsById(){
        Grade grade =Grade.MAG;
        SubjectGrade subjectGrade=SubjectGrade.MED_VET;
        Title title = Title.builder().grade(grade).subjectGrade(subjectGrade).build();
        Name name = Name.builder().firstname("Max").lastname("Mustermann").subname("Maxi").build();
        Gender gender = Gender.MALE;
        Phonenumber phonenumber=Phonenumber.builder().serialnumber("781919").areacode("660").countrycode("+43").build();
        Address address = Address.builder().place("Wien").street("Spengergasse 20").zipcode("1050").build();
        Address addresse2 = Address.builder().place("Wien").street("Afritschagassen 129").zipcode("1220").build();

        String abbreviation ="ZA";
        String nameAb ="Zahnarzt";

        Manager manager = Manager.builder().name(name).gender(gender).phonenumber(phonenumber).title(title).address(address).birthDate(LocalDate.of(2009, Month.APRIL,19)).build();
        managerRepository.save(manager);
        Ordination ordination= Ordination.builder().abbrevitation(abbreviation).name(nameAb).address(address).manager(manager).build();
        repository.save(ordination);

        Boolean actualResult = repository.existsOrdinationById(ordination.getAbbrevitation());
        assertThat(actualResult).isTrue();


    }
}
