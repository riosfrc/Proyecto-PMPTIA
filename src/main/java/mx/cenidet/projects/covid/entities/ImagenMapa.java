package mx.cenidet.projects.covid.entities;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Data
@Entity
@Table(name = "imagen_mapa")
public class ImagenMapa {
	
	@Id
	@GeneratedValue(generator = "imagen-resultado-generator")
	@GenericGenerator(name = "imagen-resultado-generator",
    				  parameters = @Parameter(name = "prefix", value = "M_IMAGEN"),
    				  strategy = "mx.cenidet.projects.covid.generators.CovidIdGenerator")
	private String idImagenMapa;
	
	private String nombre;
	
	private String ruta;
	
	@JsonBackReference
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_imagen_deteccion")
	private ImagenDeteccion imagenDeteccion;
	
	public ImagenMapa() {
		
	}
	
	public ImagenMapa(String nombre, String ruta, ImagenDeteccion imagenDeteccion) {
		this.nombre = nombre;
		this.ruta = ruta;
		this.imagenDeteccion = imagenDeteccion; 
	}
}
