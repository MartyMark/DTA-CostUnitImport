package costunitimport;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import costunitimport.dao.factory.RepositoryFactory;
import costunitimport.logger.Logger;
import costunitimport.model.CareProviderMethod;
import costunitimport.model.CostUnitTypeAssignment;
import costunitimport.model.CostUnitTypeDataSupply;
import costunitimport.model.CostUnitTypeMedium;
import costunitimport.model.DTAAccountingCode;
import costunitimport.model.DTACostUnitSeparation;
import costunitimport.model.address.Country;
import costunitimport.model.address.FederalState;
import costunitimport.model.address.ZipType;
import costunitimport.model.sags.DTAAccumulativeGroupKey;
import costunitimport.model.sags.DTAAccumulativeGroupKeyAccountinCode;

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
			
			Logger.info("CostUnitTypeDataSupply - KASSE_ART_DATENLIEFERUNG");
			rFactory.getCostUnitTypeDataSupplyRepository().save(new CostUnitTypeDataSupply(7, "Rechnungs-und Abrechnungsdaten SLGA und SLLA digitalisiert"));
			rFactory.getCostUnitTypeDataSupplyRepository().save(new CostUnitTypeDataSupply(21, "Rechnung (Papier)"));
			rFactory.getCostUnitTypeDataSupplyRepository().save(new CostUnitTypeDataSupply(24, "maschinenlesbarer Beleg"));
			rFactory.getCostUnitTypeDataSupplyRepository().save(new CostUnitTypeDataSupply(26, "Verordnung (Papier)"));
			rFactory.getCostUnitTypeDataSupplyRepository().save(new CostUnitTypeDataSupply(27, "Kostenvoranschlag (Papier)"));
			rFactory.getCostUnitTypeDataSupplyRepository().save(new CostUnitTypeDataSupply(28, "papiergebundene Unterlagen einer digitalen Abrechnung (Verordnung, ggf. Kostenvoranschlag, ggf. Rechnung)"));
			rFactory.getCostUnitTypeDataSupplyRepository().save(new CostUnitTypeDataSupply(29, "maschinenlesbarer Beleg einschließlich der dazugehöri-gen Abrechnungsunterlagen"));
			
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
			
			Logger.info("DTAAccumulativeGroupKeyAccountinCode - DTA_SAGS_ABRECHNUNGSCODE");
			DTAAccumulativeGroupKeyAccountinCode sagscode1 = new DTAAccumulativeGroupKeyAccountinCode(1, 11);
			DTAAccumulativeGroupKeyAccountinCode sagscode2 = new DTAAccumulativeGroupKeyAccountinCode(1, 12);
			DTAAccumulativeGroupKeyAccountinCode sagscode3 = new DTAAccumulativeGroupKeyAccountinCode(1, 13);
			DTAAccumulativeGroupKeyAccountinCode sagscode4 = new DTAAccumulativeGroupKeyAccountinCode(1, 14);
			DTAAccumulativeGroupKeyAccountinCode sagscode5 = new DTAAccumulativeGroupKeyAccountinCode(1, 15);
			DTAAccumulativeGroupKeyAccountinCode sagscode6 = new DTAAccumulativeGroupKeyAccountinCode(1, 16);
			DTAAccumulativeGroupKeyAccountinCode sagscode7 = new DTAAccumulativeGroupKeyAccountinCode(1, 17);
			DTAAccumulativeGroupKeyAccountinCode sagscode8 = new DTAAccumulativeGroupKeyAccountinCode(1, 19);
			DTAAccumulativeGroupKeyAccountinCode sagscode9 = new DTAAccumulativeGroupKeyAccountinCode(2, 21);
			DTAAccumulativeGroupKeyAccountinCode sagscode10 = new DTAAccumulativeGroupKeyAccountinCode(2, 22);
			DTAAccumulativeGroupKeyAccountinCode sagscode11 = new DTAAccumulativeGroupKeyAccountinCode(2, 23);
			DTAAccumulativeGroupKeyAccountinCode sagscode12 = new DTAAccumulativeGroupKeyAccountinCode(2, 24);
			DTAAccumulativeGroupKeyAccountinCode sagscode13 = new DTAAccumulativeGroupKeyAccountinCode(2, 25);
			DTAAccumulativeGroupKeyAccountinCode sagscode14 = new DTAAccumulativeGroupKeyAccountinCode(2, 26);
			DTAAccumulativeGroupKeyAccountinCode sagscode15 = new DTAAccumulativeGroupKeyAccountinCode(2, 27);
			DTAAccumulativeGroupKeyAccountinCode sagscode16 = new DTAAccumulativeGroupKeyAccountinCode(2, 28);
			DTAAccumulativeGroupKeyAccountinCode sagscode17 = new DTAAccumulativeGroupKeyAccountinCode(2, 29);
			DTAAccumulativeGroupKeyAccountinCode sagscode18 = new DTAAccumulativeGroupKeyAccountinCode(2, 71);
			DTAAccumulativeGroupKeyAccountinCode sagscode19 = new DTAAccumulativeGroupKeyAccountinCode(2, 72);
			DTAAccumulativeGroupKeyAccountinCode sagscode20 = new DTAAccumulativeGroupKeyAccountinCode(2, 73);
			DTAAccumulativeGroupKeyAccountinCode sagscode21 = new DTAAccumulativeGroupKeyAccountinCode(2, 74);
			DTAAccumulativeGroupKeyAccountinCode sagscode22 = new DTAAccumulativeGroupKeyAccountinCode(3, 31);
			DTAAccumulativeGroupKeyAccountinCode sagscode23 = new DTAAccumulativeGroupKeyAccountinCode(3, 32);
			DTAAccumulativeGroupKeyAccountinCode sagscode24 = new DTAAccumulativeGroupKeyAccountinCode(3, 33);
			DTAAccumulativeGroupKeyAccountinCode sagscode25 = new DTAAccumulativeGroupKeyAccountinCode(3, 34);
			DTAAccumulativeGroupKeyAccountinCode sagscode26 = new DTAAccumulativeGroupKeyAccountinCode(4, 31);
			DTAAccumulativeGroupKeyAccountinCode sagscode27 = new DTAAccumulativeGroupKeyAccountinCode(4, 32);
			DTAAccumulativeGroupKeyAccountinCode sagscode28 = new DTAAccumulativeGroupKeyAccountinCode(4, 33);
			DTAAccumulativeGroupKeyAccountinCode sagscode29 = new DTAAccumulativeGroupKeyAccountinCode(4, 34);
			DTAAccumulativeGroupKeyAccountinCode sagscode30 = new DTAAccumulativeGroupKeyAccountinCode(5, 41);
			DTAAccumulativeGroupKeyAccountinCode sagscode31 = new DTAAccumulativeGroupKeyAccountinCode(5, 42);
			DTAAccumulativeGroupKeyAccountinCode sagscode32 = new DTAAccumulativeGroupKeyAccountinCode(5, 43);
			DTAAccumulativeGroupKeyAccountinCode sagscode33 = new DTAAccumulativeGroupKeyAccountinCode(5, 44);
			DTAAccumulativeGroupKeyAccountinCode sagscode34 = new DTAAccumulativeGroupKeyAccountinCode(5, 45);
			DTAAccumulativeGroupKeyAccountinCode sagscode35 = new DTAAccumulativeGroupKeyAccountinCode(5, 46);
			DTAAccumulativeGroupKeyAccountinCode sagscode36 = new DTAAccumulativeGroupKeyAccountinCode(5, 47);
			DTAAccumulativeGroupKeyAccountinCode sagscode37 = new DTAAccumulativeGroupKeyAccountinCode(5, 48);
			DTAAccumulativeGroupKeyAccountinCode sagscode38 = new DTAAccumulativeGroupKeyAccountinCode(5, 49);
			DTAAccumulativeGroupKeyAccountinCode sagscode39 = new DTAAccumulativeGroupKeyAccountinCode(6, 50);
			DTAAccumulativeGroupKeyAccountinCode sagscode40 = new DTAAccumulativeGroupKeyAccountinCode(7, 55);
			DTAAccumulativeGroupKeyAccountinCode sagscode41 = new DTAAccumulativeGroupKeyAccountinCode(7, 56);
			DTAAccumulativeGroupKeyAccountinCode sagscode42 = new DTAAccumulativeGroupKeyAccountinCode(7, 57);
			DTAAccumulativeGroupKeyAccountinCode sagscode43 = new DTAAccumulativeGroupKeyAccountinCode(8, 61);
			DTAAccumulativeGroupKeyAccountinCode sagscode44 = new DTAAccumulativeGroupKeyAccountinCode(9, 62);
			DTAAccumulativeGroupKeyAccountinCode sagscode45 = new DTAAccumulativeGroupKeyAccountinCode(10, 65);
			DTAAccumulativeGroupKeyAccountinCode sagscode46 = new DTAAccumulativeGroupKeyAccountinCode(11, 66);
			DTAAccumulativeGroupKeyAccountinCode sagscode47 = new DTAAccumulativeGroupKeyAccountinCode(12, 63);
			DTAAccumulativeGroupKeyAccountinCode sagscode48 = new DTAAccumulativeGroupKeyAccountinCode(12, 67);
			DTAAccumulativeGroupKeyAccountinCode sagscode49 = new DTAAccumulativeGroupKeyAccountinCode(13, 68);
			DTAAccumulativeGroupKeyAccountinCode sagscode50 = new DTAAccumulativeGroupKeyAccountinCode(14, 69);
			DTAAccumulativeGroupKeyAccountinCode sagscode51 = new DTAAccumulativeGroupKeyAccountinCode(15, 8);
			DTAAccumulativeGroupKeyAccountinCode sagscode52 = new DTAAccumulativeGroupKeyAccountinCode(16, 11);
			DTAAccumulativeGroupKeyAccountinCode sagscode53 = new DTAAccumulativeGroupKeyAccountinCode(16, 15);
			DTAAccumulativeGroupKeyAccountinCode sagscode54 = new DTAAccumulativeGroupKeyAccountinCode(16, 16);
			DTAAccumulativeGroupKeyAccountinCode sagscode55 = new DTAAccumulativeGroupKeyAccountinCode(16, 17);
			DTAAccumulativeGroupKeyAccountinCode sagscode56 = new DTAAccumulativeGroupKeyAccountinCode(16, 19);
			DTAAccumulativeGroupKeyAccountinCode sagscode57 = new DTAAccumulativeGroupKeyAccountinCode(17, 35);
			DTAAccumulativeGroupKeyAccountinCode sagscode58 = new DTAAccumulativeGroupKeyAccountinCode(17, 36);
			DTAAccumulativeGroupKeyAccountinCode sagscode59 = new DTAAccumulativeGroupKeyAccountinCode(17, 37);
			DTAAccumulativeGroupKeyAccountinCode sagscode60 = new DTAAccumulativeGroupKeyAccountinCode(17, 39);
			DTAAccumulativeGroupKeyAccountinCode sagscode61 = new DTAAccumulativeGroupKeyAccountinCode(18, 81);
			DTAAccumulativeGroupKeyAccountinCode sagscode62 = new DTAAccumulativeGroupKeyAccountinCode(18, 82);
			DTAAccumulativeGroupKeyAccountinCode sagscode63 = new DTAAccumulativeGroupKeyAccountinCode(18, 83);
			DTAAccumulativeGroupKeyAccountinCode sagscode64 = new DTAAccumulativeGroupKeyAccountinCode(18, 84);
			DTAAccumulativeGroupKeyAccountinCode sagscode65 = new DTAAccumulativeGroupKeyAccountinCode(19, 86);
			DTAAccumulativeGroupKeyAccountinCode sagscode66 = new DTAAccumulativeGroupKeyAccountinCode(19, 87);
			DTAAccumulativeGroupKeyAccountinCode sagscode67 = new DTAAccumulativeGroupKeyAccountinCode(19, 88);
			DTAAccumulativeGroupKeyAccountinCode sagscode68 = new DTAAccumulativeGroupKeyAccountinCode(19, 89);
			DTAAccumulativeGroupKeyAccountinCode sagscode69 = new DTAAccumulativeGroupKeyAccountinCode(20, 91);
			
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode1);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode2); 
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode3); 
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode4);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode5); 
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode6); 
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode7); 
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode8); 
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode9); 
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode10);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode11);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode12);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode13);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode14);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode15);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode16);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode17);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode18);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode19);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode20);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode21);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode22);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode23);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode24);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode25);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode26);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode27);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode28);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode29);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode30);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode31);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode32);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode33);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode34);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode35);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode36);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode37);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode38);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode39);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode40);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode41);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode42);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode43);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode44);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode45);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode46);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode47);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode48);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode49);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode50);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode51);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode52);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode53);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode54);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode55);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode56);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode57);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode58);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode59);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode60);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode61);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode62);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode63);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode64);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode65);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode66);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode67);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode68);
			rFactory.getAccumulativeGroupKeyAccountingCodeRepository().save(sagscode69);
			
			Logger.info("DTAAccountingCode - DTA_ABRECHNUNGSCODE"); 
			DTAAccountingCode code1 = new DTAAccountingCode(8, "Leistungserbringer für Arzneimittel und apothekenübliche Waren");
			DTAAccountingCode code2 = new DTAAccountingCode(11, "Apotheke");
			DTAAccountingCode code3 = new DTAAccountingCode(12, "Augenoptiker");
			DTAAccountingCode code4 = new DTAAccountingCode(13, "Augenarzt");
			DTAAccountingCode code5 = new DTAAccountingCode(14, "Hörgeräteakustiker");
			DTAAccountingCode code6 = new DTAAccountingCode(15, "Orthopädiemechaniker, Bandagist, Sanitätshaus");
			DTAAccountingCode code7 = new DTAAccountingCode(16, "Orthopädieschumacher");
			DTAAccountingCode code8 = new DTAAccountingCode(17, "Orthopäde");
			DTAAccountingCode code9 = new DTAAccountingCode(19, "sonstige Hilfsmittellieferant");
			DTAAccountingCode code10 = new DTAAccountingCode(21, "Masseur/Medizinischer Badebetrieb");
			DTAAccountingCode code11 = new DTAAccountingCode(22, "Krankengymnast/Physiotherapeut");
			DTAAccountingCode code12 = new DTAAccountingCode(23, "Logopäde/Stimmlehrer/Sprachtherapeut");
			DTAAccountingCode code13 = new DTAAccountingCode(24, "Sprachheilpädagoge/Dipl.Pädagoge");
			DTAAccountingCode code14 = new DTAAccountingCode(25, "Sonstiger Sprachtherapeut");
			DTAAccountingCode code15 = new DTAAccountingCode(26, "Ergotherapeut");
			DTAAccountingCode code16 = new DTAAccountingCode(27, "Krankenhaus");
			DTAAccountingCode code17 = new DTAAccountingCode(28, "Kurbetrieb");
			DTAAccountingCode code18 = new DTAAccountingCode(29, "Sonstige therapeutische Heilperson");
			DTAAccountingCode code19 = new DTAAccountingCode(31, "freigemeinnützige Anbieter (Sozialstationen)");
			DTAAccountingCode code20 = new DTAAccountingCode(32, "privatgewerbliche Anbieter");
			DTAAccountingCode code21 = new DTAAccountingCode(33, "öffentliche Anbieter");
			DTAAccountingCode code22 = new DTAAccountingCode(34, "Sonstige Pflegedienste");
			DTAAccountingCode code23 = new DTAAccountingCode(35, "frei gemeinnütziger Anbieter (Sozialstation)");
			DTAAccountingCode code24 = new DTAAccountingCode(36, "privat gewerblicher Anbieter");
			DTAAccountingCode code25 = new DTAAccountingCode(37, "öffentlicher Anbieter");
			DTAAccountingCode code26 = new DTAAccountingCode(39, "sonstiger Pflegedienst");
			DTAAccountingCode code27 = new DTAAccountingCode(41, "Öffentliche Anbieter von qualifizierten Krankentransportleistungen");
			DTAAccountingCode code28 = new DTAAccountingCode(42, "Deutsches Rotes Kreuz (DRK)");
			DTAAccountingCode code29 = new DTAAccountingCode(43, "Arbeiter-Samariter-Bund (ASB)");
			DTAAccountingCode code30 = new DTAAccountingCode(44, "Johanniter-Unfall-Hilfe (JUH)");
			DTAAccountingCode code31 = new DTAAccountingCode(45, "Malteser-Hilfsdienst");
			DTAAccountingCode code32 = new DTAAccountingCode(46, "Sonstige Leistungserbringer von nichtqualifizierten Krankentransportleistungen");
			DTAAccountingCode code33 = new DTAAccountingCode(47, "Leistungerbringer von Flugrettungs- und Transportleistungen");
			DTAAccountingCode code34 = new DTAAccountingCode(48, "Sonstiger nichtöffentlicher Anbieter von qualifizierten Krankentransport- bzw. Rettungsdienstleistung");
			DTAAccountingCode code35 = new DTAAccountingCode(49, "Sonstiger Anbieter von Krankentransportleistungen");
			DTAAccountingCode code36 = new DTAAccountingCode(50, "Hebammen");
			DTAAccountingCode code37 = new DTAAccountingCode(55, "Sonstige Leistungserbringer von nichtärzlichen Dialysesachleistungen");
			DTAAccountingCode code38 = new DTAAccountingCode(56, "Kuratorium für Heimdialyse (KfH)");
			DTAAccountingCode code39 = new DTAAccountingCode(57, "Patienten-Heimversorgung (PHV)");
			DTAAccountingCode code40 = new DTAAccountingCode(60, "Betriebshilfe");
			DTAAccountingCode code41 = new DTAAccountingCode(61, "Leistungserbringer von Rehabilitationssport");
			DTAAccountingCode code42 = new DTAAccountingCode(62, "Leistungserbringer von Funktionstraining");
			DTAAccountingCode code43 = new DTAAccountingCode(63, "Leistungserbringer für ergänzende Rehabilitationsmaßnahmen");
			DTAAccountingCode code44 = new DTAAccountingCode(65, "Sonstige Leistungserbringer");
			DTAAccountingCode code45 = new DTAAccountingCode(66, "Leistungserbringer von Präventions- und Gesundheitsförderungsmaßnahmen im Rahmen von amb. Vorsorgele");
			DTAAccountingCode code46 = new DTAAccountingCode(67, "Ambulantes Rehazentrum / Mobile Rehabilitationseinrichtung");
			DTAAccountingCode code47 = new DTAAccountingCode(68, "Sozialpädiatrische Zentren / Frühförderstellen");
			DTAAccountingCode code48 = new DTAAccountingCode(69, "Soziotherapeutischer Leistungserbringer");
			DTAAccountingCode code49 = new DTAAccountingCode(71, "Podologen");
			DTAAccountingCode code50 = new DTAAccountingCode(72, "Med.Fußpfleger");
			DTAAccountingCode code51 = new DTAAccountingCode(73, "Leistungserbringer von Ernährungstherapie für seltene angeborene Stoffwechselerkrankungen");
			DTAAccountingCode code52 = new DTAAccountingCode(74, "Leistungserbringer von Ernährungstherapie für Mukoviszidose");
			DTAAccountingCode code53 = new DTAAccountingCode(75, "SAPV");
			DTAAccountingCode code54 = new DTAAccountingCode(76, "Leistungserbringer nach § 132g SGB V");
			
			DTAAccountingCode code71 = new DTAAccountingCode(81, "privatgewerblicher Anbieter");
			
			DTAAccountingCode code56 = new DTAAccountingCode(82, "frei gemeinnütziger Anbieter (gemeinnützige private Anbieter");
			DTAAccountingCode code57 = new DTAAccountingCode(83, "öffentlicher Anbieter");
			DTAAccountingCode code58 = new DTAAccountingCode(84, "sonstige Pflegeeinrichtung");
			DTAAccountingCode code59 = new DTAAccountingCode(86, "privat gewerblicher Anbieter");
			DTAAccountingCode code60 = new DTAAccountingCode(87, "frei gemeinnütziger Anbieter (gemeinnützige private Anbieter)");
			DTAAccountingCode code61 = new DTAAccountingCode(88, "öffentlicher Anbieter");
			DTAAccountingCode code62 = new DTAAccountingCode(89, "sonstige Pflegeeinrichtung");
			DTAAccountingCode code63 = new DTAAccountingCode(91, "privat gewerblicher Anbieter");
			DTAAccountingCode code64 = new DTAAccountingCode(92, "frei gemeinnütziger Anbieter (gemeinnützige private Anbieter)");
			DTAAccountingCode code65 = new DTAAccountingCode(93, "öffentlicher Anbieter");
			DTAAccountingCode code66 = new DTAAccountingCode(94, "sonstige Pflegeeinrichtung");
			DTAAccountingCode code67 = new DTAAccountingCode(96, "privat gewerblicher Anbieter");
			DTAAccountingCode code68 = new DTAAccountingCode(97, "frei gemeinnütziger Anbieter (gemeinnützige private Anbieter)");
			DTAAccountingCode code69 = new DTAAccountingCode(98, "öffentlicher Anbieter");
			DTAAccountingCode code70 = new DTAAccountingCode(99, "sonstige Pflegeeinrichtung");
			
			rFactory.getAccountingCodeRepository().save(code1); 
			rFactory.getAccountingCodeRepository().save(code2); 
			rFactory.getAccountingCodeRepository().save(code3);
			rFactory.getAccountingCodeRepository().save(code4); 
			rFactory.getAccountingCodeRepository().save(code5); 
			rFactory.getAccountingCodeRepository().save(code6); 
			rFactory.getAccountingCodeRepository().save(code7); 
			rFactory.getAccountingCodeRepository().save(code8); 
			rFactory.getAccountingCodeRepository().save(code9); 
			rFactory.getAccountingCodeRepository().save(code10);
			rFactory.getAccountingCodeRepository().save(code11);
			rFactory.getAccountingCodeRepository().save(code12);
			rFactory.getAccountingCodeRepository().save(code13);
			rFactory.getAccountingCodeRepository().save(code14);
			rFactory.getAccountingCodeRepository().save(code15);
			rFactory.getAccountingCodeRepository().save(code16);
			rFactory.getAccountingCodeRepository().save(code17);
			rFactory.getAccountingCodeRepository().save(code18);
			rFactory.getAccountingCodeRepository().save(code19);
			rFactory.getAccountingCodeRepository().save(code20);
			rFactory.getAccountingCodeRepository().save(code21);
			rFactory.getAccountingCodeRepository().save(code22);
			rFactory.getAccountingCodeRepository().save(code23);
			rFactory.getAccountingCodeRepository().save(code24);
			rFactory.getAccountingCodeRepository().save(code25);
			rFactory.getAccountingCodeRepository().save(code26);
			rFactory.getAccountingCodeRepository().save(code27);
			rFactory.getAccountingCodeRepository().save(code28);
			rFactory.getAccountingCodeRepository().save(code29);
			rFactory.getAccountingCodeRepository().save(code30);
			rFactory.getAccountingCodeRepository().save(code31);
			rFactory.getAccountingCodeRepository().save(code32);
			rFactory.getAccountingCodeRepository().save(code33);
			rFactory.getAccountingCodeRepository().save(code34);
			rFactory.getAccountingCodeRepository().save(code35);
			rFactory.getAccountingCodeRepository().save(code36);
			rFactory.getAccountingCodeRepository().save(code37);
			rFactory.getAccountingCodeRepository().save(code38);
			rFactory.getAccountingCodeRepository().save(code39);
			rFactory.getAccountingCodeRepository().save(code40);
			rFactory.getAccountingCodeRepository().save(code41);
			rFactory.getAccountingCodeRepository().save(code42);
			rFactory.getAccountingCodeRepository().save(code43);
			rFactory.getAccountingCodeRepository().save(code44);
			rFactory.getAccountingCodeRepository().save(code45);
			rFactory.getAccountingCodeRepository().save(code46);
			rFactory.getAccountingCodeRepository().save(code47);
			rFactory.getAccountingCodeRepository().save(code48);
			rFactory.getAccountingCodeRepository().save(code49);
			rFactory.getAccountingCodeRepository().save(code50);
			rFactory.getAccountingCodeRepository().save(code51);
			rFactory.getAccountingCodeRepository().save(code52);
			rFactory.getAccountingCodeRepository().save(code53);
			rFactory.getAccountingCodeRepository().save(code54);
			
			rFactory.getAccountingCodeRepository().save(code71);
			
			rFactory.getAccountingCodeRepository().save(code56);
			rFactory.getAccountingCodeRepository().save(code57);
			rFactory.getAccountingCodeRepository().save(code58);
			rFactory.getAccountingCodeRepository().save(code59);
			rFactory.getAccountingCodeRepository().save(code60);
			rFactory.getAccountingCodeRepository().save(code61);
			rFactory.getAccountingCodeRepository().save(code62);
			rFactory.getAccountingCodeRepository().save(code63);
			rFactory.getAccountingCodeRepository().save(code64);
			rFactory.getAccountingCodeRepository().save(code65);
			rFactory.getAccountingCodeRepository().save(code66);
			rFactory.getAccountingCodeRepository().save(code67);
			rFactory.getAccountingCodeRepository().save(code68);
			rFactory.getAccountingCodeRepository().save(code69);
			rFactory.getAccountingCodeRepository().save(code70);
			
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
