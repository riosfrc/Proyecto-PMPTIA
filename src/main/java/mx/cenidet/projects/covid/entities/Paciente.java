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
@Table(name = "paciente_repositorio")
public class Paciente {
	
	@Id
    @GeneratedValue(generator = "paciente-generator")
	@GenericGenerator(name = "paciente-generator",
    				  parameters = @Parameter(name = "prefix", value = "paciente"),
    				  strategy = "mx.cenidet.projects.covid.generators.CovidIdGenerator")
	private String idPaciente;
	
	@NotEmpty
	private String edad;
	
	@NotEmpty
	private String peso;
	
	@NotEmpty
	private String sexo;
	
	private String saturacionOxigeno;
	
	@NotEmpty
	private String enfermedad;
	
	@NotEmpty
	private String faseEnfermedad;
	
	@JsonManagedReference
	@ToString.Exclude
	@OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL)
	private List<Imagen> imagenes;
	
	public Paciente() {
		
	}

	public Paciente(String edad, String peso, String sexo, String saturacionOxigeno, String enfermedad, String faseEnfermedad) {
		this.edad = edad;
		this.peso = peso;
		this.sexo = sexo;
		this.saturacionOxigeno = saturacionOxigeno;
		this.enfermedad = enfermedad;
		this.faseEnfermedad = faseEnfermedad;
	}
	
}
