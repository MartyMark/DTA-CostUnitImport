package costunitimport.model;

public enum Transmissiondays {
	ALL_DAYS(1, "Übertragung an allen Tagen"),
	MON_SAM(2, "Übertragung nur an Werktagen (Montag bis Samstag außer Feiertag)"),
	MON_FRI(3, "Übertragung nur an Arbeitstagen (Montag bis Freitag außer Feiertag)");
	
	private Integer id;
	private String description;
	
	private Transmissiondays(Integer id, String description) {
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
