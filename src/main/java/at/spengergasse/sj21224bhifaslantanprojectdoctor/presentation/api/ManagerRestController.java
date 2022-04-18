package at.spengergasse.sj21224bhifaslantanprojectdoctor.presentation.api;


import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Manager;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.ManagerService;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.ManagerDto;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.MutateManagerCommand;
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
@RequestMapping(ManagerRestController.BASE_URL)
public class ManagerRestController {
    public static final String BASE_URL = "/api/manager";

    private  final ManagerService managerService;

    @GetMapping({"","/"})
    public HttpEntity<List<ManagerDto>> getManager(){
        List<Manager> managers = managerService.getManager();

        return (managers.isEmpty())
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(managers.stream().map(ManagerDto::new).toList());
    }
    @GetMapping({"/{id}"})
    public HttpEntity<ManagerDto> getManagerbyId(@PathVariable Long id){
        return managerService.getManagerById(id)
                .map(ManagerDto::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping({"","/"})
    public HttpEntity<ManagerDto> createManager(@Valid @RequestBody MutateManagerCommand mutateManagerCommand){
       Manager manager = managerService.createManager(mutateManagerCommand);
       URI self = createSelfLink(manager);
       return ResponseEntity.created(self).body(new ManagerDto(manager));
    }

    private URI createSelfLink(Manager manager){
        URI self = UriComponentsBuilder.fromPath("/{id}")
                .uriVariables(Map.of("id",manager.getId()))
                .build().toUri();
        return self;
    }

    @PutMapping("/{id}")
    public HttpEntity<ManagerDto> replaceManager(@Valid @PathVariable Long id, @RequestBody MutateManagerCommand mutateManagerCommand){
        managerService.replaceManager(id,mutateManagerCommand);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/")
    public HttpEntity<Void> deleteManagers(){
        managerService.deleteManagers();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping({"/{id}"})
    public HttpEntity<Void> deleteManager(@PathVariable Long id, @RequestBody MutateManagerCommand mutateManagerCommand){
        managerService.deleteManager(id);
        return ResponseEntity.ok().build();
    }


    @PatchMapping({"/{id}"})
    public HttpEntity<ManagerDto> partiallyUpdateDoctor(@PathVariable Long id, @RequestBody MutateManagerCommand mutateManagerCommand){
        return  ResponseEntity.ok(new ManagerDto(managerService.partiallyUpdate(id,mutateManagerCommand)));
    }





}
