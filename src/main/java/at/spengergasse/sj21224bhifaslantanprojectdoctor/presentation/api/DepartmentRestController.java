package at.spengergasse.sj21224bhifaslantanprojectdoctor.presentation.api;


import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Department;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.DepartmentService;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.DepartmentDto;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.MutateDepartmentCommand;
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
@RequestMapping(DepartmentRestController.BASE_URL)
public class DepartmentRestController {
    public static final String BASE_URL = "/api/departments";

    private final DepartmentService departmentService;

    @GetMapping({"","/"})
    public HttpEntity<List<DepartmentDto>> getDepartments(){
        List<Department> departments = departmentService.getDepartments();

        return (departments.isEmpty())
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(departments.stream().map(DepartmentDto::new).toList());
    }

    @GetMapping({"/id"})
    public HttpEntity<DepartmentDto> getDepartmentById(@PathVariable Long id){
        return departmentService.getDepartmentById(id)
                .map(DepartmentDto::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /*@GetMapping({"/id"})
    public HttpEntity<DepartmentDto> getDepartmentByHead(@PathVariable Long id){
        return departmentService.getDepartmentById(id)
                .map(DepartmentDto::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }*/

    @PostMapping({"","/"})
    public HttpEntity<DepartmentDto> createDepartment(@Valid @RequestBody MutateDepartmentCommand mutateDepartmentCommand){
        Department newDepartment = departmentService.createDepartment(mutateDepartmentCommand);
        URI self = createSelfLink(newDepartment);
        return ResponseEntity.created(self).body(new DepartmentDto(newDepartment));

    }

    private URI createSelfLink(Department department){
        URI self = UriComponentsBuilder.fromPath("/{id}")
                .uriVariables(Map.of("id",department.getId()))
                .build().toUri();
        return self;
    }

    @PutMapping({"/{id}"})
    public HttpEntity<DepartmentDto> replaceDepartment(@Valid @PathVariable Long id, @RequestBody MutateDepartmentCommand mutateDepartmentCommand){
            return ResponseEntity.ok(new DepartmentDto(departmentService.replaceDepartment(id,mutateDepartmentCommand)));
    }

    @DeleteMapping({""})
    public HttpEntity<Void> deleteDepartments(){
        departmentService.deleteDepartments();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping({"/{id}"})
    public HttpEntity<Void> deleteDepartment(@PathVariable Long id){
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping({"/{id}"})
    public HttpEntity<DepartmentDto> partiallyUpdateDepartment(@PathVariable Long id, @RequestBody MutateDepartmentCommand mutateDepartmentCommand){
        return ResponseEntity.ok(new DepartmentDto(departmentService.partiallyUpdate(id,mutateDepartmentCommand)));
    }
}
