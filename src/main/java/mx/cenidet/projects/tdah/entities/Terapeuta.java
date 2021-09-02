package mx.cenidet.projects.tdah.entities;

import java.util.List;

import javax.persistence.*;

import lombok.Data;
import lombok.ToString;
import mx.cenidet.security.entities.Usuario;

@Data
@Entity
@Table(name = "terapeuta")
public class Terapeuta {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idTerapeuta;
	
	private String cedulaProfesional;
	
	private String especialidad;
	
	@OneToOne
	@JoinColumn(name = "idUsuario")
	private Usuario usuario;
	
	@ToString.Exclude
	@OneToMany(mappedBy = "terapeuta")
	private List<Paciente> pacientes;
}
