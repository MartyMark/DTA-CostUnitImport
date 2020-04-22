package costunitimport.model;

import java.time.LocalDate;
import javax.persistence.Id;

public class Address {
	
	@Id 
	private Integer id;
    
	private Integer ik;
	private Zip zip;
	private String street;
	private String postBox;
	private LocalDate validityFrom;
	private LocalDate validityUntil;

	public Integer getIk() {
		return ik;
	}
	
	public void setIk(Integer ik) {
		this.ik = ik;
	}
	
	public Zip getZip() {
		return zip;
	}
	
	public void setZip(Zip zip) {
		this.zip = zip;
	}
	
	public String getStreet() {
		return street;
	}
	
	public void setStreet(String street) {
		this.street = street;
	}
	
	public String getPostBox() {
		return postBox;
	}
	
	public void setPostBox(String postBox) {
		this.postBox = postBox;
	}
	
	public LocalDate getValidityFrom() {
		return validityFrom;
	}
	
	public void setValidityFrom(LocalDate validityFrom) {
		this.validityFrom = validityFrom;
	}
	
	public LocalDate getValidityUntil() {
		return validityUntil;
	}
	
	public void setValidityUntil(LocalDate validityUntil) {
		this.validityUntil = validityUntil;
	}
}
