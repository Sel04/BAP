package at.spengergasse.sj21224bhifaslantanprojectdoctor.presentation.api;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Secretary;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence.SecretaryRepository;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.SecretaryService;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.MutateSecretaryCommand;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.SecretaryDto;
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
@RequestMapping(SecretaryRestController.BASE_URL)
public class SecretaryRestController {

    public static final String BASE_URL ="/api/secretary";
    public static final String PATH_INDEX ="/";
    public static final String PATH_VAR_ID ="/{id}";
    public static final String ROUTE_ID = BASE_URL + PATH_VAR_ID;

    private final SecretaryService secretaryService;

    private final SecretaryRepository secretaryRepository;

    @GetMapping({"",PATH_INDEX})
    public HttpEntity<List<SecretaryDto>> getSecretary(){
        List<Secretary> secretary = secretaryService.getSecretaries();

        return (secretary.isEmpty())
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(secretary.stream().map(SecretaryDto::new).toList());

    }

    @GetMapping({PATH_VAR_ID})
    public HttpEntity<SecretaryDto> getSecretarybyBirthDate(@PathVariable Long id){
        return secretaryService.getSecretary(id)
                .map(SecretaryDto::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    private URI createSelfLink(Secretary secretary){
        URI self= UriComponentsBuilder.fromPath(ROUTE_ID)
                .uriVariables(Map.of("id",secretary.getId()))
                .build().toUri();

        return self;
    }

    @PostMapping({"",PATH_INDEX})
    public HttpEntity<SecretaryDto> createSecretary(@Valid @RequestBody MutateSecretaryCommand command, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return (HttpEntity<SecretaryDto>) ResponseEntity.status(HttpStatus.CONFLICT);
        }
        else {
            Secretary secretary = secretaryService.createSecretary(command);
            return ResponseEntity.created(createSelfLink(secretary)).body(new SecretaryDto(secretary));
        }
    }

    @PutMapping({PATH_VAR_ID})
    public HttpEntity<SecretaryDto> replaceSecretary(@PathVariable Long id,@Valid @RequestBody MutateSecretaryCommand command){
        return ResponseEntity.ok(new SecretaryDto(secretaryService.replaceSecretary(id,command)));
    }

    @PatchMapping({PATH_VAR_ID})
    public HttpEntity<SecretaryDto> partiallyUpdateSecretary(@PathVariable Long id,@Valid @RequestBody MutateSecretaryCommand command){
        return ResponseEntity.ok(new SecretaryDto(secretaryService.partiallyUpdateSecretary(id,command)));
    }

    @DeleteMapping({"",PATH_INDEX})
    public HttpEntity<Void> deleteSecretaries(){
        secretaryService.deleteSecretary();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping({PATH_VAR_ID})
    public HttpEntity<Void> deleteSecretary(@PathVariable Long id){
        secretaryService.deleteSecretary(id);
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
