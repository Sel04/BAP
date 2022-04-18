package at.spengergasse.sj21224bhifaslantanprojectdoctor.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Embeddable
public class Name {
    @NotNull(message = "Firstname can not be null")
    @NotBlank(message = "Firstname can not be blank")
    @NotEmpty(message = "Firstname can not be empty")
    private String firstname;
    @NotNull(message = "Subname can not be null")
    @NotBlank(message = "Subname can not be blank")
    @NotEmpty(message = "Subname can not be empty")
    private String subname;
    @NotNull(message = "Lastname can not be null")
    @NotBlank(message = "Lastname can not be blank")
    @NotEmpty(message = "Lastname can not be empty")
    private String lastname;
}