package at.spengergasse.sj21224bhifaslantanprojectdoctor.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Embeddable
public class Phonenumber {
    private String countrycode;
    private String areacode;
    private String serialnumber;
}
