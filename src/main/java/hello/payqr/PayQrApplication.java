package hello.payqr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PayQrApplication {

	public static void main(String[] args) {
		SpringApplication.run(PayQrApplication.class, args);
	}

}
