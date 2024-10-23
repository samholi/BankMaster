package in.softgrid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SoftGridSimpleRegisterLoginApplication {

	public static void main(String[] args) {
		SpringApplication.run(SoftGridSimpleRegisterLoginApplication.class, args);
	}

}
