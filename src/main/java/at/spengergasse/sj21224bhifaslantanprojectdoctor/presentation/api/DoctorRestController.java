package at.spengergasse.sj21224bhifaslantanprojectdoctor.presentation.api;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Doctor;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.DoctorService;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.DoctorDto;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.MutateDoctorCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor

@RestController
@RequestMapping(DoctorRestController.BASE_URL)
public class DoctorRestController {
    public static final String BASE_URL = "/api/doctors";

    private final DoctorService doctorService;

    @GetMapping({"","/"})
    public HttpEntity<List<DoctorDto>> getDoctor(){
        List<Doctor> doctors = doctorService.getDoctors();

        return (doctors.isEmpty())
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(doctors.stream().map(DoctorDto::new).toList());
    }

    @GetMapping({"{abbreviation}"})
    public HttpEntity<DoctorDto> getDoctorbyAbbreviation(@PathVariable String abbreviation){
        return doctorService.getDoctorByAbbreviation(abbreviation)
                        .map(DoctorDto::new)
                        .map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping({"/{id}"})
    public HttpEntity<DoctorDto> getDoctorbyId(@PathVariable Long id){
        return doctorService.getDoctorById(id)
                .map(DoctorDto::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping({"","/"})
    public HttpEntity<DoctorDto> createDoctor(@Valid @RequestBody MutateDoctorCommand doctor){
        Doctor newDoctor = doctorService.createDoctor(doctor);
        URI self = createSelfLink(newDoctor);
        return ResponseEntity.created(self).body(new DoctorDto(newDoctor));
    }

    private URI createSelfLink(Doctor doctor){
        URI self = UriComponentsBuilder.fromPath("/{id}")
                            .uriVariables(Map.of("id",doctor.getId()))
                            .build().toUri();
        return self;
    }

    @PutMapping("/{id}")
    public HttpEntity<DoctorDto> replaceDoctor(@Valid @PathVariable Long id,@RequestBody MutateDoctorCommand mutateDoctorCommand){
        return ResponseEntity.ok(new DoctorDto(doctorService.replaceDoctor(id,mutateDoctorCommand)));
    }

    @DeleteMapping({"/"})
    public HttpEntity<Void> deleteDoctors(){
        doctorService.deleteDoctors();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping({"/{id}"})
    public HttpEntity<Void> deleteDoctor(@PathVariable Long id, @RequestBody MutateDoctorCommand mutateDoctorCommand){
        doctorService.deleteDoctor(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping({"/{id}"})
    public HttpEntity<DoctorDto> partiallyUpdateDoctor(@PathVariable Long id, @RequestBody MutateDoctorCommand mutateDoctorCommand){
        return  ResponseEntity.ok(new DoctorDto(doctorService.partiallyUpdate(id,mutateDoctorCommand)));
    }





}
