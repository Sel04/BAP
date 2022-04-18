package at.spengergasse.sj21224bhifaslantanprojectdoctor.services;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.*;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.foundation.TemporalValueFactory;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence.DepartmentRepository;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.DepartmentDto;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.MutateDepartmentCommand;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.exceptions.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.PersistenceException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DataJpaTest
public class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;
    private DepartmentService departmentService;
    private TemporalValueFactory temporalValueFactory;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        temporalValueFactory = new TemporalValueFactory();
        departmentService = new DepartmentService(departmentRepository,temporalValueFactory);
        /*
        departmentRepository = mock(DepartmentRepository.class);
        temporalValueFactory = mock(TemporalValueFactory.class);
        departmentService = new DepartmentService(departmentRepository,temporalValueFactory);
        */
    }
    @Test
    void ensureGetAllWorksProperly(){
        //given
        String name = "Department 1";
        String abbreviation = "D1";

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

        Department department = Department.builder().abbreviation(abbreviation).name(name).head(doctor1).build();
        Department department2 = Department.builder().abbreviation(abbreviation).name(name).head(doctor1).build();

        List<Department> departments = Arrays.asList(department,department2);
        when(departmentRepository.findAll()).thenReturn(departments);
        // when
        List<Department> expected = departmentService.getDepartments();

        //then
        assertEquals(expected,departments);
        verify(departmentRepository).findAll();


    }


    @Test
    void ensureCreartingDepartmentWithExceptionsHandlingIsRight(){
        String name = "Department 1";
        String abbreviation = "D1";

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

        Exception pEx = new PersistenceException("connection lost");
        when(departmentRepository.findDepartmentByName(name)).thenReturn(Optional.empty());
        when(departmentRepository.save(any())).thenThrow(pEx);

        Department department = Department.builder().abbreviation(abbreviation).name(name).head(doctor1).build();

        DepartmentDto departmentDto = new DepartmentDto(department);
        var ex = assertThrows(ServiceException.class, () -> departmentService.createDepartmentfromDto(departmentDto));

        assertThat(ex).hasMessageContaining("Department")
                    .hasMessageContaining(abbreviation,name,doctor1)
                    .hasMessageContaining("database problem")
                    .hasRootCause(pEx);

    }

    @Test
    void ensureUpdateWorksProperly(){
        //given
        String name = "Department 1";
        String abbreviation = "D1";

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

        String hospitalname = "DoctorCenter";

        Manager manager = Manager.builder()
                .name(selTan)
                .gender(Gender.MALE)
                .phonenumber(selTanPhoneNumber)
                .title(title)
                .birthDate(LocalDate.of(1990,3,20))
                .build();

        Hospital hospital = Hospital.builder()
                .name(hospitalname)
                .abbreviation(abbreviation)
                .phoneNumber(selTanPhoneNumber)
                .address(address)
                .head(doctor1)
                .manager(manager)
                .build();

        Department department = Department.builder().abbreviation(abbreviation).name(name).head(doctor1).hospital(hospital).build();

        MutateDepartmentCommand departmentCommand = new MutateDepartmentCommand(abbreviation,name,doctor1,hospital);
        when(departmentRepository.findDepartmentById(department.getId())).thenReturn(Optional.of(department));
        when(departmentRepository.save(department)).thenReturn(department);

        Department replacedDepartment = departmentService.replaceDepartment(department.getId(), departmentCommand);

        Optional<Department> department1 = departmentRepository.findDepartmentById(department.getId());
        //then
        assertThat(replacedDepartment).isEqualTo(department1.get());

    }

    @Test
    void ensurePartiallyUpdateWorksProperly(){
        Long id = 1234l;
        String name = "Department 1";
        String abbreviation = "D11";

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

        String hospitalname = "DoctorCenter";

        Manager manager = Manager.builder()
                .name(selTan)
                .gender(Gender.MALE)
                .phonenumber(selTanPhoneNumber)
                .title(title)
                .birthDate(LocalDate.of(1990,3,20))
                .build();

        Hospital hospital = Hospital.builder()
                .name(hospitalname)
                .abbreviation(abbreviation)
                .phoneNumber(selTanPhoneNumber)
                .address(address)
                .head(doctor1)
                .manager(manager)
                .build();

        Department department = Department.builder().abbreviation(abbreviation).name(name).head(doctor1).hospital(hospital).build();

        MutateDepartmentCommand departmentCommand = new MutateDepartmentCommand(abbreviation,name,doctor1,hospital);
        when(departmentRepository.findDepartmentById(department.getId())).thenReturn(Optional.of(department));
        when(departmentRepository.save(department)).thenReturn(department);

        Department partiallyUpdateDepartment = departmentService.partiallyUpdate(department.getId(), departmentCommand);

        Optional<Department> departments = departmentRepository.findDepartmentById(department.getId());
        Department department1 = departments.get();

        assertThat(partiallyUpdateDepartment).isEqualTo(department1);
    }

    @Test
    void ensureDeleteWorksProperly(){
        Long id = 1234l;
        String name = "Department 1";
        String abbreviation = "D11";

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

        String hospitalname = "DoctorCenter";

        Manager manager = Manager.builder()
                .name(selTan)
                .gender(Gender.MALE)
                .phonenumber(selTanPhoneNumber)
                .title(title)
                .birthDate(LocalDate.of(1990,3,20))
                .build();

        Hospital hospital = Hospital.builder()
                .name(hospitalname)
                .abbreviation(abbreviation)
                .phoneNumber(selTanPhoneNumber)
                .address(address)
                .head(doctor1)
                .manager(manager)
                .build();

        Department department = Department.builder().abbreviation(abbreviation).name(name).head(doctor1).hospital(hospital).build();
        when(departmentRepository.findDepartmentById(department.getId())).thenReturn(Optional.of(department));

        departmentService.deleteDepartment(department.getId());

        verify(departmentRepository).findDepartmentById(department.getId());

    }
}
