package at.spengergasse.sj21224bhifaslantanprojectdoctor.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Embeddable
public class Address {
    @NotNull(message = "Street can not be null")
    @NotBlank(message = "Street can not be blank")
    @NotEmpty(message = "Street can not be empty")
    private String street;
    @NotNull(message = "Zipcode can not be null")
    @NotBlank(message = "Zipcode can not be blank")
    @NotEmpty(message = "Zipcode can not be empty")
    private String zipcode;
    @NotNull(message = "Place can not be null")
    @NotBlank(message = "Place can not be blank")
    @NotEmpty(message = "Place can not be empty")
    private String place;
}
