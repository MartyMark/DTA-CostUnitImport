package costunitimport.rssfeed;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * Parser für den übergeben RSS-Feed in ein CostUnitRSSFeed-Objekt
 */
public class RSSFeedParser {
	private final URL url;

	public RSSFeedParser(String feedUrl) throws MalformedURLException {
		this.url = new URL(feedUrl);
	}

	public CostUnitRSSFeed readFeed() throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(CostUnitRSSFeed.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		return (CostUnitRSSFeed) jaxbUnmarshaller.unmarshal(url);
	}
}
