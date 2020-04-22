package costunitimport;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import costunitimport.logger.Logger;

@SpringBootApplication
public class Application implements ApplicationRunner {

	public static void main(String... args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(final ApplicationArguments args) throws Exception {
		Logger.info("Application loaded.");
	}
}