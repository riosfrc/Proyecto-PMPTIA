package mx.cenidet.projects.covid.entities;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Data
@Entity
@Table(name = "imagen_deteccion")
public class ImagenDeteccion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idImagen;
	
	private String nombre;
	
	private String ruta;
	
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_paciente")
	private PacienteDeteccion pacienteDeteccion;
	
	public ImagenDeteccion() {
		
	}

	public ImagenDeteccion(String nombre, String ruta, PacienteDeteccion pacienteDeteccion) {
		this.nombre = nombre;
		this.ruta = ruta;
		this.pacienteDeteccion = pacienteDeteccion;
	}
}
