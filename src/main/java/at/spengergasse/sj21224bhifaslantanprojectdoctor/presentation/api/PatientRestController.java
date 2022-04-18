package at.spengergasse.sj21224bhifaslantanprojectdoctor.presentation.api;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Patient;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence.PatientRepository;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.PatientService;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.MutatePatientCommand;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.PatientDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor

@RestController
@RequestMapping(PatientRestController.BASE_URL)
public class PatientRestController {

    public static final String BASE_URL ="/api/patient";
    public static final String PATH_INDEX ="/";
    public static final String PATH_VAR_ID ="/{id}";
    public static final String ROUTE_ID = BASE_URL + PATH_VAR_ID;

    private final PatientService patientService;

    private final PatientRepository patientRepository;

    @GetMapping({"",PATH_INDEX})
    public HttpEntity<List<PatientDto>> getPatient(){
        List<Patient> patients =patientService.getPatients();

        return(patients.isEmpty())
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(patients.stream().map(PatientDto::new).toList());
    }

    @GetMapping(PATH_VAR_ID)
    public HttpEntity<PatientDto> getPatientById(@PathVariable Long id){
        return patientService.getPatient(id)
                .map(PatientDto::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    private URI createSelfLink(Patient patient){
        URI self= UriComponentsBuilder.fromPath(ROUTE_ID)
                .uriVariables(Map.of("id",patient.getId()))
                .build().toUri();

        return self;
    }

    @PostMapping({"",PATH_INDEX})
    public HttpEntity<PatientDto> createPatient(@Valid @RequestBody MutatePatientCommand command, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return (HttpEntity<PatientDto>) ResponseEntity.status(HttpStatus.CONFLICT);
        }
        else {
            Patient patient = patientService.createPatient(command);
            return ResponseEntity.created(createSelfLink(patient)).body(new PatientDto(patient));
        }
    }

    @PutMapping({PATH_VAR_ID})
    public HttpEntity<PatientDto> replacePatient(@PathVariable Long id, @Valid @RequestBody MutatePatientCommand command){
        return ResponseEntity.ok(new PatientDto(patientService.replacePatient(id,command)));
    }

    @PatchMapping({PATH_VAR_ID})
    public HttpEntity<PatientDto> partiallyUpdatePatient(@PathVariable Long id,  @RequestBody MutatePatientCommand command){
        return ResponseEntity.ok(new PatientDto(patientService.partiallyUpdatePatient(id,command)));
    }

    @DeleteMapping({"",PATH_INDEX})
    public HttpEntity<Void> deletePatients(){
        patientService.deletePatients();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping({PATH_VAR_ID})
    public HttpEntity<Void> deletePatient(@PathVariable Long id ){
        patientService.deletePatient(id);
        return ResponseEntity.ok().build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String,String> handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

}
