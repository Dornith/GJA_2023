package vut.fit.gja2023.systemmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class SystemManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SystemManagerApplication.class, args);
    }

}
