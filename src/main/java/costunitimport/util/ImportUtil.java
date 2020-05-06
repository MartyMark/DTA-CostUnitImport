package costunitimport.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

	/**
	 * Speichert die angegebene Datei von der Adresse in ein temporäres lokales Verzeichnis
	 */
	public static Path downloadAndSaveTempFile(String link, String filename) throws IOException {
		File tempDir = createTempDir();
		File tempFile = File.createTempFile(filename, "", tempDir);

		Path path = Paths.get(tempFile.getAbsolutePath());
		try (InputStream in = new URL(link).openStream()) {
			Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
		}
		return path;
	}
	
	/**
	 * Erstellt ein Temp-Verzeichnis
	 *
	 * @return ein <code>File</code>-Objekt, das auf das Temp-Verzeichnis verweist.
	 */
	public static File createTempDir() {
		final int tempDirAttempts = 10000;

		File baseDir = new File(System.getProperty("java.io.tmpdir"));
		String baseName = System.currentTimeMillis() + "-";

		for (int counter = 0; counter < tempDirAttempts; counter++) {
			File tempDir = new File(baseDir, baseName + counter);
			if (tempDir.mkdir()) {
				return tempDir;
			}
		}
		throw new IllegalStateException("Failed to create directory within " + tempDirAttempts + " attempts (tried " + baseName + "0 to " + baseName + (tempDirAttempts - 1) + ')');
	}
}
