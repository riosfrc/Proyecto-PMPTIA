package mx.cenidet.projects.covid.entities;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Data
@Entity
@Table(name = "imagen_deteccion")
public class ImagenDeteccion {
	
	@Id
	@GeneratedValue(generator = "imagen-deteccion-generator")
	@GenericGenerator(name = "imagen-deteccion-generator",
    				  parameters = @Parameter(name = "prefix", value = "D_IMAGEN"),
    				  strategy = "mx.cenidet.projects.covid.generators.CovidIdGenerator")
	private String idImagenDeteccion;
	
	private String nombre;
	
	private String ruta;
	
	private String tipo;
	
	private String fecha;
	
	@OneToOne(mappedBy = "imagenDeteccion", cascade = CascadeType.ALL)
	private ImagenMapa imagenMapa;
	
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_paciente_deteccion")
	private PacienteDeteccion pacienteDeteccion;
	
	public ImagenDeteccion() {
		
	}

	public ImagenDeteccion(String nombre, String ruta, String tipo, String fecha, PacienteDeteccion pacienteDeteccion) {
		this.nombre = nombre;
		this.ruta = ruta;
		this.tipo = tipo;
		this.fecha = fecha;
		this.pacienteDeteccion = pacienteDeteccion;
	}
}
