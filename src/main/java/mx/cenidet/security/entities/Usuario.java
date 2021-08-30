package mx.cenidet.security.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
@Entity
@Table(name = "usuario")
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idUsuario;
	
	@NotEmpty
	private String correo;
	
	@NotEmpty
	private String contraseña;
	
	@NotEmpty
	private String nombre;
	
	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuario_rol",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_rol")
    )
	private List<Rol> roles = new ArrayList<Rol>();
	
	public Usuario() {
		
	}
	
	public Usuario(String nombre, String correo, String contraseña) {
		this.correo = correo;
		this.contraseña = contraseña;
		this.nombre = nombre;
	}
	
	public void addRole(Rol rol) {
		this.roles.add(rol);
	}
}
