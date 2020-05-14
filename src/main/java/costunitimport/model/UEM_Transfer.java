package costunitimport.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@Entity
@Table(name = "Übermittlungssegment")
@JsonRootName(value = "Übermittlungssegment")
@XmlRootElement(name = "Übermittlungssegment")
@XmlAccessorType(XmlAccessType.FIELD)
public class UEM_Transfer {
	@JsonIgnore
	private @Id @GeneratedValue Integer id;
	
	@JsonProperty("Art des Übermittlungsmediums")
	@XmlElement(name = "Art des Übermittlungsmediums")
	private String kindOfDataMedium;
	
	@OneToMany(cascade = {CascadeType.ALL})
	@JsonProperty("Übermittlungsdaten")
	@XmlElement(name = "Übermittlungsdaten")
	private List<DFU_Transmissionmedium> transmissionmedien;
	
	public List<DFU_Transmissionmedium> getTransmissionmedien() {
		return transmissionmedien;
	}

	public void addTransmissionmedien(DFU_Transmissionmedium transmissionmedium) {
		if(transmissionmedien == null) {
			transmissionmedien = new ArrayList<>();
		}
		this.transmissionmedien.add(transmissionmedium);
	}

	public String getKindOfDataMedium() {
		return kindOfDataMedium;
	}

	public void setKindOfDataMedium(String kindOfDataMedium) {
		this.kindOfDataMedium = kindOfDataMedium;
	}
}
