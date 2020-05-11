package costunitimport.segment;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import costunitimport.dao.factory.RepositoryFactory;
import costunitimport.exception.InternalServiceApplication;
import costunitimport.model.address.Address;
import costunitimport.model.address.AddressType;
import costunitimport.model.address.Country;
import costunitimport.model.address.FederalState;
import costunitimport.model.address.Zip;
import costunitimport.model.address.ZipType;
import costunitimport.util.TextFormatter;

public class ANS extends Segment {

	private Integer kindOfAddress;
	private Integer zipCode;
	private String location;
	private String street;
	
	private final RepositoryFactory rFactory;

	public ANS(String[] data, RepositoryFactory rFactory) {
		super(data);
		this.rFactory = rFactory;
	}

	@Override
	protected void assignData() {
		int position = 1;
		kindOfAddress = getData(position++, Integer.class);
		zipCode = getData(position++, Integer.class);
		location = getData(position++, String.class);
		street = getData(position, String.class);
	}
	
	public Address buildAddress(LocalDate validityFrom, LocalDate validityUntil) {
		Address address = new Address();
		switch (kindOfAddress.intValue()) {
			case 1://Hausanschrift
				address.setStreet(street);
				address.setZip(buildZip());
				address.setAddressType(AddressType.STREET);
				break;
			case 2://Postfachanschrift
				address.setPostBox(street);
				address.setZip(buildZip());
				address.setAddressType(AddressType.MAIL_BOX);
				break;
			case 3://Großkundenanschrift
				address.setAddressType(AddressType.MAJOR_CLIENT);
				break;
			default:
				throw new InternalServiceApplication("Unbekannte Art der Anschrift! Art: " + kindOfAddress); 
		}
		address.setValidityFrom(validityFrom);
		address.setValidityUntil(validityUntil);
		return address;
	}
	
	private Zip buildZip() {
		String zipCode = getZipCode().toString();
		Optional<Zip> zip = rFactory.getZipRepository().findByZipCodeAndLocation(zipCode, location);
		if(zip.isEmpty()) {
			//Wenn kein Zip-Objekt ermittelt werden kann, dann wird einfach eine PLZ angelegt 
			//-> ANS+2+00560+Münchberg+95205' 
			Zip newZip = new Zip();
			newZip.setZipCode(zipCode);
			newZip.setLocation(location);
			newZip.setCountry(rFactory.getCountryRepository().findById(Country.GERMANY).orElse(null));
			newZip.setFederalState(rFactory.getFederalStateRepository().findById(FederalState.UNKNOWN_FEDERAL_STATE_ID).orElse(null));
			newZip.setZipType(rFactory.getZipTypeRepository().findById(ZipType.IMPORT_INTERFACE_UNCHECKED).orElse(null));
			return newZip;
		}
		return zip.get();
	}

	/**
	 * Art der Anschrift<br>
	 * Schlüssel Art der Anschrift 
	 * @return Art der Anschrift 
	 */
	public Integer getKindOfAddress() {
		return kindOfAddress;
	}

	/**
	 * Postleitzahl 
	 * @return Postleitzahl 
	 */
	public Integer getZipCode() {
		String zipCodeStr = zipCode.toString();
		if(zipCodeStr.length()<5) {
			try {
				zipCodeStr = TextFormatter.convertString(zipCodeStr, 5, false, '0');
			} catch (IOException e) {
				throw new InternalServiceApplication("Fehlerhafte Verarbeitung PLZ! " +zipCodeStr, e); 
			}
		}
		return Integer.valueOf(zipCodeStr);
	}

	/**
	 * Ort 
	 * @return Ort 
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Straße, Hausnr. / Postfach<br>
	 * Straße und Hausnr. oder Postfac
	 * @return Straße, Hausnr. / Postfach 
	 */
	public String getStreet() {
		return street;
	}

}
