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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import costunitimport.costunitImport.CostUnitFileImport;
import costunitimport.dao.factory.RepositoryFactory;
import costunitimport.logger.Logger;
import costunitimport.model.CostUnitFile;
import costunitimport.model.CostUnitInstitution;
import costunitimport.rssfeed.CostUnitRSSFeed;
import costunitimport.rssfeed.CostUnitRSSFeedItem;
import costunitimport.rssfeed.RSSFeedParser;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class ImportCostUnitFilesController {
	
	@Autowired
	private RepositoryFactory repositoryFactory;
	
    @Value("${rssfeeds.url}")
    private String rssfeedUrl;
	
	public EntityModel<CostUnitInstitution> findCostUnitInstitutionById(@RequestParam Integer id) {
		try {
			RSSFeedParser rssFeedParser = new RSSFeedParser(rssfeedUrl);
			CostUnitRSSFeed rssFeed = rssFeedParser.readFeed();
			
			for (CostUnitRSSFeedItem feedItem : rssFeed.getChannel().getItems()) {
				String filename = new File(feedItem.getLink()).getName().replace(".", "");
				List<CostUnitFile> costUnitFile = repositoryFactory.getCostUnitFileRepository().findByFileName(filename);
				if(costUnitFile.isEmpty()) {
					Logger.info("Started import of CostUnitFile " + filename);
					String httpsLink = feedItem.getLink().replaceFirst("http:", "https:");
					Path path = downloadAndSaveTempFile(httpsLink, filename);
					
					CostUnitFileImport costUnitImport = new CostUnitFileImport(path, repositoryFactory);
					costUnitImport.start();
					
					Logger.info("Finished import of CostUnitFile " + filename);
					deleteFiles(path);
				}
			}
			
		} catch (JAXBException | IOException e) {
			Logger.error(e);
		}
		return null;
	}
	
	private void deleteFiles(Path path) {
		try {
			Files.deleteIfExists(path);
		} catch (IOException e) {
			Logger.info("File " + path.getFileName() + " konte nicht gelöscht werden!");
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
