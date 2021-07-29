package mx.cenidet.projects.covid.entities;

import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name="paciente_deteccion")
public class PacienteDeteccion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idPaciente;
	
	@NotEmpty
	private String edad;
	
	@NotEmpty
	private String peso;
	
	@NotEmpty
	private String sexo;
	
	private String enfermedadDetectada;
	
	@JsonManagedReference
	@ToString.Exclude
	@OneToMany(mappedBy = "pacienteDeteccion", cascade = CascadeType.ALL)
	private List<ImagenDeteccion> ImagenDeteccion;
	
	public PacienteDeteccion() {
		
	}
	
	public PacienteDeteccion(String edad, String peso, String sexo) {
		this.edad = edad;
		this.peso = peso;
		this.sexo = sexo;
	}
}
