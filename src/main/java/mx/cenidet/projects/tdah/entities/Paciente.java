package mx.cenidet.projects.tdah.entities;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "paciente")
public class Paciente {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idPaciente;
	
	private String nombre;
	
	private String sexo;
	
	private String fechaResgistro;
	
	private String fechaNacimiento;
	
	private String lugarNacimiento;
	
}
