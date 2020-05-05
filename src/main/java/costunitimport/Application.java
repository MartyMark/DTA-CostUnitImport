package costunitimport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements ApplicationRunner {
	
	public final Logger log = LoggerFactory.getLogger(Application.class);
	
	public static void main(String... args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(final ApplicationArguments args) throws Exception {
		log.info("Application loaded.");
	}
}