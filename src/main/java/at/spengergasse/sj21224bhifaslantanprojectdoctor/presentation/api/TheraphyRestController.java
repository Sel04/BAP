package at.spengergasse.sj21224bhifaslantanprojectdoctor.presentation.api;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Therapy;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence.TherapyRepository;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.TherapyService;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.MutateTherapyCommand;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.TheraphyDto;
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
@RequestMapping(TheraphyRestController.BASE_URL)
public class TheraphyRestController {

    public static final String BASE_URL ="/api/theraphy";
    public static final String PATH_INDEX ="/";
    public static final String PATH_VAR_ID ="/{id}";
    public static final String ROUTE_ID = BASE_URL + PATH_VAR_ID;

    private final TherapyService therapyService;

    private final TherapyRepository therapyRepository;

    @GetMapping({"",PATH_INDEX})
    public HttpEntity<List<TheraphyDto>> getTheraphy(){
        List<Therapy> therapies=therapyService.getTheraphies();

        return(therapies.isEmpty())
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(therapies.stream().map(TheraphyDto::new).toList());
    }

    @GetMapping({PATH_VAR_ID})
    public HttpEntity<TheraphyDto> getTheraphyById(@PathVariable long id){
        return therapyService.getTheraphy(id)
                .map(TheraphyDto::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @PostMapping({"",PATH_INDEX})
    public HttpEntity<TheraphyDto> createOrdination(@Valid @RequestBody MutateTherapyCommand command, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return (HttpEntity<TheraphyDto>) ResponseEntity.status(HttpStatus.CONFLICT);
        }
        else {
            Therapy therapy = therapyService.createTheraphy(command);
            return ResponseEntity.created(createSelfLink(therapy)).body(new TheraphyDto(therapy));
        }
    }

    private URI createSelfLink(Therapy therapy) {
        URI self= UriComponentsBuilder.fromPath(ROUTE_ID)
                .uriVariables(Map.of("id",therapy.getId()))
                .build().toUri();

        return self;
    }

    @PutMapping({PATH_VAR_ID})
    public HttpEntity<TheraphyDto> replaceOrdination(@PathVariable long id,@Valid @RequestBody MutateTherapyCommand command)  {
        return ResponseEntity.ok(new TheraphyDto(therapyService.replaceTheraphy(id,command)));
    }

    @PatchMapping({PATH_VAR_ID})
    public HttpEntity<TheraphyDto> partiallyUpdateTheraphy(@PathVariable Long id, @RequestBody MutateTherapyCommand command){
        return ResponseEntity.ok(new TheraphyDto(therapyService.partiallyUpdateTheraphy(id,command)));
    }

    @DeleteMapping({"",PATH_INDEX})
    public HttpEntity<Void> deleteTheraphies(){
        therapyService.deleteTheraphy();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping({PATH_VAR_ID})
    public HttpEntity<Void> deleteTheraphy(@PathVariable long id){
        therapyService.deleteTheraphy(id);
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
