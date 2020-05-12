package costunitimport.rssfeed;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Daten des RSS-Feeds. Es sind hier die eigentlichen aktuellen und zukünftigen Kostenträgerdateien<br>
 * des RSS-Feeds aufgelistet mit dem Titel, Link und Erstellungszeitpunkt
 */
public class CostUnitRSSFeedItem {

	String title;
	String link;
	String description;
	LocalDateTime pubDate;

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

	public String getDescription() {
		return description;
	}

	@XmlElement
	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getPubDate() {
		return pubDate;
	}

	@XmlElement
	@XmlJavaTypeAdapter(DateAdapter.class)
	public void setPubDate(LocalDateTime pubDate) {
		this.pubDate = pubDate;
	}
	
	/**
	 * Beschafft sich das Gültigkeitsdatum aus der Beschreibung der Datei, da dieses genauer ist, als das Datum aus dem UNB-Segment der Datei.
	 */
	public LocalDate getValidityFrom() {
		String dateString = description.replaceAll("\\D+","");
		
		String day = dateString.substring(0, 2);
		String month = dateString.substring(2, 4);
		String year = dateString.substring(4, 8);
		
		return LocalDate.parse(year + "-" + month + "-" + day);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("CostUnitRSSFeedItem[");
		builder.append("title:").append(getTitle());
		builder.append(", link:").append(getLink());
		builder.append(", description:").append(getDescription());
		builder.append(", pubDate:").append(getPubDate());
		builder.append("]");
		return builder.toString();
	}


}
