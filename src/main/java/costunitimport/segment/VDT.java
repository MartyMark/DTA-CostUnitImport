package costunitimport.segment;

import java.time.LocalDate;

public class VDT extends Segment{

	private LocalDate validityFrom;
	private LocalDate validityUntil;

	public VDT(String[] data) {
		super(data);
	}

	@Override
	protected void assignData() {
		int position = 1;
		validityFrom = getData(position++, LocalDate.class);
		validityUntil = getData(position, LocalDate.class);
	}

	/**
	 * Gültigkeitsdatum ab
	 * 
	 * @return Gültigkeitsdatum ab
	 */
	public LocalDate getValidityFrom() {
		return validityFrom;
	}

	/**
	 * Gültigkeitsdatum bis
	 * 
	 * @return Gültigkeitsdatum bis
	 */
	public LocalDate getValidityUntil() {
		return validityUntil;
	}

}
