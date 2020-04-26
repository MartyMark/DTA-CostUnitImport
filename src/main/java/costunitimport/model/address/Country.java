package costunitimport.model.address;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "STAAT")
public class Country {
	
	public static final int UNKNOWN = 0;
	public static final int AUSTRIA = 1;
	public static final int SWITZERLAND = 25;
	public static final int GERMANY = 35;
	public static final int BELGIUM = 10106;
	public static final int CZECH = 34;
	public static final int POLAND = 10111;
	public static final int NETHERLANDS = 104;
	public static final int SPAIN = 10113;
	public static final int FRANCE = 10105;
	public static final int LUXEMBOURG = 10108;
	public static final int DENMARK = 10110;
	public static final int FINNLAND = 10135;
	public static final int CROATIA = 10124;
	
	@Id
	private Integer id;
	
	private String description;
	private String token;
	private String dtaTokenP302;
	private String iSOA2;
	private String iSOA3;
	private Integer iSON3;
	private String dtaTokenP301;
	private Integer maxZipLength;
	private String flag;
	private Integer ibanLength;
	private String countryCode;
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public String getDtaTokenP302() {
		return dtaTokenP302;
	}
	
	public void setDtaTokenP302(String dtaTokenP302) {
		this.dtaTokenP302 = dtaTokenP302;
	}
	
	public String getiSOA2() {
		return iSOA2;
	}
	
	public void setiSOA2(String iSOA2) {
		this.iSOA2 = iSOA2;
	}
	
	public String getiSOA3() {
		return iSOA3;
	}
	
	public void setiSOA3(String iSOA3) {
		this.iSOA3 = iSOA3;
	}
	
	public Integer getiSON3() {
		return iSON3;
	}
	
	public void setiSON3(Integer iSON3) {
		this.iSON3 = iSON3;
	}
	
	public String getDtaTokenP301() {
		return dtaTokenP301;
	}
	
	public void setDtaTokenP301(String dtaTokenP301) {
		this.dtaTokenP301 = dtaTokenP301;
	}
	
	public Integer getMaxZipLength() {
		return maxZipLength;
	}
	
	public void setMaxZipLength(Integer maxZipLength) {
		this.maxZipLength = maxZipLength;
	}
	
	public String getFlag() {
		return flag;
	}
	
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	public Integer getIbanLength() {
		return ibanLength;
	}
	
	public void setIbanLength(Integer ibanLength) {
		this.ibanLength = ibanLength;
	}
	
	public String getCountryCode() {
		return countryCode;
	}
	
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
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
