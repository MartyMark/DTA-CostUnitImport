package costunitimport.segment;

import java.util.ArrayList;
import java.util.List;

public class CostUnitFileUEM extends CostUnitFileAbstract {

	private Integer kindOfDataMedium;
	private Integer parameter;
	private String charSet;
	private String typeOfCompression;
	
	private List<CostUnitFileDFU> costUnitFileDFUs= new ArrayList<>();

	public CostUnitFileUEM(String[] data) {
		super(data);
	}

	@Override
	protected void assignData() {
		int position = 1;
		kindOfDataMedium = getData(position++, Integer.class);
		parameter = getData(position++, Integer.class);
		charSet = getData(position++, String.class);
		typeOfCompression = getData(position, String.class);
	}


	/**
	 * Art des Übermittlungsmediums<br>
	 * Schlüssel Übermittlungsmedium
	 * 
	 * @return Art des Übermittlungsmediums
	 */
	public Integer getKindOfDataMedium() {
		return kindOfDataMedium;
	}

	/**
	 * Parameter<br>
	 * Schlüssel ÜbermittlungsmediumParameter
	 * 
	 * @return Parameter
	 */
	public Integer getParameter() {
		return parameter;
	}

	/**
	 * Zeichensatz <br>
	 * Schlüssel Übermittlungszeichensatz
	 * 
	 * @return Zeichensatz
	 */
	public String getCharSet() {
		return charSet;
	}

	/**
	 * Art der Komprimierung <br>
	 * Schlüssel Komprimierungsart
	 * 
	 * @return Art der Komprimierung
	 */
	public String getTypeOfCompression() {
		return typeOfCompression;
	}
	
	public List<CostUnitFileDFU> getCostUnitFileDFUs() {
		return costUnitFileDFUs;
	}
	
	public void setCostUnitFileDFUs(List<CostUnitFileDFU> costUnitFileDFUs) {
		this.costUnitFileDFUs = costUnitFileDFUs;
	}

}
