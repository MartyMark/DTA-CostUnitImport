package costunitimport.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import javax.xml.bind.JAXBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import costunitimport.dao.factory.RepositoryFactory;
import costunitimport.exception.InternalServiceApplication;
import costunitimport.fileimport.CostUnitFileImport;
import costunitimport.model.CostUnitFile;
import costunitimport.rssfeed.CostUnitRSSFeed;
import costunitimport.rssfeed.CostUnitRSSFeedItem;
import costunitimport.rssfeed.RSSFeedParser;
import costunitimport.util.ImportUtil;

@RestController
public class ImportCostUnitFilesController {
	
	public final Logger log = LoggerFactory.getLogger(ImportCostUnitFilesController.class);

	@Autowired
	private RepositoryFactory rFactory;

	@Value("${rssfeeds.url}")
	private String rssfeedUrl;
	
	@GetMapping("/importcostunitfiles")
	public ResponseEntity<String> manuellImport() {
		importCostUnitFiles();
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * Import läuft jeden Tag um 6 Uhr morgens
	 */
	@Scheduled(cron = "0 0 6 * * *", zone = "Europe/Paris")
	public void automaticImport() {
		importCostUnitFiles();
	}


	private void importCostUnitFiles() {
		try {
			RSSFeedParser rssFeedParser = new RSSFeedParser(rssfeedUrl);
			CostUnitRSSFeed rssFeed = rssFeedParser.readFeed();

			for (CostUnitRSSFeedItem feedItem : rssFeed.getChannel().getItems()) {
				String filename = new File(feedItem.getLink()).getName().replace(".", "");
				List<CostUnitFile> costUnitFile = rFactory.getCostUnitFileRepository().findByFileName(filename);
				if (costUnitFile.isEmpty()) {
					log.info("Started import of CostUnitFile " + filename);
					String httpsLink = feedItem.getLink().replaceFirst("http:", "https:");
					Path path = ImportUtil.downloadAndSaveTempFile(httpsLink, filename);

					CostUnitFileImport costUnitImport = new CostUnitFileImport(path, rFactory);
					costUnitImport.start();

					log.info("Finished import of CostUnitFile " + filename);
					ImportUtil.deleteFiles(path);
				}
			}
		} catch (JAXBException | IOException e) {
			throw new InternalServiceApplication("Import ist fehlgeschlagen!", e);
		}
	}
}
