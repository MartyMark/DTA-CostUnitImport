package costunitimport.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import costunitimport.dao.factory.RepositoryFactory;
import costunitimport.exception.InternalServiceApplication;
import costunitimport.fileimport.CostUnitFileImport;
import costunitimport.model.CostUnitFile;
import costunitimport.model.CostUnitInstitution;
import costunitimport.rssfeed.CostUnitRSSFeed;
import costunitimport.rssfeed.CostUnitRSSFeedItem;
import costunitimport.rssfeed.RSSFeedParser;

@RestController
public class ImportCostUnitFilesController {
	
	public final Logger log = LoggerFactory.getLogger(ImportCostUnitFilesController.class);
	
	@Autowired
	private RepositoryFactory rFactory;
	
    @Value("${rssfeeds.url}")
    private String rssfeedUrl;
	
    @GetMapping("/importcostunitfiles")
	public EntityModel<CostUnitInstitution> importCostUnitFiles() {
		try {
			RSSFeedParser rssFeedParser = new RSSFeedParser(rssfeedUrl);
			CostUnitRSSFeed rssFeed = rssFeedParser.readFeed();
			
			for (CostUnitRSSFeedItem feedItem : rssFeed.getChannel().getItems()) {
				String filename = new File(feedItem.getLink()).getName().replace(".", "");
				List<CostUnitFile> costUnitFile = rFactory.getCostUnitFileRepository().findByFileName(filename);
				if(costUnitFile.isEmpty()) {
					log.info("Started import of CostUnitFile " + filename);
					String httpsLink = feedItem.getLink().replaceFirst("http:", "https:");
					Path path = downloadAndSaveTempFile(httpsLink, filename);
					
					CostUnitFileImport costUnitImport = new CostUnitFileImport(path, rFactory);
					costUnitImport.start();
					
					log.info("Finished import of CostUnitFile " + filename);
					deleteFiles(path);
				}
			}
		} catch (JAXBException | IOException e) {
			throw new InternalServiceApplication("Import ist fehlgeschlagen!", e); 
		}
		return null;
	}
	
	private void deleteFiles(Path path) {
		try {
			Files.deleteIfExists(path);
		} catch (IOException e) {
			log.info("File " + path.getFileName() + " konte nicht gelöscht werden!");
		}
	}

	/**
	 * Speichert die angegebene Datei von der Adresse in ein temporäres lokales Verzeichnis
	 */
	private static Path downloadAndSaveTempFile(String link, String filename) throws IOException {
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
