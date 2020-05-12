package costunitimport.rssfeed;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Kopfdaten des RSS-Feeds
 */
@XmlRootElement(name = "channel")
public class CostUnitRSSChannel {

	private String title;
	private String link;
	private String language;

	private List<CostUnitRSSFeedItem> items = new ArrayList<>();

	public String getTitle() {
		return title;
	}

	@XmlElement
	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	@XmlElement
	public void setLink(String link) {
		this.link = link;
	}

	public String getLanguage() {
		return language;
	}

	@XmlElement
	public void setLanguage(String language) {
		this.language = language;
	}


	public List<CostUnitRSSFeedItem> getItems() {
		return items;
	}
	
	/**
	 * Filtert die importierten Kostenträgerdateien.
	 * 
	 * 1. Kostenträgerdateien deren GültigkeitsAb-Datum in der Zukunft liegt
	 * 2. Zu alte Versionen
	 */
	public List<CostUnitRSSFeedItem> getFilterdItems() {
		Map<String, List<CostUnitRSSFeedItem>> linkNameToItems = new HashMap<>();
		
		for(CostUnitRSSFeedItem item : items) {
			/* Liegt das GültigAb-Datum vor dem Importdatum wird die Datei nicht importiert */
			if(item.getValidityFrom().isBefore(LocalDate.now())) {
				String key = item.getLink().substring(0, item.getLink().length() - 3);
				
				if(linkNameToItems.get(key) == null) {
					linkNameToItems.put(key, new ArrayList<>());
				}
				linkNameToItems.get(key).add(item);
			}
		}
		
		List<CostUnitRSSFeedItem> filterdItems = new ArrayList<>();
		for (Entry<String, List<CostUnitRSSFeedItem>> entry : linkNameToItems.entrySet()) {
			entry.getValue().sort(Comparator.comparing(CostUnitRSSFeedItem::getValidityFrom).reversed());
			
			filterdItems.add(entry.getValue().get(0));
		}
		return filterdItems;
	}

	@XmlElement(name = "item")
	public void setItems(List<CostUnitRSSFeedItem> items) {
		this.items = items;
	}

	public List<CostUnitRSSFeedItem> getMessages() {
		return items;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("CostUnitRSSChannel[");
		builder.append("title:").append(getTitle());
		builder.append(", link:").append(getLink());
		builder.append(", language:").append(getLanguage());
		builder.append("\r\n CostUnitRSSFeedItems:\\r\\n");
		for (CostUnitRSSFeedItem costUnitRSSFeedItem : items) {
			builder.append(costUnitRSSFeedItem).append("\r\n");
		}
		builder.append("]");
		return builder.toString();
	}
}
