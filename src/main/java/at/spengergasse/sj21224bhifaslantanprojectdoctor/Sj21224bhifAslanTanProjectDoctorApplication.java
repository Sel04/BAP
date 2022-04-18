package at.spengergasse.sj21224bhifaslantanprojectdoctor;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Address;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Ordination;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence.OrdinationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Sj21224bhifAslanTanProjectDoctorApplication {

    public static void main(String[] args) {
        SpringApplication.run(Sj21224bhifAslanTanProjectDoctorApplication.class, args);
    }
}
