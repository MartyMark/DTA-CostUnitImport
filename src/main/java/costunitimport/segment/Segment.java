package costunitimport.segment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public abstract class Segment {
		private String[] data = new String[0];
	
	private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
	private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyMMdd:HHmm");
	private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmm");

	public Segment(String[] data) {
		this.data = data;
		assignData();
	}

	protected abstract void assignData();

	public <P extends Object> P getData(int position, Class<P> type) {
		String importData = getImportData(position);
		if (importData == null || importData.isEmpty()) {
			return null;
		}
		if (type.equals(String.class)) {
			return (P) importData;
		} else if (type.equals(Integer.class)) {
			return (P) Integer.valueOf(importData);
		} else if (type.equals(BigDecimal.class)) {
			return (P) BigDecimal.valueOf(Double.valueOf(importData));
		} else if (type.equals(LocalDate.class)) {
			try {
				return (P)LocalDate.parse(importData, dateFormatter);
			} catch (Exception e) {
//				throw new ApplicationException(ApplicationException.ILLEGAL_DATA_STATE, "Fehler beim erzeugen des Datums: " + importData);
			}
		} else if(type.equals(LocalTime.class)) {
			try {
				return (P) LocalTime.parse(importData, timeFormatter);
			} catch (Exception e) {
//				throw new ApplicationException(ApplicationException.ILLEGAL_DATA_STATE, "Fehler beim erzeugen der Zeit: " + importData);
			}
		} else if(type.equals(LocalDateTime.class)) {
			try {
				return (P) LocalDateTime.parse(importData, dateTimeFormatter);
			} catch (Exception e) {
//				throw new ApplicationException(ApplicationException.ILLEGAL_DATA_STATE, "Fehler beim erzeugen der Zeit: " + importData);
			}
		}
//		throw new ApplicationException("Unbekannter Datentyp! Typ: " + type);
		return null;
	}

	private String getImportData(int position) {
		if (data != null && data.length > position) {
			String importData = data[position];
			if(importData.endsWith("'")) {
				return importData.substring(0, importData.length()-1);
			}
			return importData;
		}
		return null;
	}
}
