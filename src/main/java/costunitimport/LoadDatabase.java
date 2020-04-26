package costunitimport;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import costunitimport.dao.factory.RepositoryFactory;
import costunitimport.logger.Logger;
import costunitimport.model.CareProviderMethod;
import costunitimport.model.CostUnitTypeMedium;
import costunitimport.model.DTACostUnitSeparation;
import costunitimport.model.address.Country;
import costunitimport.model.address.FederalState;
import costunitimport.model.address.ZipType;

@Configuration
public class LoadDatabase {
	
	@Bean
	CommandLineRunner initDatabase(RepositoryFactory rFactory) {
		return args -> {
			Logger.info("Country - STAAT");
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
			
			CareProviderMethod p302 = new CareProviderMethod(5, "Sonstige Leistungserbringer", "Abrechnung nach §302", true);
			rFactory.getCareProviderMethodRepository().save(p302);
			
			Logger.info("CostUnitTypeMedium - KASSEN_ART_MEDIUM");
			
			rFactory.getCostUnitTypeMediumRepository().save(new CostUnitTypeMedium(1, "DFÜ"));
			rFactory.getCostUnitTypeMediumRepository().save(new CostUnitTypeMedium(2, "Magnetband"));
			rFactory.getCostUnitTypeMediumRepository().save(new CostUnitTypeMedium(3, "Magnetbandkassette"));
			rFactory.getCostUnitTypeMediumRepository().save(new CostUnitTypeMedium(4, "Diskette"));
			rFactory.getCostUnitTypeMediumRepository().save(new CostUnitTypeMedium(5, "Maschinenlesbarer Beleg"));
			rFactory.getCostUnitTypeMediumRepository().save(new CostUnitTypeMedium(6, "Nicht maschinenlesbarer Beleg"));
			rFactory.getCostUnitTypeMediumRepository().save(new CostUnitTypeMedium(7, "CD-ROM"));
			rFactory.getCostUnitTypeMediumRepository().save(new CostUnitTypeMedium(9, "Alle Datenträger (Schlüssel 2 bis 4 und 7)"));
			
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
		
			Logger.info("SAGS - SAGS");
			
			
		};
	}
}
