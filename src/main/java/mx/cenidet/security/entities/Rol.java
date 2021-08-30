package mx.cenidet.security.entities;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "rol")
public class Rol {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idRol;
	
	private String nombre;
	
	private String descripcion;
	
	public Rol() {
		
	}
	
	public Rol(String nombre, String descripcion) {
		this.nombre = nombre;
		this.descripcion = descripcion;
	}
}
