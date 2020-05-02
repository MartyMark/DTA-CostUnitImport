package costunitimport.model;

public enum AddressType {

	STREET(1, "Hausanschrift"),
	MAIL_BOX(2, "Postfachanschrift"),
	MAJOR_CLIENT(3, "Großkundeanschrift");
	
	private Integer id;
	private String description;
	
	private AddressType(Integer id, String description) {
		this.id = id;
		this.description = description;
	}
	
	public Integer getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}
}