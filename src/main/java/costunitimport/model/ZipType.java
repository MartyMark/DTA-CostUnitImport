package costunitimport.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "STAAT_PLZ_ART")
public class ZipType {
	
	public static final int POSTBOX=1;
	public static final int GROUP_CORPORATE=3;
	public static final int SINGLE_CORPORATE=4;
	public static final int ACTION_ZIPCODE=5;
	public static final int DELIVERY_AND_POSTBOX=6;
	public static final int IMPORT_INTERFACE = 7;
	public static final int IMPORT_INTERFACE_UNCHECKED = 8;
	
	@Id
	private Integer id;
	
	private String description;
	
	public ZipType() {}
	
	public ZipType(Integer id, String description) {
		this.id = id;
		this.description = description;
	}
	
	public Integer getId() {
		return id;
	}

	
	public void setId(Integer id) {
		this.id = id;
	}

	
	public String getDescription() {
		return description;
	}

	
	public void setDescription(String description) {
		this.description = description;
	}
}
