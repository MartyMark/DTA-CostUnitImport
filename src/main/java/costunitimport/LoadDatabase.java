package costunitimport;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import costunitimport.dao.factory.RepositoryFactory;
import costunitimport.logger.Logger;
import costunitimport.model.CareProviderMethod;
import costunitimport.model.CostUnitTypeAssignment;
import costunitimport.model.CostUnitTypeMedium;
import costunitimport.model.DTACostUnitSeparation;
import costunitimport.model.address.Country;
import costunitimport.model.address.FederalState;
import costunitimport.model.address.ZipType;
import costunitimport.model.sags.DTAAccumulativeGroupKey;

@Configuration
public class LoadDatabase {
	
	private static final String UNKNOWN = "unbekannt";
	
	@Bean
	CommandLineRunner initDatabase(RepositoryFactory rFactory) {
		return args -> {
			Logger.info("Country - STAAT");
			Country unknown = new Country();
			unknown.setId(0);
			unknown.setDescription(UNKNOWN);
			unknown.setMaxZipLength(0);
			unknown.setFlag("WHITE");
			rFactory.getCountryRepository().save(unknown);
			
			Country germany = new Country();
			germany.setId(35);
			germany.setDescription("Deutschland");
			germany.setToken("D");
			germany.setMaxZipLength(5);
			germany.setDtaTokenP302("D");
			germany.setDtaTokenP301("D");
			germany.setiSOA2("DE");
			germany.setiSOA3("DEU");
			germany.setiSON3(276);
			germany.setFlag("GERMANY");
			germany.setIbanLength(22);
			germany.setCountryCode("+49");
			
			rFactory.getCountryRepository().save(germany);
			
			Logger.info("FederalState - STAAT_BUNDESLAND");
			rFactory.getFederalStateRepository().save(new FederalState(0, germany, UNKNOWN, null));
			rFactory.getFederalStateRepository().save(new FederalState(1, germany, "Schleswig-Holstein", null));
			rFactory.getFederalStateRepository().save(new FederalState(2, germany, "Hamburg", null));
			rFactory.getFederalStateRepository().save(new FederalState(3, germany, "Niedersachen", null));
			rFactory.getFederalStateRepository().save(new FederalState(4, germany, "Bremen", null));
			rFactory.getFederalStateRepository().save(new FederalState(5, germany, "Nordrhein-Westfalen", null));
			rFactory.getFederalStateRepository().save(new FederalState(6, germany, "Hessen", null));
			rFactory.getFederalStateRepository().save(new FederalState(7, germany, "Rheinland-Pfalz", null));
			rFactory.getFederalStateRepository().save(new FederalState(8, germany, "Baden-Württemberg", null));
			rFactory.getFederalStateRepository().save(new FederalState(9, germany, "Bayern", null));
			rFactory.getFederalStateRepository().save(new FederalState(10, germany, "Saarland", null));
			rFactory.getFederalStateRepository().save(new FederalState(11, germany, "Berlin", null));
			rFactory.getFederalStateRepository().save(new FederalState(12, germany, "Brandenburg", null));
			rFactory.getFederalStateRepository().save(new FederalState(13, germany, "Mecklenburg-Vorpommern", null));
			rFactory.getFederalStateRepository().save(new FederalState(14, germany, "Sachsen", null));
			rFactory.getFederalStateRepository().save(new FederalState(15, germany, "Sachsen-Anhalt", null));
			rFactory.getFederalStateRepository().save(new FederalState(16, germany, "Thüringen", null));
			rFactory.getFederalStateRepository().save(new FederalState(16, germany, "Rheinland", 5));
			rFactory.getFederalStateRepository().save(new FederalState(17, germany, "Westfalen-Lippe", 5));
			rFactory.getFederalStateRepository().save(new FederalState(99, germany, "Alle Bundesländer", null));
			
			Logger.info("ZipType - STAAT_PLZ_ART");
			rFactory.getZipTypeRepository().save(new ZipType(1, "Postfach"));
			rFactory.getZipTypeRepository().save(new ZipType(2, "Schalterausgabe"));
			rFactory.getZipTypeRepository().save(new ZipType(3, "Gruppen-GE"));
			rFactory.getZipTypeRepository().save(new ZipType(4, "Einzel-GE"));
			rFactory.getZipTypeRepository().save(new ZipType(5, "Aktions-PLZ"));
			rFactory.getZipTypeRepository().save(new ZipType(6, "Zustellung und Postfach"));
			rFactory.getZipTypeRepository().save(new ZipType(7, "Import-Schnittstelle"));
			rFactory.getZipTypeRepository().save(new ZipType(8, "Import-Schnittstelle ungeprüft"));
			
			Logger.info("DTACareProvidermethod - DTA_LEISTUNGSVERFAHREN");
			CareProviderMethod unknownMethod = new CareProviderMethod(0, UNKNOWN, "", false);
			CareProviderMethod apo = new CareProviderMethod(3, "Apotheken", "Abrechnung nach §300", true);
			CareProviderMethod p302 = new CareProviderMethod(5, "Sonstige Leistungserbringer", "Abrechnung nach §302", true);
			CareProviderMethod hpf = new CareProviderMethod(6, "Leistungserbringer Pflege", "Abrechnung nach §105", true);
			CareProviderMethod hospital = new CareProviderMethod(9, "Krankenhäuser", "Abrechnung nach §301", true);
			CareProviderMethod direct = new CareProviderMethod(10, "Direktabrechner", "Abrechnung nach §295 1b SGB V", true);
			
			rFactory.getCareProviderMethodRepository().save(unknownMethod);
			rFactory.getCareProviderMethodRepository().save(p302);
			rFactory.getCareProviderMethodRepository().save(apo);
			rFactory.getCareProviderMethodRepository().save(hpf);
			rFactory.getCareProviderMethodRepository().save(hospital);
			rFactory.getCareProviderMethodRepository().save(direct);
			
			Logger.info("CostUnitTypeMedium - KASSEN_ART_MEDIUM");
			rFactory.getCostUnitTypeMediumRepository().save(new CostUnitTypeMedium(1, "DFÜ"));
			rFactory.getCostUnitTypeMediumRepository().save(new CostUnitTypeMedium(2, "Magnetband"));
			rFactory.getCostUnitTypeMediumRepository().save(new CostUnitTypeMedium(3, "Magnetbandkassette"));
			rFactory.getCostUnitTypeMediumRepository().save(new CostUnitTypeMedium(4, "Diskette"));
			rFactory.getCostUnitTypeMediumRepository().save(new CostUnitTypeMedium(5, "Maschinenlesbarer Beleg"));
			rFactory.getCostUnitTypeMediumRepository().save(new CostUnitTypeMedium(6, "Nicht maschinenlesbarer Beleg"));
			rFactory.getCostUnitTypeMediumRepository().save(new CostUnitTypeMedium(7, "CD-ROM"));
			rFactory.getCostUnitTypeMediumRepository().save(new CostUnitTypeMedium(9, "Alle Datenträger (Schlüssel 2 bis 4 und 7)"));
			
			Logger.info("CostUnitTypeAssignment - KASSE_ART_VERKNUEPFUNG");
			rFactory.getCostUnitTypeAssignmentRepository().save(new CostUnitTypeAssignment(1, "Verweis vom IK der Versichertenkarte"));
			rFactory.getCostUnitTypeAssignmentRepository().save(new CostUnitTypeAssignment(2, "Verweis auf eine Datenannahmestelle (ohne Entschlüsselungsbefugnis)"));
			rFactory.getCostUnitTypeAssignmentRepository().save(new CostUnitTypeAssignment(3, "Verweis auf eine Datenannahmestelle (mit Entschlüsselungsbefugnis)"));
			rFactory.getCostUnitTypeAssignmentRepository().save(new CostUnitTypeAssignment(9, "Verweis auf eine Papierannahmestelle"));
			
			Logger.info("DTACostUnitSeperation - DTA_KASSENTRENNUNG");
			rFactory.getCostUnitSeparationRepository().save(new DTACostUnitSeparation(1, "AOK"));
			rFactory.getCostUnitSeparationRepository().save(new DTACostUnitSeparation(2, "Ersatzkassen"));
			rFactory.getCostUnitSeparationRepository().save(new DTACostUnitSeparation(3, "Ersatzkassen"));
			rFactory.getCostUnitSeparationRepository().save(new DTACostUnitSeparation(4, "Betriebskassen"));
			rFactory.getCostUnitSeparationRepository().save(new DTACostUnitSeparation(5, "Innungskrankenkassen"));
			rFactory.getCostUnitSeparationRepository().save(new DTACostUnitSeparation(6, "Bundesknappschaft"));
			rFactory.getCostUnitSeparationRepository().save(new DTACostUnitSeparation(6, "Landwirtschaftliche Krankenkassen"));
			rFactory.getCostUnitSeparationRepository().save(new DTACostUnitSeparation(7, "Seekrankenkassen"));
			rFactory.getCostUnitSeparationRepository().save(new DTACostUnitSeparation(8, "Bundeswehr"));
			rFactory.getCostUnitSeparationRepository().save(new DTACostUnitSeparation(9, "Sonstige"));
			
			//TODO
			Logger.info("DTAAccumulativeGroupKeyAccountinCode - DTA_SAGS_ABRECHNUNGSCODE");
			
			Logger.info("DTAAccountingCode - DTA_ABRECHNUNGSCODE"); 
			
			
			Logger.info("DTAAccumulativeGroupKey - SAGS");
			DTAAccumulativeGroupKey sags1 = new DTAAccumulativeGroupKey(1, "Leistungserbringer von Hilfsmitteln", "A", p302);
			DTAAccumulativeGroupKey sags2 = new DTAAccumulativeGroupKey(2, "Leistungserbringer von Heilmitteln", "B", p302);
			DTAAccumulativeGroupKey sags3 = new DTAAccumulativeGroupKey(3, "Leistungserbringer von häuslicher Krankenpflege", "C", p302);
			DTAAccumulativeGroupKey sags4 = new DTAAccumulativeGroupKey(4, "Leistungserbringer von Haushaltshilfe", "D", p302);
			DTAAccumulativeGroupKey sags5 = new DTAAccumulativeGroupKey(5, "Leistungserbringer von Krankentransportleistungen", "D", p302);
			DTAAccumulativeGroupKey sags6 = new DTAAccumulativeGroupKey(6, "Hebammen", "F", p302);
			DTAAccumulativeGroupKey sags7 = new DTAAccumulativeGroupKey(7, "nichtärztliche Dialysesachleistungen", "G", p302);
			DTAAccumulativeGroupKey sags8 = new DTAAccumulativeGroupKey(8, "Leistungserbringer von Rehabilitationssport", "H", p302);
			DTAAccumulativeGroupKey sags9 = new DTAAccumulativeGroupKey(9, "Leistungserbringer von Funktionstraining", "I", p302);
			DTAAccumulativeGroupKey sags10 = new DTAAccumulativeGroupKey(10, "Sonstige Leistungserbringer", "J", p302);
			DTAAccumulativeGroupKey sags11 = new DTAAccumulativeGroupKey(11, "Leistungserbringer Präventions- und Gesundheitsfördermaßnamen", "K", p302);
			DTAAccumulativeGroupKey sags12 = new DTAAccumulativeGroupKey(12, "Leistungserbringer vonergänzenden Rehamaßnamen / Amb.Rehamaßnamen / Mobile Rehaeinrichtungen", "L", p302);
			DTAAccumulativeGroupKey sags13 = new DTAAccumulativeGroupKey(13, "Sozialpädiaristische Zentren / Frühförderstellen", "M", p302);
			DTAAccumulativeGroupKey sags14 = new DTAAccumulativeGroupKey(14, "Soziotherapeutischer Leistungserbringer", "N", p302);
			DTAAccumulativeGroupKey sags15 = new DTAAccumulativeGroupKey(15, "Leistungserbringer für Arzneimittel und Apothekenübliche Wareb", null, apo);
			DTAAccumulativeGroupKey sags16 = new DTAAccumulativeGroupKey(16, "Leistungserbringer Pflegehilfsmittel", null, hpf);
			DTAAccumulativeGroupKey sags17 = new DTAAccumulativeGroupKey(17, "Leistungserbringer ambulante Pflege", null, hpf);
			DTAAccumulativeGroupKey sags18 = new DTAAccumulativeGroupKey(18, "Leistungserbringer Tagespflege", null, hpf);
			DTAAccumulativeGroupKey sags19 = new DTAAccumulativeGroupKey(19, "Leistungserbringer Nachtpflege", null, hpf);
			DTAAccumulativeGroupKey sags20 = new DTAAccumulativeGroupKey(20, "Leistungserbringer Kurzzeitpflege", null, hpf);
			DTAAccumulativeGroupKey sags21 = new DTAAccumulativeGroupKey(21, "Leistungserbringer vollständige Pflege", null, hpf);
			DTAAccumulativeGroupKey sags22 = new DTAAccumulativeGroupKey(22, "Leistungserbringer Sozialpädiaristische Zentren nach § 119 SGB V", null, hospital);
			DTAAccumulativeGroupKey sags23 = new DTAAccumulativeGroupKey(23, "Spezialisierte ambulante Paliativversorgung", "O", p302);
			DTAAccumulativeGroupKey sags24 = new DTAAccumulativeGroupKey(24, "gesundheitliche Versorgungsplanung nach § 132g SGB V", "P", p302);
			DTAAccumulativeGroupKey sags25 = new DTAAccumulativeGroupKey(25, "Kurzzeitpflege", "Q", p302);
			
			rFactory.getSAGSRepository().save(sags1);
			rFactory.getSAGSRepository().save(sags2);
			rFactory.getSAGSRepository().save(sags3);
			rFactory.getSAGSRepository().save(sags4);
			rFactory.getSAGSRepository().save(sags5);
			rFactory.getSAGSRepository().save(sags6);
			rFactory.getSAGSRepository().save(sags7);
			rFactory.getSAGSRepository().save(sags8);
			rFactory.getSAGSRepository().save(sags9);
			rFactory.getSAGSRepository().save(sags10);
			rFactory.getSAGSRepository().save(sags11);
			rFactory.getSAGSRepository().save(sags12);
			rFactory.getSAGSRepository().save(sags13);
			rFactory.getSAGSRepository().save(sags14);
			rFactory.getSAGSRepository().save(sags15);
			rFactory.getSAGSRepository().save(sags16);
			rFactory.getSAGSRepository().save(sags17);
			rFactory.getSAGSRepository().save(sags18);
			rFactory.getSAGSRepository().save(sags19);
			rFactory.getSAGSRepository().save(sags20);
			rFactory.getSAGSRepository().save(sags21);
			rFactory.getSAGSRepository().save(sags22);
			rFactory.getSAGSRepository().save(sags23);
			rFactory.getSAGSRepository().save(sags24);
			rFactory.getSAGSRepository().save(sags25);
		};
	}
}
