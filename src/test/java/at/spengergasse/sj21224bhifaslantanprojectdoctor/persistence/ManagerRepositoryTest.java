package at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ManagerRepositoryTest {
    @Autowired
    private ManagerRepository managerRepository;

    @Test
    void ensureSavingWorksProperly(){
        Name selTan = Name.builder().firstname("Sel").subname("Ilyas").lastname("Tan").build();
        Phonenumber selTanPhoneNumber = Phonenumber.builder().areacode("+43").countrycode("664").serialnumber("918329081").build();
        Title title = Title.builder().grade(Grade.DR).subjectGrade(SubjectGrade.PHD).build();
        Address address = Address.builder().street("Pernerstorfergasse 60/3/18").zipcode("1100").place("Wien").build();
        Gender gender = Gender.MALE;
        LocalDate date = LocalDate.of(1990,3,20);



        Manager manager = Manager.builder()
                .name(selTan)
                .gender(gender)
                .phonenumber(selTanPhoneNumber)
                .title(title)
                .birthDate(date)
                .build();

        Ordination ordination= Ordination.builder().abbrevitation("ZA").name("Zahnarzt").address(address).manager(manager).build();

        manager.setOrdination(ordination);


        var saved = managerRepository.save(manager);
        assertThat(saved).isSameAs(manager);
        assertThat(saved.getId()).isNotNull();
    }
}
