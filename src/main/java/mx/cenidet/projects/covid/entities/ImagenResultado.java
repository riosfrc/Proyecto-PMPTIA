package mx.cenidet.projects.covid.entities;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Data
@Entity
@Table(name = "imagen_resultado")
public class ImagenResultado {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idImagen;
	
	private String nombre;
	
	private String ruta;
	
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_paciente")
	private PacienteDeteccion pacienteDeteccion;
	
	public ImagenResultado() {
		
	}
	
	public ImagenResultado(PacienteDeteccion pacienteDeteccion) {
		this.pacienteDeteccion = pacienteDeteccion;
	}
}
