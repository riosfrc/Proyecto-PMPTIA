package mx.cenidet.projects.tdah.entities;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "padre")

public class Padre {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nombre;
	
	private String ocupacion; 
	
	private String ciudad; 
	
	private String telefonoOfi;
	
	private String edad; 
	
	private String escolaridad; 
	
	private String telefono; 
	
	
	
	
}
