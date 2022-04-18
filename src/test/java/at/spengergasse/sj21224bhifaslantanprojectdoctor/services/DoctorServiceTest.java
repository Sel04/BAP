package at.spengergasse.sj21224bhifaslantanprojectdoctor.services;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.*;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence.DoctorRepository;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.DoctorDto;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.MutateDoctorCommand;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.exceptions.ServiceException;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.foundation.TemporalValueFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
public class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;
    private TemporalValueFactory temporalValueFactory;
    private DoctorService doctorService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
        temporalValueFactory = new TemporalValueFactory();
        doctorService = new DoctorService(doctorRepository, temporalValueFactory);
    }

    @Test
    void ensureCreatingDoctorWithExceptionHandlingIsRight(){
        Name selTan = Name.builder().firstname("Sel").subname("Ilyas").lastname("Tan").build();
        Phonenumber selTanPhoneNumber = Phonenumber.builder().areacode("+43").countrycode("664").serialnumber("918329081").build();
        Title title = Title.builder().grade(Grade.DR).subjectGrade(SubjectGrade.PHD).build();
        Address address = Address.builder().street("Pernerstorfergasse 60/3/18").zipcode("1100").place("Wien").build();
        LocalDate date1 = LocalDate.of(1990,3,20);
        String abbreviation = "ST";
        Gender gender = Gender.MALE;
        String aerztekammerId = "DO1";
        Subject subject = Subject.ALLGEMEINMEDIZIN;
        Position position = Position.HEAD_OF_DEPARTMENT;
        Double salary = 2000.00;

        Doctor doctor1 = Doctor.builder()
                .name(selTan)
                .gender(gender)
                .phonenumber(selTanPhoneNumber)
                .title(title)
                .birthDate(date1)
                .created(temporalValueFactory.created_at())
                .abbreviation(abbreviation)
                .aerztekammerId("DO1")
                .subject(subject)
                .position(position)
                .salary(salary)
                .build();

        DoctorDto doctorDto = new DoctorDto(doctor1);

        Exception pEx = new PersistenceException("connection lost");
        when(doctorRepository.findDoctorByAbbreviation(doctor1.getAbbreviation())).thenReturn(Optional.empty());
        when(doctorRepository.save(any())).thenThrow(pEx);

        var ex = assertThrows(ServiceException.class, () -> doctorService.createDoctorFromDto(doctorDto));

        assertThat(ex).hasMessageContaining("Doctor")
                .hasMessageContaining(abbreviation,selTan,gender,selTanPhoneNumber,title,date1,address,aerztekammerId,subject,position,salary)
                .hasMessageContaining("database problem")
                .hasRootCause(pEx);
    }

    @Test
    void ensureGetAllWorksProperly(){
        Name selTan = Name.builder().firstname("Sel").subname("Ilyas").lastname("Tan").build();
        Phonenumber selTanPhoneNumber = Phonenumber.builder().areacode("+43").countrycode("664").serialnumber("918329081").build();
        Title title = Title.builder().grade(Grade.DR).subjectGrade(SubjectGrade.PHD).build();
        Address address = Address.builder().street("Pernerstorfergasse 60/3/18").zipcode("1100").place("Wien").build();
        LocalDate date1 = LocalDate.of(1990,3,20);
        String abbreviation = "ST";
        Gender gender = Gender.MALE;
        String aerztekammerId = "DO1";
        Subject subject = Subject.ALLGEMEINMEDIZIN;
        Position position = Position.HEAD_OF_DEPARTMENT;
        Double salary = 2000.00;

        Doctor doctor1 = Doctor.builder()
                .name(selTan)
                .gender(gender)
                .phonenumber(selTanPhoneNumber)
                .title(title)
                .birthDate(date1)
                .created(temporalValueFactory.created_at())
                .abbreviation(abbreviation)
                .aerztekammerId("DO1")
                .subject(subject)
                .position(position)
                .salary(salary)
                .build();

        Doctor doctor2 = Doctor.builder()
                .name(selTan)
                .gender(gender)
                .phonenumber(selTanPhoneNumber)
                .title(title)
                .birthDate(date1)
                .created(temporalValueFactory.created_at())
                .abbreviation(abbreviation)
                .aerztekammerId("DO1")
                .subject(subject)
                .position(position)
                .salary(salary)
                .build();

        List<Doctor> doctors = Arrays.asList(doctor1,doctor2);
        when(doctorRepository.findAll()).thenReturn(doctors);

        List<Doctor> expected = doctorService.getDoctors();

        assertEquals(expected,doctors);
        verify(doctorRepository).findAll();
    }

    @Test
    void ensureUpdateWorksProperly(){
        Name selTan = Name.builder().firstname("Sel").subname("Ilyas").lastname("Tan").build();
        Phonenumber selTanPhoneNumber = Phonenumber.builder().areacode("+43").countrycode("664").serialnumber("918329081").build();
        Title title = Title.builder().grade(Grade.DR).subjectGrade(SubjectGrade.PHD).build();
        Address address = Address.builder().street("Pernerstorfergasse 60/3/18").zipcode("1100").place("Wien").build();
        LocalDate date1 = LocalDate.of(1990,3,20);
        String abbreviation = "ST";
        Gender gender = Gender.MALE;
        String aerztekammerId = "DO1";
        Subject subject = Subject.ALLGEMEINMEDIZIN;
        Position position = Position.HEAD_OF_DEPARTMENT;
        Double salary = 2000.00;

        Doctor doctor1 = Doctor.builder()
                .name(selTan)
                .gender(gender)
                .phonenumber(selTanPhoneNumber)
                .title(title)
                .birthDate(date1)
                .created(temporalValueFactory.created_at())
                .abbreviation(abbreviation)
                .aerztekammerId("DO1")
                .subject(subject)
                .position(position)
                .salary(salary)
                .build();

        Name cemil = Name.builder().firstname("Cemil").subname("Ilyas").lastname("Aslan").build();


        MutateDoctorCommand mutateDoctorCommand = new MutateDoctorCommand(cemil,gender,selTanPhoneNumber,title,address,date1,abbreviation,aerztekammerId,subject,position,salary,null);
        when(doctorRepository.findDoctorById(doctor1.getId())).thenReturn(Optional.of(doctor1));
        when(doctorRepository.save(doctor1)).thenReturn(doctor1);

        Doctor replacedDoctor = doctorService.replaceDoctor(doctor1.getId(),mutateDoctorCommand);

        Optional<Doctor> doctor = doctorRepository.findDoctorById(doctor1.getId());

        assertThat(replacedDoctor).isEqualTo(doctor.get());

    }


    @Test
    void ensurePartiallyUpdateWorksProperly(){
        Name selTan = Name.builder().firstname("Sel").subname("Ilyas").lastname("Tan").build();
        Phonenumber selTanPhoneNumber = Phonenumber.builder().areacode("+43").countrycode("664").serialnumber("918329081").build();
        Title title = Title.builder().grade(Grade.DR).subjectGrade(SubjectGrade.PHD).build();
        Address address = Address.builder().street("Pernerstorfergasse 60/3/18").zipcode("1100").place("Wien").build();
        LocalDate date1 = LocalDate.of(1990,3,20);
        String abbreviation = "ST";
        Gender gender = Gender.MALE;
        String aerztekammerId = "DO1";
        Subject subject = Subject.ALLGEMEINMEDIZIN;
        Position position = Position.HEAD_OF_DEPARTMENT;
        Double salary = 2000.00;

        Doctor doctor1 = Doctor.builder()
                .name(selTan)
                .gender(gender)
                .phonenumber(selTanPhoneNumber)
                .title(title)
                .birthDate(date1)
                .created(temporalValueFactory.created_at())
                .abbreviation(abbreviation)
                .aerztekammerId("DO1")
                .subject(subject)
                .position(position)
                .salary(salary)
                .build();

        Name cemil = Name.builder().firstname("Cemil").subname("Ilyas").lastname("Aslan").build();


        MutateDoctorCommand mutateDoctorCommand = new MutateDoctorCommand(cemil,gender,selTanPhoneNumber,title,address,date1,abbreviation,aerztekammerId,subject,position,salary,null);
        when(doctorRepository.findDoctorById(doctor1.getId())).thenReturn(Optional.of(doctor1));
        when(doctorRepository.save(doctor1)).thenReturn(doctor1);

        Doctor replacedDoctor = doctorService.partiallyUpdate(doctor1.getId(),mutateDoctorCommand);

        Optional<Doctor> doctor = doctorRepository.findDoctorById(doctor1.getId());

        assertThat(replacedDoctor).isEqualTo(doctor.get());

    }

    @Test
    void ensureDeleteWorksProperly(){
        Name selTan = Name.builder().firstname("Sel").subname("Ilyas").lastname("Tan").build();
        Phonenumber selTanPhoneNumber = Phonenumber.builder().areacode("+43").countrycode("664").serialnumber("918329081").build();
        Title title = Title.builder().grade(Grade.DR).subjectGrade(SubjectGrade.PHD).build();
        Address address = Address.builder().street("Pernerstorfergasse 60/3/18").zipcode("1100").place("Wien").build();
        LocalDate date1 = LocalDate.of(1990,3,20);
        String abbreviation = "ST";
        Gender gender = Gender.MALE;
        String aerztekammerId = "DO1";
        Subject subject = Subject.ALLGEMEINMEDIZIN;
        Position position = Position.HEAD_OF_DEPARTMENT;
        Double salary = 2000.00;

        Doctor doctor1 = Doctor.builder()
                .name(selTan)
                .gender(gender)
                .phonenumber(selTanPhoneNumber)
                .title(title)
                .birthDate(date1)
                .created(temporalValueFactory.created_at())
                .abbreviation(abbreviation)
                .aerztekammerId("DO1")
                .subject(subject)
                .position(position)
                .salary(salary)
                .build();
        when(doctorRepository.findDoctorById(doctor1.getId())).thenReturn(Optional.of(doctor1));

        doctorService.deleteDoctor(doctor1.getId());

        verify(doctorRepository).findDoctorById(doctor1.getId());


    }
}