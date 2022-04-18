package at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DepartmentRepositoryTest {
    @Autowired
    private DepartmentRepository repository;

    @Test
    void ensureSavingWorksProperly(){
        //given

        Name selTan = Name.builder().firstname("Sel").subname("Ilyas").lastname("Tan").build();
        Phonenumber selTanPhoneNumber = Phonenumber.builder().areacode("+43").countrycode("664").serialnumber("918329081").build();
        Title title = Title.builder().grade(Grade.DR).subjectGrade(SubjectGrade.PHD).build();
        Address address = Address.builder().street("Pernerstorfergasse 60/3/18").zipcode("1100").place("Wien").build();

        Doctor head = Doctor.builder()
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


        Department department = Department.builder()
                .name("Casualty department")
                .abbreviation("CD")
                .head(head)
                .build();

        //When
        var saved = repository.save(department);
        assertThat(saved).isSameAs(department);
        assertThat(saved.getId()).isNotNull();

    }
}
