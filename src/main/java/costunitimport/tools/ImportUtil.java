package costunitimport.tools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImportUtil {
	
	public static final Logger log = LoggerFactory.getLogger(ImportUtil.class);
	
	private ImportUtil() {}
	
	public static void deleteFiles(Path path) {
		try {
			Files.deleteIfExists(path);
		} catch (IOException e) {
			log.info("File " + path.getFileName() + " konte nicht gelöscht werden!");
		}
	}
}
