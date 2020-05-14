package costunitimport.model;

import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@Entity
@Table(name = "Datenfernübertragungssegment")
@JsonRootName(value = "Datenfernübertragungssegment")
@XmlRootElement(name = "Datenfernübertragungssegment")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(Include.NON_NULL)
public class DFU_Transmissionmedium {
	
	@JsonIgnore
	private @Id @GeneratedValue Integer id;
	
	@JsonProperty("Benutzerkennung")
	@XmlElement(name = "Benutzerkennung")
	private String userName;
	
	@JsonProperty("Übertragung von")
	@XmlElement(name = "Übertragung von")
	private LocalTime transferTimeFrom;
	
	@JsonProperty("Übertragung bis")
	@XmlElement(name = "Übertragung bis")
	private LocalTime transferTimeUntil;
	
	@JsonProperty("Übertragungstage")
	@XmlElement(name = "Übertragungstage")
	private String transferDays;
	
	@JsonProperty("Kommunikationskanal")
	@XmlElement(name = "Kommunikationskanal")
	private String commuinicationChannel;

	public Integer getId() {
		return id;
	}

	public String getUserName() {
		return userName;
	}

	public LocalTime getTransferTimeFrom() {
		return transferTimeFrom;
	}

	public LocalTime getTransferTimeUntil() {
		return transferTimeUntil;
	}

	public String getTransferDays() {
		return transferDays;
	}

	public String getCommuinicationChannel() {
		return commuinicationChannel;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setTransferTimeFrom(LocalTime transferTimeFrom) {
		this.transferTimeFrom = transferTimeFrom;
	}

	public void setTransferTimeUntil(LocalTime transferTimeUntil) {
		this.transferTimeUntil = transferTimeUntil;
	}

	public void setTransferDays(String transferDays) {
		this.transferDays = transferDays;
	}

	public void setCommuinicationChannel(String commuinicationChannel) {
		this.commuinicationChannel = commuinicationChannel;
	}
}
