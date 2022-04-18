package at.spengergasse.sj21224bhifaslantanprojectdoctor.presentation.api;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Diagnosis;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.DiagnoseService;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.DiagnoseDto;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.MutateDiagnosisCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor

@RestController
@RequestMapping(value = DiagnoseRestController.BASE_URL)
public class DiagnoseRestController {
    public static final String BASE_URL = "/api/diagnoses";

    private final DiagnoseService diagnoseService;

    @GetMapping({"","/"})
    public HttpEntity<List<DiagnoseDto>> getDiagnoses(){
        List<Diagnosis> diagnoses = diagnoseService.getDiagnosis();

        return (diagnoses.isEmpty())
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(diagnoses.stream().map(DiagnoseDto::new).toList());
    }

    @GetMapping({"/{date}"})
    public HttpEntity<DiagnoseDto> getDiagnoseByDate(@PathVariable LocalDate date) {
        return diagnoseService.getDiagnosisbyDate(date)
                .map(DiagnoseDto::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping({"/{id}"})
    public HttpEntity<DiagnoseDto> getDiagnoseByDate(@PathVariable Long id) {
        return diagnoseService.getDiagnosisbyId(id)
                .map(DiagnoseDto::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping(value = {"","/"})
    public HttpEntity<DiagnoseDto> createDiagnose(@Valid @RequestBody MutateDiagnosisCommand mutateDiagnosisCommand){
        Diagnosis diagnosis = diagnoseService.createDiagnosis(mutateDiagnosisCommand);
        URI self = createSelfLink(diagnosis);
        return ResponseEntity.created(self).body(new DiagnoseDto(diagnosis));
    }


    private URI createSelfLink(Diagnosis diagnosis){
        URI self = UriComponentsBuilder.fromPath("/{id}")
                .uriVariables(Map.of("id",diagnosis.getId()))
                .build().toUri();
        return self;
    }

    @PutMapping("/{id}")
    public HttpEntity<DiagnoseDto> replaceDiagnose(@Valid @PathVariable Long id,@RequestBody MutateDiagnosisCommand mutateDiagnosisCommand){
        return ResponseEntity.ok(new DiagnoseDto(diagnoseService.replaceDiagnosis(id,mutateDiagnosisCommand)));
    }

    @DeleteMapping({"/"})
    public HttpEntity<Void> deleteDiagnoses(){
        diagnoseService.deleteDiagnoses();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping({"/{id}"})
    public HttpEntity<Void> deleteDiagnosis(@PathVariable Long id, @RequestBody MutateDiagnosisCommand mutateDiagnosisCommand){
        diagnoseService.deleteDiagnosis(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping({"/{id}"})
    public HttpEntity<DiagnoseDto> partiallyUpdateDiagnosis(@PathVariable Long id, @RequestBody MutateDiagnosisCommand mutateDiagnosisCommand){
        return ResponseEntity.ok(new DiagnoseDto(diagnoseService.partiallyUpdate(id,mutateDiagnosisCommand)));
    }

}
