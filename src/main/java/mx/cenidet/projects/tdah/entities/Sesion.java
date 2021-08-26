package mx.cenidet.projects.tdah.entities;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Data
@Entity
@Table(name = "sesion")
public class Sesion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idSesion;
	
	private String nombre;
	
	private String ruta;
	
	private String fecha;
	
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_paciente")
	private Paciente paciente;
	
	public Sesion() {
		
	}
	
	public Sesion(String nombre, String ruta, String fecha) {
		this.nombre = nombre;
		this.ruta = ruta;
		this.fecha = fecha;
	}
	
}
