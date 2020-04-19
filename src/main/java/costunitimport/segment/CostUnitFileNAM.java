package costunitimport.segment;

import java.util.ArrayList;
import java.util.List;

public class CostUnitFileNAM extends CostUnitFileAbstract {

	private Integer sequentialNumber;
	private String name1;
	private String name2;
	private String name3;
	private String name4;
	
	private List<CostUnitFileANS> costUnitFileANSs = new ArrayList<>();

	public CostUnitFileNAM(String[] data) {
		super(data);
	}

	@Override
	protected void assignData() {
		int position = 1;
		sequentialNumber = getData(position++, Integer.class);
		name1 = getData(position++, String.class);
		name2 = getData(position++, String.class);
		name3 = getData(position++, String.class);
		name4 = getData(position, String.class);
	}

	/**
	 * Laufende Nr.<br>
	 * 01 bis 03
	 * @return Laufende Nr. 
	 */
	public Integer getSequentialNumber() {
		return sequentialNumber;
	}

	/**
	 * Name-1 
	 * @return Name-1 
	 */
	public String getName1() {
		return name1;
	}

	/**
	 * Name-2 
	 * @return Name-2 
	 */
	public String getName2() {
		return name2;
	}

	/**
	 * Name-3 
	 * @return Name-3 
	 */
	public String getName3() {
		return name3;
	}

	/**
	 * Name-4 
	 * @return Name-4 
	 */
	public String getName4() {
		return name4;
	}

	
	public List<CostUnitFileANS> getCostUnitFileANSs() {
		return costUnitFileANSs;
	}

}
