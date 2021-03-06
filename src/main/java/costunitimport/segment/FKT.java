package costunitimport.segment;

public class FKT extends Segment {

	private String processingIndicator;

	public FKT(String[] data) {
		super(data);
	}

	@Override
	protected void assignData() {
		int position = 1;
		processingIndicator = getData(position, String.class);
	}

	/**
	 * Verarbeitungskennzeichen<br>
	 * Schlüssel Verarbeitungskennzeichen
	 * 
	 * @return Verarbeitungskennzeichen
	 * 01 - Neuanmeldung
	 * 02 - Änderung
	 * 03 - Stornierung
	 * 04 - Unverändert
	 */
	public String getProcessingIndicator() {
		return processingIndicator;
	}
}
