package costunitimport.rssfeed;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Adapter-Klasse zum konvertieren eines String in eine LocalDate<br>
 * Bei dem RSS-Feed vom Spitzenverband wird das Datum so angegeben: "Fri, 3 Jan 2020 14:24:11 +0200"
 */
public class DateAdapter extends XmlAdapter<String, LocalDateTime> {

	private final DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ZZZZZ", Locale.ENGLISH);

	@Override
	public LocalDateTime unmarshal(String string) throws Exception {
		//Mit dem DateTimeFormatter konnte das Datum nicht erstellt werden, ich bin deswegen den Weg über das SimpleDateFormat
		//gegangen. Hier hatte es komischerweise direkt geklappt. Ich habe Locale.ENGLISH angegeben wegen dem englischen
		//Wochentag. Es musste die Uhrzeit jedoch noch angepasst werden
		LocalDateTime localDateTime = dateFormat.parse(string).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		return localDateTime.plusHours(1);
	}

	@Override
	public String marshal(LocalDateTime date) throws Exception {
		return date.format(DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss ZZZZZ"));
	}
}
