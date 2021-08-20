package mx.cenidet.projects.covid.entities;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "usuario")
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idUsuario;
	
	private String correo;
	
	private String contraseña;
	
	private String rol;
	
}
