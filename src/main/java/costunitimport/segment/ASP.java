package costunitimport.segment;

public class ASP extends Segment {

	private Integer sequentialNumber;
	private String telephone;
	private String fax;
	private String name;
	private String fieldOfActivity;

	public ASP(String[] data)  {
		super(data);
	}

	@Override
	protected void assignData() {
		int position = 1;
		sequentialNumber = getData(position++, Integer.class);
		telephone = getData(position++, String.class);
		fax = getData(position++, String.class);
		name = getData(position++, String.class);
		fieldOfActivity = getData(position, String.class);
	}

	/**
	 * Laufende Nummer 
	 * @return Laufende Nummer 
	 */
	public Integer getSequentialNumber() {
		return sequentialNumber;
	}

	/**
	 * Telefon<br>
	 * Form: Vorwahl/Teilnehmernummer 
	 * @return Telefon 
	 */
	public String getTelephone() {
		return telephone;
	}

	/**
	 * Fax <br>
	 * Form: Vorwahl/Teilnehmernummer
	 * @return Fax 
	 */
	public String getFax() {
		return fax;
	}

	/**
	 * Name 
	 * @return Name 
	 */
	public String getName() {
		return name;
	}

	/**
	 * Arbeitsgebiet des Ansprechpartners<br>
	 * Klartext, z. B. Datenaustausch 
	 * @return Arbeitsgebiet des Ansprechpartners 
	 */
	public String getFieldOfActivity() {
		return fieldOfActivity;
	}

}
