package costunitimport.segment;

import java.time.LocalTime;

public class DFU extends Segment {

	private Integer sequentialNumber;
	private Integer transmissionProtocol;
	private String userName;
	private LocalTime transferTimeFrom;
	private LocalTime transferTimeUntil;
	private Integer transferDays;
	private String commuinicationChannel;

	public DFU(String[] data) {
		super(data);
	}

	@Override
	protected void assignData() {
		int position = 1;
		sequentialNumber = getData(position++, Integer.class);
		transmissionProtocol = getData(position++, Integer.class);
		userName = getData(position++, String.class);
		transferTimeFrom = getData(position++, LocalTime.class);
		transferTimeUntil = getData(position++, LocalTime.class);
		transferDays = getData(position++, Integer.class);
		commuinicationChannel = getData(position, String.class);
	}

	/**
	 * Laufende Nummer<br>
	 * Numerierung innerhalb UEM
	 * 
	 * @return Laufende Nummer
	 */
	public Integer getSequentialNumber() {
		return sequentialNumber;
	}

	/**
	 * Übertragungsprotokoll<br>
	 * Schlüssel DFÜProtokoll
	 * 
	 * @return Übertragungsprotokoll
	 */
	public Integer getTransmissionProtocol() {
		return transmissionProtocol;
	}

	/**
	 * Benutzerkennung<br>
	 * wenn abweichend vom IK
	 * 
	 * @return Benutzerkennung
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Übertragung von<br>
	 * Zeitfenster von (HHMM)
	 * 
	 * @return Übertragung von
	 */
	public LocalTime getTransferTimeFrom() {
		return transferTimeFrom;
	}

	/**
	 * Übertragung bis <br>
	 * Zeitfenster bis (HHMM)
	 * 
	 * @return Übertragung bis
	 */
	public LocalTime getTransferTimeUntil() {
		return transferTimeUntil;
	}

	/**
	 * Übertragungstage <br>
	 * Schlüssel Übertragungstage
	 * 
	 * @return Übertragungstage
	 */
	public Integer getTransferDays() {
		return transferDays;
	}

	/**
	 * Kommunikationskanal <br>
	 * DFÜ-Adresse / -Kennung / Telefonnummer / E-MailAdresse
	 * 
	 * @return Kommunikationskanal
	 */
	public String getCommuinicationChannel() {
		return commuinicationChannel;
	}
}
