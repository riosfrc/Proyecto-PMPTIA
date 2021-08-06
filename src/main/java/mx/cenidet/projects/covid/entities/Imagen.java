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
public class Imagen {
	
	@Id
    @GeneratedValue(generator = "paciente-generator")
	@GenericGenerator(name = "paciente-generator",
    				  parameters = @Parameter(name = "prefix", value = "imagen"),
    				  strategy = "mx.cenidet.projects.covid.generators.CovidIdGenerator")
	private String idImagen;
	
	private String nombre;
	
	private String ruta;
	
	@NotEmpty
	private String tipo;
	
	@NotEmpty
	private String fecha;
	
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "id_paciente")
	private Paciente paciente;
	
	public Imagen() {
		
	}

	public Imagen(String nombre, String ruta, String tipo, String fecha, Paciente paciente) {
		this.nombre = nombre;
		this.ruta = ruta;
		this.tipo = tipo;
		this.fecha = fecha;
		this.paciente = paciente;
	}
	
}
