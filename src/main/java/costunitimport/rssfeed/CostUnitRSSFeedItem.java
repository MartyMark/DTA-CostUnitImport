package costunitimport.rssfeed;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlElement;

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
	
	public String getFileName() {
		return new File(link).getName().replace(".", "");
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
	public void setPubDate(LocalDateTime pubDate) {
		this.pubDate = pubDate;
	}
	
	public Path download() throws IOException {
		File tempDir = createTempDir();
		File tempFile = File.createTempFile(getFileName(), "", tempDir);

		Path path = Paths.get(tempFile.getAbsolutePath());
		try (InputStream in = new URL(link.replaceFirst("http:", "https:")).openStream()) {
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
}
