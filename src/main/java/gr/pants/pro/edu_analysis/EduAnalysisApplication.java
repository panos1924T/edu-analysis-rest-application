package gr.pants.pro.edu_analysis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing
@EnableRetry
@EnableAsync
public class EduAnalysisApplication {

	public static void main(String[] args) {
		SpringApplication.run(EduAnalysisApplication.class, args);
	}

}
