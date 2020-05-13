package costunitimport.segment;

import java.time.LocalDate;

import costunitimport.exception.InternalServiceApplication;
import costunitimport.model.address.Address;
import costunitimport.model.address.AddressType;

public class ANS extends Segment {

	private Integer kindOfAddress;
	private Integer zipCode;
	private String location;
	private String street;
	
	public ANS(String[] data) {
		super(data);
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
		address.setValidityFrom(validityFrom);
		address.setValidityUntil(validityUntil);
		address.setLocation(location);
		address.setZipCode(String.valueOf(zipCode));
		
		switch (kindOfAddress.intValue()) {
			case 1://Hausanschrift
				address.setStreet(street);
				address.setAddressType(AddressType.STREET);
				break;
			case 2://Postfachanschrift
				address.setPostBox(street);
				address.setAddressType(AddressType.MAIL_BOX);
				break;
			case 3://Großkundenanschrift
				address.setAddressType(AddressType.MAJOR_CLIENT);
				break;
			default:
				throw new InternalServiceApplication("Unbekannte Art der Anschrift! Art: " + kindOfAddress); 
		}
		return address;
	}
	
	public Integer getKindOfAddress() {
		return kindOfAddress;
	}

	public String getLocation() {
		return location;
	}

	public String getStreet() {
		return street;
	}
}
