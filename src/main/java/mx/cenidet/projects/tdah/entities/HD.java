package mx.cenidet.projects.tdah.entities;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "h_d")
public class HD {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String pesoNacimiento;
	
	private String talla;
	
	private String calificacionApgar;
	
	private String calificacionApgarEspecificaciones;
	
	private String tipoNacimiento;
	
	private String lloroInmediatamente;
	
	private String respiroInmediatamente;
	
	private String estuvoIncubadora;
	
	private String complicacionesNacimiento;
	
	private String edadSostuvoCabeza;
	
	private String edadSubioEscalera;
	
	private String edadSentar;
	
	private String edadDijoPalabras;
	
	private String edadSostuvoPie;
	
	private String edadCaminoSolo;
	
	private String edadIndagarEnuresis;
}
