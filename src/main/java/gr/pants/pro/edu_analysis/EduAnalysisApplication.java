package gr.pants.pro.edu_analysis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class EduAnalysisApplication {

	public static void main(String[] args) {
		SpringApplication.run(EduAnalysisApplication.class, args);
	}

}
