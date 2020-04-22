package costunitimport;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import costunitimport.logger.Logger;

@Configuration
public class LoadDatabase {
	@Bean
	CommandLineRunner initDatabase() {
		return args -> {
			Logger.info("Preloading started");
			
			Logger.info("Preloading finished");
		};
	}
}
