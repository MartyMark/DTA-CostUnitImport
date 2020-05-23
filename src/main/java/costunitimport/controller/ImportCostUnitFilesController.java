package costunitimport.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import costunitimport.dao.factory.RepositoryFactory;
import costunitimport.exception.InternalServiceApplication;
import costunitimport.fileimport.FileImport;
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
	             
	@PreAuthorize("hasRole('ADMIN')")
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
			CostUnitRSSFeed rssFeed = new RSSFeedParser(rssfeedUrl).readFeed();
			
			for (CostUnitRSSFeedItem feedItem : rssFeed.getChannel().getFilterdItems()) {
				Optional<CostUnitFile> file = rFactory.getFileRepository().findByName(feedItem.getFileName());
				if (file.isEmpty()) {
					Path path = feedItem.download();

					FileImport fileImp = new FileImport(path, rFactory, feedItem.getValidityFrom());
					fileImp.start();

					ImportUtil.deleteFiles(path);
				}
			}
		} catch (JAXBException | IOException e) {
			throw new InternalServiceApplication("Import ist fehlgeschlagen!", e);
		}
	}
}
