package at.spengergasse.sj21224bhifaslantanprojectdoctor.services;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.*;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence.DoctorRepository;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.DoctorDto;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.MutateDoctorCommand;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.exceptions.ServiceException;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.foundation.TemporalValueFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = false)
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final TemporalValueFactory temporalValueFactory;

    private String CANNOT_BE_NULL = "cannot be null!";
    private String CANNOT_BE_EMPTY_OR_BLANK = "cannot be empty or blank!";
    private String CANNOT_BE_BLANK = "cannot be blank!";
    private String CANNOT_BE_NULL_EMPTY_OR_BLANK = "cannot be null, empty or blank!";

    public Doctor createDoctor(MutateDoctorCommand doctor){
        Doctor newDoctor = null;
        LocalDateTime created = temporalValueFactory.created_at();

        /*if(doctor.getName()== null){
            log.error("Name " + CANNOT_BE_NULL);
            throw new IllegalArgumentException("Name " + CANNOT_BE_NULL);
        }
        else if(doctor.getName().getFirstname().isBlank() || doctor.getName().getFirstname().isEmpty()){
            log.error("Firstname " + CANNOT_BE_EMPTY_OR_BLANK);
            throw new IllegalArgumentException("Firstname " + CANNOT_BE_EMPTY_OR_BLANK);
        }
        else if(doctor.getName().getLastname().isBlank() || doctor.getName().getLastname().isEmpty()){
            log.error("Lastname " + CANNOT_BE_EMPTY_OR_BLANK);
            throw new IllegalArgumentException("Lastname " + CANNOT_BE_EMPTY_OR_BLANK);
        }
//        else if(doctor.getName().getSubname().isBlank() ){
//            throw new IllegalArgumentException("Subname " + CANNOT_BE_BLANK);
//        }
        else if(doctor.getGender() == null){
            log.error("Gender " + CANNOT_BE_NULL);
            throw new IllegalArgumentException("Gender " + CANNOT_BE_NULL);
        }
        else if(doctor.getPhonenumber()== null){
            log.error("Phonenumeber " +CANNOT_BE_NULL);
            throw new IllegalArgumentException("Phonenumber " + CANNOT_BE_NULL);
        }
        // Phonenumber String?

        else if(doctor.getAddress() == null){
            throw new IllegalArgumentException("Address " + CANNOT_BE_NULL);
        }
        else if(doctor.getAddress().getPlace().isBlank() || doctor.getAddress().getPlace().isEmpty()){
            throw new IllegalArgumentException("Place of address" + CANNOT_BE_EMPTY_OR_BLANK);
        }
        else if(doctor.getAddress().getStreet().isBlank() || doctor.getAddress().getStreet().isEmpty()) {
            throw new IllegalArgumentException("Street of address " + CANNOT_BE_EMPTY_OR_BLANK);
        }
        else if(doctor.getTitle() == null){
            throw new IllegalArgumentException("Title " + CANNOT_BE_NULL);
        }
        else if(doctor.getBirthDate() == null){
            throw new IllegalArgumentException("Birthdate " + CANNOT_BE_NULL);
        }
        else if (doctor.getBirthDate().isAfter(LocalDate.now())){
            throw new IllegalArgumentException("Birthdate cannot be in the future!");
        }

        else if(doctor.getAbbreviation() == null ||  doctor.getAbbreviation().isEmpty() || doctor.getAbbreviation().isBlank()){
            throw new IllegalArgumentException("Abbreviation " + CANNOT_BE_NULL_EMPTY_OR_BLANK);
        }
        else if(doctor.getAerztekammerId() == null || doctor.getAerztekammerId().isEmpty() || doctor.getAerztekammerId().isBlank()){
            throw new IllegalArgumentException("Aerztekammer Id " + CANNOT_BE_NULL_EMPTY_OR_BLANK);
        }
        else if(doctor.getPosition() == null){
            throw new IllegalArgumentException("Position " + CANNOT_BE_NULL);
        }
        else if(doctor.getSubject() == null){
            throw new IllegalArgumentException("Subject " + CANNOT_BE_NULL);
        }
         */

        try {
            var doctor1 = doctorRepository.findDoctorByAbbreviation(doctor.getAbbreviation());
            if(doctor1.isPresent()){
                return doctor1.get();
            }
             newDoctor = Doctor.builder().abbreviation(doctor.getAbbreviation())
                                         .address(doctor.getAddress())
                                         .aerztekammerId(doctor.getAerztekammerId())
                                         .birthDate(doctor.getBirthDate())
                                         .name(doctor.getName())
                                         .created(created)
                                         .gender(doctor.getGender())
                                         .position(doctor.getPosition())
                                         .phonenumber(doctor.getPhonenumber())
                                         .salary(doctor.getSalary())
                                         .subject(doctor.getSubject())
                                         .title(doctor.getTitle())
                                         .build();
            log.info("Doctor {} " + newDoctor.getDoctor() + "created " + created);
            return doctorRepository.save(newDoctor);
        }
        catch(PersistenceException pEx){
            log.error("Cannot create Doctor " + newDoctor.getDoctor());
            throw ServiceException.cannotCreateEntity(newDoctor, pEx);
        }
    }

    @Transactional
    public Doctor createDoctorFromDto(DoctorDto doctor){
        Doctor newDoctor = null;
        LocalDateTime created = temporalValueFactory.created_at();

        /*if(doctor.name()== null){
            throw new IllegalArgumentException("Name " + CANNOT_BE_NULL);
        }
        else if(doctor.name().getFirstname().isBlank() || doctor.name().getFirstname().isEmpty()){
            throw new IllegalArgumentException("Firstname " + CANNOT_BE_EMPTY_OR_BLANK);
        }
        else if(doctor.name().getLastname().isBlank() || doctor.name().getLastname().isEmpty()){
            throw new IllegalArgumentException("Lastname " + CANNOT_BE_EMPTY_OR_BLANK);
        }
//        else if(doctor.getName().getSubname().isBlank() ){
//            throw new IllegalArgumentException("Subname " + CANNOT_BE_BLANK);
//        }
        else if(doctor.gender() == null){
            throw new IllegalArgumentException("Gender " + CANNOT_BE_NULL);
        }
        else if(doctor.phonenumber()== null){
            throw new IllegalArgumentException("Phonenumber " + CANNOT_BE_NULL);
        }
        // Phonenumber String?

        else if(doctor.address() == null){
            throw new IllegalArgumentException("Address " + CANNOT_BE_NULL);
        }
        else if(doctor.address().getPlace().isBlank() || doctor.address().getPlace().isEmpty()){
            throw new IllegalArgumentException("Place of address" + CANNOT_BE_EMPTY_OR_BLANK);
        }
        else if(doctor.address().getStreet().isBlank() || doctor.address().getStreet().isEmpty()) {
            throw new IllegalArgumentException("Street of address " + CANNOT_BE_EMPTY_OR_BLANK);
        }
        else if(doctor.title() == null){
            throw new IllegalArgumentException("Title " + CANNOT_BE_NULL);
        }
        else if(doctor.date() == null){
            throw new IllegalArgumentException("Birthdate " + CANNOT_BE_NULL);
        }
        else if (doctor.date().isAfter(LocalDate.now())){
            throw new IllegalArgumentException("Birthdate cannot be in the future!");
        }
        else if(doctor.abbreviation() == null ||  doctor.abbreviation().isEmpty() || doctor.abbreviation().isBlank()){
            throw new IllegalArgumentException("Abbreviation " + CANNOT_BE_NULL_EMPTY_OR_BLANK);
        }
        else if(doctor.aerztekammerId() == null || doctor.aerztekammerId().isEmpty() || doctor.aerztekammerId().isBlank()){
            throw new IllegalArgumentException("Aerztekammer Id " + CANNOT_BE_NULL_EMPTY_OR_BLANK);
        }
        else if(doctor.position() == null){
            throw new IllegalArgumentException("Position " + CANNOT_BE_NULL);
        }
        else if(doctor.subject() == null){
            throw new IllegalArgumentException("Subject " + CANNOT_BE_NULL);
        }
         */
        try {
            var doctor1 = doctorRepository.findDoctorByAbbreviation(doctor.abbreviation());
            if(doctor1.isPresent()){
                return doctor1.get();
            }
            newDoctor = Doctor.builder().abbreviation(doctor.abbreviation())
                    .address(doctor.address())
                    .aerztekammerId(doctor.aerztekammerId())
                    .birthDate(doctor.date())
                    .created(created)
                    .name(doctor.name())
                    .gender(doctor.gender())
                    .position(doctor.position())
                    .phonenumber(doctor.phonenumber())
                    .salary(doctor.salary())
                    .subject(doctor.subject())
                    .title(doctor.title())
                    .build();
            log.info("Doctor {} " + newDoctor.getDoctor() + "created " + created);
            return doctorRepository.save(newDoctor);
        }
        catch(PersistenceException pEx){
            log.error("Cannot create Doctor " + newDoctor.getDoctor());
            throw ServiceException.cannotCreateEntity(newDoctor, pEx);
        }
    }

    public void  deleteDoctor(Long id){
        Doctor doctor= null;
        Optional<Doctor> entity = doctorRepository.findDoctorById(id);
        try{
            if(entity.isPresent()){
                doctor = entity.get();
                doctorRepository.delete(doctor);
                log.info("Doctor {} " + doctor.getDepartment() + " deleted: " + LocalDateTime.now());
            }
        }catch(PersistenceException pe){
            log.error("Cannot delete Doctor " + doctor.getDoctor());
            throw ServiceException.cannotDeleteEntity(doctor, pe);
        }
    }

    public List<Doctor> getDoctors(){
        var doctor = doctorRepository.findAll();
        log.info("Found doctors ", doctor.size());
        return doctor;
    }
    public void deleteDoctors(){
        doctorRepository.deleteAll();

    }
    public Optional<Doctor> getDoctorByAbbreviation(String abbreviation){
        return doctorRepository.findDoctorByAbbreviation(abbreviation);
    }
    public  Optional<Doctor> getDoctorById(Long id){
        return doctorRepository.findDoctorById(id);
    }
    public Doctor replaceDoctor(Long id, MutateDoctorCommand mutateDoctorCommand){
        Optional<Doctor> entity = doctorRepository.findDoctorById(id);
        LocalDateTime replace = temporalValueFactory.replaced_at();
        Doctor doctor =  entity.get();;
        try {
           // if (entity.isPresent()) {
                //doctor = entity.get();
                doctor.setName(mutateDoctorCommand.getName());
                doctor.setBirthDate(mutateDoctorCommand.getBirthDate());
                doctor.setAbbreviation(mutateDoctorCommand.getAbbreviation());
                doctor.setPosition(mutateDoctorCommand.getPosition());
                doctor.setAerztekammerId(mutateDoctorCommand.getAerztekammerId());
                doctor.setDepartment(mutateDoctorCommand.getDepartment());
               // doctor.setPatientList(mutateDoctorCommand.getPatientList());
                doctor.setUpdated(replace);
                doctor.setSalary(mutateDoctorCommand.getSalary());
                doctor.setSubject(mutateDoctorCommand.getSubject());
                doctor.setAddress(mutateDoctorCommand.getAddress());
                doctor.setGender(mutateDoctorCommand.getGender());
                doctor.setPhonenumber(mutateDoctorCommand.getPhonenumber());
                doctor.setTitle(mutateDoctorCommand.getTitle());


            //}
            log.info("Doctor "  + doctor.getDoctor() + "replaced at: " + replace );
            return doctorRepository.save(doctor);
        }catch(PersistenceException ex) {
            log.error("Cannot update Doctor: " + doctor.getDoctor());
            throw ServiceException.cannotUpdateEntity(doctor,ex);
        }
    }

    public Doctor partiallyUpdate(Long id,MutateDoctorCommand mutateDoctorCommand){
        Optional<Doctor> doctors = doctorRepository.findDoctorById(id);
        LocalDateTime replace = temporalValueFactory.replaced_at();

        if(doctors.isPresent()){
            Doctor doctor = doctors.get();
            if(mutateDoctorCommand.getName() != null){
                doctor.setName(mutateDoctorCommand.getName());
            }
            if(mutateDoctorCommand.getAddress() != null){
                doctor.setAddress(mutateDoctorCommand.getAddress());
            }
            if(mutateDoctorCommand.getAbbreviation() != null){
                doctor.setAbbreviation(mutateDoctorCommand.getAbbreviation());
            }
            if(mutateDoctorCommand.getAerztekammerId() != null){
                doctor.setAbbreviation(mutateDoctorCommand.getAerztekammerId());
            }
            if(mutateDoctorCommand.getGender() != null){
                doctor.setGender(mutateDoctorCommand.getGender());
            }
            if(mutateDoctorCommand.getBirthDate() != null){
                doctor.setBirthDate(mutateDoctorCommand.getBirthDate());
            }
            if(mutateDoctorCommand.getTitle() != null){
                doctor.setTitle(mutateDoctorCommand.getTitle());
            }
            if(mutateDoctorCommand.getPhonenumber() != null){
                doctor.setPhonenumber(mutateDoctorCommand.getPhonenumber());
            }
            if(mutateDoctorCommand.getSalary() != 0){
                doctor.setSalary(mutateDoctorCommand.getSalary());
            }
            doctor.setUpdated(replace);
            log.info("Doctor " + doctor.getDoctor() + " upated: " + replace);
            return doctorRepository.save(doctor);
        }
        return doctors.get();
    }


}
