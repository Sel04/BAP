package at.spengergasse.sj21224bhifaslantanprojectdoctor.services;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.*;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence.HospitalRepository;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.MutateHospitalCommand;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.exceptions.ServiceException;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.HospitalDto;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
@DataJpaTest
public class HospitalServiceTest {
    @Mock
    private HospitalRepository hospitalRepository;
    private HospitalService hospitalService;
    private TemporalValueFactory temporalValueFactory;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
        temporalValueFactory = new  TemporalValueFactory();
        hospitalService = new HospitalService(hospitalRepository,temporalValueFactory);
    }

    @Test
    void ensureCreatingHospitalWithExceptionHandlingIsRight(){
        Name selTan = Name.builder().firstname("Sel").subname("Ilyas").lastname("Tan").build();
        Phonenumber selTanPhoneNumber = Phonenumber.builder().areacode("+43").countrycode("664").serialnumber("918329081").build();
        Title title = Title.builder().grade(Grade.DR).subjectGrade(SubjectGrade.PHD).build();
        Address address = Address.builder().street("Pernerstorfergasse 60/3/18").zipcode("1100").place("Wien").build();


        Doctor doctor = Doctor.builder()
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

        Manager manager = Manager.builder()
                .name(selTan)
                .gender(Gender.MALE)
                .phonenumber(selTanPhoneNumber)
                .title(title)
                .birthDate(LocalDate.of(1990,3,20))
                .build();

        String hospitalname = "DoctorCenter";
        String abbreviation = "DC";

        Hospital hospital = Hospital.builder()
                .name(hospitalname)
                .abbreviation(abbreviation)
                .phoneNumber(selTanPhoneNumber)
                .address(address)
                .head(doctor)
                .manager(manager)
                .build();

        HospitalDto hospitaldto = new HospitalDto(hospital);

        Exception pEx = new PersistenceException("connection lost");
        when(hospitalRepository.findHospitalByAbbreviation(abbreviation)).thenReturn(Optional.empty());
        when(hospitalRepository.save(any())).thenThrow(pEx);

        var ex = assertThrows(ServiceException.class, ()-> hospitalService.createHospitalFromDto(hospitaldto));

        assertThat(ex).hasMessageContaining("Hospital")
                .hasMessageContaining(abbreviation,hospitalname,selTanPhoneNumber,address,doctor,manager)
                .hasMessageContaining("database problem")
                .hasRootCause(pEx);
    }

    @Test
    void ensureGetAllWorks(){
        Name selTan = Name.builder().firstname("Sel").subname("Ilyas").lastname("Tan").build();
        Phonenumber selTanPhoneNumber = Phonenumber.builder().areacode("+43").countrycode("664").serialnumber("918329081").build();
        Title title = Title.builder().grade(Grade.DR).subjectGrade(SubjectGrade.PHD).build();
        Address address = Address.builder().street("Pernerstorfergasse 60/3/18").zipcode("1100").place("Wien").build();


        Doctor doctor = Doctor.builder()
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

        Manager manager = Manager.builder()
                .name(selTan)
                .gender(Gender.MALE)
                .phonenumber(selTanPhoneNumber)
                .title(title)
                .birthDate(LocalDate.of(1990,3,20))
                .build();

        String hospitalname = "DoctorCenter";
        String abbreviation = "DC";

        Hospital hospital = Hospital.builder()
                .name(hospitalname)
                .abbreviation(abbreviation)
                .phoneNumber(selTanPhoneNumber)
                .address(address)
                .head(doctor)
                .manager(manager)
                .build();

        Hospital hospital1 = Hospital.builder()
                .name(hospitalname)
                .abbreviation(abbreviation)
                .phoneNumber(selTanPhoneNumber)
                .address(address)
                .head(doctor)
                .manager(manager)
                .build();

        List<Hospital> hospitals = Arrays.asList(hospital1,hospital);
        when(hospitalRepository.findAll()).thenReturn(hospitals);

        List<Hospital> expected = hospitalService.getHospitals();

        assertEquals(expected,hospitals);
        verify(hospitalRepository).findAll();
    }

    @Test
    void ensureUpdateWorksProperly(){
        Name selTan = Name.builder().firstname("Sel").subname("Ilyas").lastname("Tan").build();
        Phonenumber selTanPhoneNumber = Phonenumber.builder().areacode("+43").countrycode("664").serialnumber("918329081").build();
        Title title = Title.builder().grade(Grade.DR).subjectGrade(SubjectGrade.PHD).build();
        Address address = Address.builder().street("Pernerstorfergasse 60/3/18").zipcode("1100").place("Wien").build();


        Doctor doctor = Doctor.builder()
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

        Manager manager = Manager.builder()
                .name(selTan)
                .gender(Gender.MALE)
                .phonenumber(selTanPhoneNumber)
                .title(title)
                .birthDate(LocalDate.of(1990,3,20))
                .build();

        String hospitalname = "DoctorCenter";
        String abbreviation = "DC";

        Hospital hospital = Hospital.builder()
                .name(hospitalname)
                .abbreviation(abbreviation)
                .phoneNumber(selTanPhoneNumber)
                .address(address)
                .head(doctor)
                .manager(manager)
                .build();

        MutateHospitalCommand hospitalCommand = new MutateHospitalCommand(abbreviation,"hospitalname",selTanPhoneNumber,address,doctor,manager);
        when(hospitalRepository.findHospitalById(hospital.getId())).thenReturn(Optional.of(hospital));
        when(hospitalRepository.save(hospital)).thenReturn(hospital);

        Hospital replacedHospital = hospitalService.replaceHospital(hospital.getId(),hospitalCommand);

        Optional<Hospital> hospital1 = hospitalRepository.findHospitalById(hospital.getId());

        assertThat(replacedHospital).isEqualTo(hospital1.get());
    }

    @Test
    void ensurePartiallyUpdateWorksProperly(){
        Name selTan = Name.builder().firstname("Sel").subname("Ilyas").lastname("Tan").build();
        Phonenumber selTanPhoneNumber = Phonenumber.builder().areacode("+43").countrycode("664").serialnumber("918329081").build();
        Title title = Title.builder().grade(Grade.DR).subjectGrade(SubjectGrade.PHD).build();
        Address address = Address.builder().street("Pernerstorfergasse 60/3/18").zipcode("1100").place("Wien").build();


        Doctor doctor = Doctor.builder()
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

        Manager manager = Manager.builder()
                .name(selTan)
                .gender(Gender.MALE)
                .phonenumber(selTanPhoneNumber)
                .title(title)
                .birthDate(LocalDate.of(1990,3,20))
                .build();

        String hospitalname = "DoctorCenter";
        String abbreviation = "DC";

        Hospital hospital = Hospital.builder()
                .name(hospitalname)
                .abbreviation(abbreviation)
                .phoneNumber(selTanPhoneNumber)
                .address(address)
                .head(doctor)
                .manager(manager)
                .build();

        MutateHospitalCommand hospitalCommand = new MutateHospitalCommand(abbreviation,"hospitalname",selTanPhoneNumber,address,doctor,manager);
        when(hospitalRepository.findHospitalById(hospital.getId())).thenReturn(Optional.of(hospital));
        when(hospitalRepository.save(hospital)).thenReturn(hospital);

        Hospital replacedHospital = hospitalService.partiallyUpdate(hospital.getId(),hospitalCommand);

        Optional<Hospital> hospital1 = hospitalRepository.findHospitalById(hospital.getId());

        assertThat(replacedHospital).isEqualTo(hospital1.get());
    }

    @Test
    void ensureDeleteWorksProperly(){
        Name selTan = Name.builder().firstname("Sel").subname("Ilyas").lastname("Tan").build();
        Phonenumber selTanPhoneNumber = Phonenumber.builder().areacode("+43").countrycode("664").serialnumber("918329081").build();
        Title title = Title.builder().grade(Grade.DR).subjectGrade(SubjectGrade.PHD).build();
        Address address = Address.builder().street("Pernerstorfergasse 60/3/18").zipcode("1100").place("Wien").build();


        Doctor doctor = Doctor.builder()
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

        Manager manager = Manager.builder()
                .name(selTan)
                .gender(Gender.MALE)
                .phonenumber(selTanPhoneNumber)
                .title(title)
                .birthDate(LocalDate.of(1990,3,20))
                .build();

        String hospitalname = "DoctorCenter";
        String abbreviation = "DC";

        Hospital hospital = Hospital.builder()
                .name(hospitalname)
                .abbreviation(abbreviation)
                .phoneNumber(selTanPhoneNumber)
                .address(address)
                .head(doctor)
                .manager(manager)
                .build();
        when(hospitalRepository.findHospitalById(hospital.getId())).thenReturn(Optional.of(hospital));

        hospitalService.deleteHospital(hospital.getId());

        verify(hospitalRepository).findHospitalById(hospital.getId());
    }
}
