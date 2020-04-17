package costunitimport;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class LoadDatabase {
	@Bean
	CommandLineRunner initDatabase() {
		return args -> {
			log.info("Preloading started");
			
			log.info("Preloading finished");
		};
	}
}
