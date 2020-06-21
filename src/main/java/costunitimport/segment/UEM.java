package costunitimport.segment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import costunitimport.dao.factory.RepositoryFactory;
import costunitimport.model.TransferParameter;
import costunitimport.model.UEM_Transfer;

public class UEM extends Segment {

	private Integer kindOfDataMedium;
	private Integer parameter;
	private String charSet;
	private String typeOfCompression;
	
	private List<DFU> dfuList= new ArrayList<>();
	
	private RepositoryFactory rFactory;

	public UEM(String[] data, RepositoryFactory rFactory) {
		super(data);
		this.rFactory = rFactory;
	}

	@Override
	protected void assignData() {
		int position = 1;
		kindOfDataMedium = getData(position++, Integer.class);
		parameter = getData(position++, Integer.class);
		charSet = getData(position++, String.class);
		typeOfCompression = getData(position, String.class);
	}
	
	public UEM_Transfer buildTransfer() {
		UEM_Transfer transfer = new UEM_Transfer();
		transfer.setKindOfDataMedium(rFactory.getCostUnitTypeMediumRepository().findById(kindOfDataMedium).orElseThrow().getDescription());
		
		Optional<TransferParameter> transferParameter = rFactory.getTransferParameterRepository().findById(parameter);
		if(transferParameter.isPresent()) {
			transfer.setParameter(transferParameter.get().getDescription());
			transfer.setCharSet(rFactory.getCharSetRepoistory().findByCharSetId(charSet).orElseThrow().getDescription());
		}
		dfuList.forEach(x -> transfer.addTransmissionmedien(x.buildTransmissionMedium()));
		return transfer;
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
	
	public List<DFU> getDFUs() {
		return dfuList;
	}
	
	public void setCostUnitFileDFUs(List<DFU> dfuList) {
		this.dfuList = dfuList;
	}

}
