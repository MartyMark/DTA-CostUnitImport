package costunitimport.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "DTA_LEISTUNGSVERFAHREN")
public class CareProviderMethod {
	public static final int P_ALL_UNKNOWN = 0;//Unbekannt
    public static final int P_300 = 3;//Apotheken - Abrechnung nach §300
    public static final int P_302 = 5;//Sonstige Leistungserbringer - Abrechnung nach §302
    public static final int P_105 = 6;//Leistungserbringer Pflege - Abrechnung nach §105
    public static final int TRANSPORT_AUSTRIA = 7;//Transporter Österreich - DKT
    public static final int PHARMACY_AUSTRIA = 8;//Apotheken Österreich - DOA
    public static final int P_301 = 9;//Krankenhäuser - Abrechnung nach §301
    public static final int P_295 = 10;//Direktabrechner - Abrechnung nach § 295 1b SGB V
    public static final int AUSTRIA_ADJUVANT = 11;
    
    @Id 
	private Integer id;
    
	private String description;
    private String fullDescription;
    private Boolean accountIndicator;
}
