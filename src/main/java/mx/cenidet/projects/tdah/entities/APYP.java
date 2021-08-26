package mx.cenidet.projects.tdah.entities;

import javax.persistence.*;

import lombok.Data;
@Data
@Entity
@Table(name= "a_p_y_p")
public class APYP {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String edadMadreNacer;
	private String edadPadreNacer;
	private String padres;
	private String duracionEmbarazo;
	private String embarazoDeseado; 
	private String problemasEmocionales;
	private String enfermedadesMedicas;
	private String consumoMedicamentos;
	private String amenazaAborto;
	private String desnutricion;
	private String exposicionRx;
	private String ultrasonido;
	private String consumoDrogas;
	private String otroProblema;
	
	

}
