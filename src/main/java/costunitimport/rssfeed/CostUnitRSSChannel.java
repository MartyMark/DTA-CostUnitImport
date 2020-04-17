package costunitimport.rssfeed;

import java.util.ArrayList;
import java.util.List;
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
