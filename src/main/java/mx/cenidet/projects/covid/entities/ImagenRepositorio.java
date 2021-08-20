package mx.cenidet.projects.covid.entities;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Data
@Entity
@Table(name = "imagen_repositorio")
public class ImagenRepositorio {
	
	@Id
    @GeneratedValue(generator = "imagen-generator")
	@GenericGenerator(name = "imagen-generator",
    				  parameters = @Parameter(name = "prefix", value = "R_IMAGEN"),
    				  strategy = "mx.cenidet.projects.covid.generators.CovidIdGenerator")
	private String idImagenRepositorio;
	
	private String nombre;
	
	private String ruta;
	
	@NotEmpty
	private String tipo;
	
	@NotEmpty
	private String fecha;
	
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "id_paciente_repositorio")
	private PacienteRepositorio paciente;
	
	public ImagenRepositorio() {
		
	}

	public ImagenRepositorio(String nombre, String ruta, String tipo, String fecha, PacienteRepositorio paciente) {
		this.nombre = nombre;
		this.ruta = ruta;
		this.tipo = tipo;
		this.fecha = fecha;
		this.paciente = paciente;
	}
	
}
