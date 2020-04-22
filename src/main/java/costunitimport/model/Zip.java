package costunitimport.model;

import javax.persistence.Id;

public class Zip {
	
	@Id
	private Integer id;
	
	private String zipCode;
	private Country country;
	private FederalState federalState;
	private String location;
	private ZipType zipType;
	
	public ZipType getZipType() {
		return zipType;
	}

	public void setZipType(ZipType zipType) {
		this.zipType = zipType;
	}

	public String getZipCode() {
		return zipCode;
	}
	
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	public Country getCountry() {
		return country;
	}
	
	public void setCountry(Country country) {
		this.country = country;
	}
	
	public FederalState getFederalState() {
		return federalState;
	}
	
	public void setFederalState(FederalState federalState) {
		this.federalState = federalState;
	}

	public Integer getId() {
		return id;
	}
	
    public void setLocation(String location) {
		this.location = location;
	}
    
    public String getLocation() {
		return location;
	}
}
