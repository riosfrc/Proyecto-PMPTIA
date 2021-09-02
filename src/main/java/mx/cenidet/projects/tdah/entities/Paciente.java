package mx.cenidet.projects.tdah.entities;

import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "paciente")
public class Paciente {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idPaciente;
	
	private String nombre;
	
	private String apellido;
	
	private String sexo;
	
	private String fechaRegistro;
	
	private String fechaNacimiento;
	
	private String lugarNacimiento;
	
	private String folio; 
	
	private String antecedentesZurderia; 
	
	private String familiarResponsable;
	
	private String lateralidad; 
	
	private String escolaridad;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_paciente_padres")
	private PacientePadres pacientePadres;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_apnp")
	private APNP apnp;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_app")
	private APP app;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_apyp")
	private APYP apyp;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_hd")
	private HD hd;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_ae")
	private AE ae;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_df")
	private DF df;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_apye")
	private APYE apye;
	
	@JsonManagedReference
	@ToString.Exclude
	@OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL)
	private List<Sesion> sesiones;
	
	@ManyToOne
	@JoinColumn(name = "idTerapeuta")
	private Terapeuta terapeuta;
	
	public Paciente() {
		
	}
	
	public Paciente(String nombre, String apellido, String fechaNacimiento) {
		this.nombre = nombre;
		this.apellido = apellido;
		this.fechaNacimiento = fechaNacimiento;
	}
}
