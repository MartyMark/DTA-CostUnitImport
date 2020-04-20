package costunitimport.segment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

import costunitimport.dao.factory.RepositoryFactory;
import costunitimport.model.CareProviderMethod;
import costunitimport.model.CostUnitFile;
import costunitimport.model.CostUnitSeparation;

public class CostUnitFileUNB extends CostUnitFileAbstract {

	private String fileName;
	private LocalDateTime creationTime;
	private RepositoryFactory rFactory;

	public CostUnitFileUNB(String[] data, RepositoryFactory rFactory) {
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
		
		getDtaCostUnitSeparationByToken(fileName.substring(0, 2)).ifPresent(x -> file.setDtaCostUnitSeparation(x));
		
		getCareProviderMethodByToken(fileName.substring(2, 4)).ifPresent(x -> file.setCareProviderMethod(x));
		
		
		file.setValidityFrom(getValidityFrom(fileName.substring(4, 6), fileName.substring(6, 8)));
		file.setVersion(Integer.valueOf(fileName.substring(10, 11)));
		return file;
	}

	private Optional<CostUnitSeparation> getDtaCostUnitSeparationByToken(String dtaCostUnitSeparationToken) {
		Integer  dtaCostUnitSeparationId = null;
		switch (dtaCostUnitSeparationToken) {
			case "AO"://AOK
				dtaCostUnitSeparationId = CostUnitSeparation.AOK;
			break;
			case "EK"://Ersatzkassen
				dtaCostUnitSeparationId = CostUnitSeparation.SUBSTITUTE_HEALTH_INSURANCE;
			break;
			case "BK"://Betriebskrankenkassen
				dtaCostUnitSeparationId = CostUnitSeparation.COMPANY_HEALTH_INSURANCE;
			break;
			case "IK"://Innungskrankenkassen
				dtaCostUnitSeparationId = CostUnitSeparation.GUILD_HEALTH_INSURANCE;
			break;
			case "BN"://Knappschaft-Bahn-See
				dtaCostUnitSeparationId = CostUnitSeparation.FEDERAL_MINERS_UNION;
			break;
			case "LK"://Landwirtschaftliche Krankenkassen
				dtaCostUnitSeparationId = CostUnitSeparation.AGRICULTURAL_HEALTH_INSURANCE;
			break;
			case "GK"://Gesetzliche Krankenversicherung
				dtaCostUnitSeparationId = CostUnitSeparation.OTHER;
			break;

			default:
//				throw new ApplicationException(ApplicationException.ILLEGAL_DATA_STATE, "Unbekannte Kassenart: " + dtaCostUnitSeparationToken);
		}
		return rFactory.getCostUnitSeparationRepository().findById(dtaCostUnitSeparationId.intValue());
	}
	
	private Optional<CareProviderMethod> getCareProviderMethodByToken(String careProviderMethodToken) {
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
//				throw new ApplicationException(ApplicationException.ILLEGAL_DATA_STATE, "Unbekanntes Verfahren: " + careProviderMethodToken);
		}
		return rFactory.getCareProviderMethodRepository().findById(careProviderMethodId.intValue());
	}
	
	private static LocalDate getValidityFrom(String validityFromMonthQuarter, String validityFromYear) throws ApplicationException {
		int year = 2000 + Integer.parseInt(validityFromYear);
		if(FormatVerifier.isNumber(validityFromMonthQuarter)) {//Monate
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
//				throw new ApplicationException(ApplicationException.ILLEGAL_DATA_STATE, "Fehler bei der Ermittlung des Datei Gültigkeitsdatums!");
		}
	}
}
