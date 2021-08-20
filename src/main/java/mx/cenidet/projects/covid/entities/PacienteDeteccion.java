package mx.cenidet.projects.covid.entities;

import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name="paciente_deteccion")
public class PacienteDeteccion {
	
	@Id
	@GeneratedValue(generator = "paciente-deteccion-generator")
	@GenericGenerator(name = "paciente-deteccion-generator",
    				  parameters = @Parameter(name = "prefix", value = "D_PACIENTE"),
    				  strategy = "mx.cenidet.projects.covid.generators.CovidIdGenerator")
	private String idPacienteDeteccion;
	
	@NotEmpty
	private String edad;
	
	@NotEmpty
	private String peso;
	
	@NotEmpty
	private String sexo;
	
	private String enfermedadDetectada;
	
	private String confiabilidad;
	
	@JsonManagedReference
	@ToString.Exclude
	@OneToMany(mappedBy = "pacienteDeteccion", cascade = CascadeType.ALL)
	private List<ImagenDeteccion> ImagenDeteccion;
	
	public PacienteDeteccion() {
		
	}
	
	public PacienteDeteccion(String edad, String peso, String sexo, String enfermedadDetectada, String confiabilidad) {
		this.edad = edad;
		this.peso = peso;
		this.sexo = sexo;
		this.enfermedadDetectada = enfermedadDetectada;
		this.confiabilidad = confiabilidad;
	}
}
