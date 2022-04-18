package at.spengergasse.sj21224bhifaslantanprojectdoctor.presentation.api;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Ordination;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence.OrdinationRepository;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.OrdinationService;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.MutateOrdinationCommand;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.OrdinationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping(OrdinationRestController.BASE_URL)
public class OrdinationRestController {

    public static final String BASE_URL ="/api/ordination";
    public static final String PATH_INDEX ="/";
    public static final String PATH_VAR_ID ="/{id}";
    public static final String ROUTE_ID = BASE_URL + PATH_VAR_ID;


    private final OrdinationService ordinationService;

    private  OrdinationRepository ordinationRepository;

    @GetMapping({"",PATH_INDEX})
    public HttpEntity<List<OrdinationDto>> getOrdination(){
        List<Ordination>ordinations=ordinationService.getOrdinations();

        return(ordinations.isEmpty())
            ? ResponseEntity.noContent().build()
            : ResponseEntity.ok(ordinations.stream().map(OrdinationDto::new).toList());
    }

    @GetMapping({PATH_VAR_ID})
    public HttpEntity<OrdinationDto> getOrdinationById(@PathVariable Long id){
        return ordinationService.getOrdination(id)
                .map(OrdinationDto::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @PostMapping({"",PATH_INDEX})
    public HttpEntity<OrdinationDto> createOrdination(@Valid @RequestBody MutateOrdinationCommand command){
            Ordination ordination = ordinationService.createOrdination(command);
            return ResponseEntity.created(createSelfLink(ordination)).body(new OrdinationDto(ordination));
    }


    private URI createSelfLink(Ordination ordination){
        URI self= UriComponentsBuilder.fromPath(ROUTE_ID)
                .uriVariables(Map.of("id",ordination.getId()))
                .build().toUri();
        return self;
    }


    @PutMapping({PATH_VAR_ID})
    public HttpEntity<OrdinationDto> replaceOrdination(@PathVariable Long id,@Valid @RequestBody MutateOrdinationCommand command)  {
        return ResponseEntity.ok(new OrdinationDto(ordinationService.replaceOrdination(id,command)));
    }

    @PatchMapping({PATH_VAR_ID})
    public HttpEntity<OrdinationDto> partiallyUpdateOrdination(@PathVariable Long id,  @RequestBody MutateOrdinationCommand command){
        return ResponseEntity.ok(new OrdinationDto(ordinationService.partiallyUpdateOrdination(id,command)));
    }

    @DeleteMapping({"",PATH_INDEX})
    public HttpEntity<Void> deleteOrdinations(){
        ordinationService.deleteOrdinations();
        return  ResponseEntity.ok().build();
    }

    @DeleteMapping({PATH_VAR_ID})
    public HttpEntity<Void> deleteOrdination(@PathVariable Long id){
        ordinationService.deleteOrdination(id);
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
