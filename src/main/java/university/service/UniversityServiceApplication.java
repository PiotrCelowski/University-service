package university.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
public class UniversityServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UniversityServiceApplication.class, args);
	}

}
