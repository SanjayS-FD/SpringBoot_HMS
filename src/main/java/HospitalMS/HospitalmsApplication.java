package HospitalMS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HospitalmsApplication {

	public static void main(String[] args) {

		SpringApplication.run(HospitalmsApplication.class, args);

	}

}
