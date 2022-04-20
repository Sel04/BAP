package at.spengergasse.sj21224bhifaslantanprojectdoctor;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

@SpringBootApplication
public class Sj21224bhifAslanTanProjectDoctorApplication {


    public static void main(String[] args) {
        SpringApplication.run(Sj21224bhifAslanTanProjectDoctorApplication.class, args);
    }


}
