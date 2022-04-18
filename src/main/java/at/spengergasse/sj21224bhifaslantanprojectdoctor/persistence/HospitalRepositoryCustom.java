package at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Hospital;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Component;

@Component
public interface HospitalRepositoryCustom {
    Hospital addHospital(Hospital hospital);
    Hospital getByPhonenumber(String phonenumber);
    Hospital getByAbbreviation(String abbreviation);
}
