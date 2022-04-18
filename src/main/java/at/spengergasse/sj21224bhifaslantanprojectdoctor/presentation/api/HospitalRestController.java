package at.spengergasse.sj21224bhifaslantanprojectdoctor.presentation.api;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Hospital;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.HospitalService;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.HospitalDto;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.MutateHospitalCommand;
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
@RequestMapping(HospitalRestController.BASE_URL)
public class HospitalRestController {
    public static final String BASE_URL = "/api/hospital";

    private final HospitalService hospitalService;

    @GetMapping({"","/"})
    public HttpEntity<List<HospitalDto>> getHospitals(){
        List<Hospital> hospitals = hospitalService.getHospitals();

        return (hospitals.isEmpty())
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(hospitals.stream().map(HospitalDto::new).toList());
    }

    @GetMapping({"{/id}"})
    public HttpEntity<HospitalDto> getHospitalbyId(@PathVariable Long id){
        return hospitalService.getHospitalById(id)
                .map(HospitalDto::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping({"","/"})
    public HttpEntity<HospitalDto> createHospital(@Valid @RequestBody MutateHospitalCommand hospital){
        Hospital newHospital = hospitalService.createHospital(hospital);
        URI self = createSelfLink(newHospital);
        return ResponseEntity.created(self).body(new HospitalDto(newHospital));
    }

    private URI createSelfLink(Hospital hospital){
        URI self = UriComponentsBuilder.fromPath("/{id}")
                .uriVariables(Map.of("id",hospital.getId()))
                .build().toUri();
        return self;
    }

    @PutMapping("/{id}")
    public HttpEntity<HospitalDto> replaceHospital(@Valid @PathVariable Long id, @RequestBody MutateHospitalCommand hospitalCommand){
        return ResponseEntity.ok(new HospitalDto(hospitalService.replaceHospital(id,hospitalCommand)));
    }

    @DeleteMapping({"/"})
    public HttpEntity<Void> deleteHospitals(){
        hospitalService.deleteHospitals();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping({"/{id}"})
    public HttpEntity<Void> deleteHospital(@PathVariable Long id, @RequestBody MutateHospitalCommand hospitalCommand){
        hospitalService.deleteHospital(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping({"/{id}"})
    public HttpEntity<HospitalDto> partiallyUpdateDoctor(@PathVariable Long id, @RequestBody MutateHospitalCommand mutateHospitalCommand){
        return ResponseEntity.ok(new HospitalDto(hospitalService.partiallyUpdate(id,mutateHospitalCommand)));
    }
}
