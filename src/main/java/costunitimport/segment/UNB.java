package costunitimport.segment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.regex.Pattern;

import costunitimport.dao.factory.RepositoryFactory;
import costunitimport.exception.CareProviderMethodNotFoundException;
import costunitimport.exception.CostUnitSeperationNotFoundException;
import costunitimport.exception.InternalServiceApplication;
import costunitimport.model.CareProviderMethod;
import costunitimport.model.CostUnitFile;
import costunitimport.model.DTACostUnitSeparation;

public class UNB extends Segment {

	private String fileName;
	private LocalDateTime creationTime;
	private RepositoryFactory rFactory;

	public UNB(String[] data, RepositoryFactory rFactory) {
		super(data);
		this.rFactory = rFactory;
	}

	@Override
	protected void assignData() {
		int position = 1;
		position++;//Syntax
		position++;//Absender der Datei
		position++;//Empfänger der Datei
		creationTime = getData(position++, LocalDateTime.class);//Datum	-Uhrzeit
		position++;//Dateinummer
		position++;//Freifeld
		fileName = getData(position, String.class);//Dateiname
	}

	/**
	 * Gibt den Dateinamen zurück
	 * 
	 * @return Dateiname
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Setzt den Dateinamen
	 * 
	 * @param fileName
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Gibt die Uhrzeit der Datei zurück
	 * 
	 * @return
	 */
	public LocalDateTime getCreationTime() {
		return creationTime;
	}

	/**
	 * Setzt die Uhrzeit der Datei
	 * 
	 * @param creationTime
	 */
	public void setCreationTime(LocalDateTime creationTime) {
		this.creationTime = creationTime;
	}

	public CostUnitFile getCostUnitFile() {
		CostUnitFile file = new CostUnitFile();
		file.setCreationTime(creationTime);
		file.setFileName(fileName);
		file.setDtaCostUnitSeparation(getDtaCostUnitSeparationByToken(fileName.substring(0, 2)));
		file.setCareProviderMethod(getCareProviderMethodByToken(fileName.substring(2, 4)));
		file.setValidityFrom(getValidityFrom(fileName.substring(4, 6), fileName.substring(6, 8)));
		file.setVersion(Integer.valueOf(fileName.substring(10, 11)));
		return file;
	}

	private DTACostUnitSeparation getDtaCostUnitSeparationByToken(String costUnitSeparationToken) {
		Integer dtaCostUnitSeparationId = null;
		switch (costUnitSeparationToken) {
		case "AO":// AOK
			dtaCostUnitSeparationId = DTACostUnitSeparation.AOK;
			break;
		case "EK":// Ersatzkassen
			dtaCostUnitSeparationId = DTACostUnitSeparation.SUBSTITUTE_HEALTH_INSURANCE;
			break;
		case "BK":// Betriebskrankenkassen
			dtaCostUnitSeparationId = DTACostUnitSeparation.COMPANY_HEALTH_INSURANCE;
			break;
		case "IK":// Innungskrankenkassen
			dtaCostUnitSeparationId = DTACostUnitSeparation.GUILD_HEALTH_INSURANCE;
			break;
		case "BN":// Knappschaft-Bahn-See
			dtaCostUnitSeparationId = DTACostUnitSeparation.FEDERAL_MINERS_UNION;
			break;
		case "LK":// Landwirtschaftliche Krankenkassen
			dtaCostUnitSeparationId = DTACostUnitSeparation.AGRICULTURAL_HEALTH_INSURANCE;
			break;
		case "GK":// Gesetzliche Krankenversicherung
			dtaCostUnitSeparationId = DTACostUnitSeparation.OTHER;
			break;
		default:
			throw new CostUnitSeperationNotFoundException(Integer.valueOf(costUnitSeparationToken));
		}
		return rFactory.getCostUnitSeparationRepository().findById(dtaCostUnitSeparationId.intValue()).orElseThrow();
	}
	
	private CareProviderMethod getCareProviderMethodByToken(String careProviderMethodToken) {
		Integer  careProviderMethodId = null;
		switch (careProviderMethodToken) {
			case "03"://Datenaustausch Teilprojekt Apotheken
				careProviderMethodId = CareProviderMethod.P_300;
				break;
			case "05"://Datenaustausch Teilprojekt Sonstige Leistungserbringer
				careProviderMethodId = CareProviderMethod.P_302;
				break;
			case "06"://Datenaustausch Teilprojekt Leistungserbringer Pflege
				careProviderMethodId = CareProviderMethod.P_105;
				break;
			default:
				throw new CareProviderMethodNotFoundException(Integer.valueOf(careProviderMethodToken));
		}
		return rFactory.getCareProviderMethodRepository().findById(careProviderMethodId.intValue()).orElseThrow();
	}
	
	private static LocalDate getValidityFrom(String validityFromMonthQuarter, String validityFromYear) {
		int year = 2000 + Integer.parseInt(validityFromYear);
		if(isNumber(validityFromMonthQuarter)) {//Monate
			int month = Integer.parseInt(validityFromMonthQuarter);
			return LocalDate.of(year, month, 1);
		}
		switch (validityFromMonthQuarter) {
			case "Q1":
				return LocalDate.of(year, Month.JANUARY, 1);
			case "Q2":
				return LocalDate.of(year, Month.APRIL, 1);
			case "Q3":
				return LocalDate.of(year, Month.JULY, 1);
			case "Q4":
				return LocalDate.of(year, Month.OCTOBER, 1);
			default:
				throw new InternalServiceApplication("Fehler bei der Ermittlung des Datei Gültigkeitsdatums!");
		}
	}
	
	/**
	 * Prüft, ob sich um eine Zahl handelt
	 */
	public static boolean isNumber(String input) {
		if (input == null) {
			return false;
		}
		return Pattern.matches("\\d+", input);
	}
}
