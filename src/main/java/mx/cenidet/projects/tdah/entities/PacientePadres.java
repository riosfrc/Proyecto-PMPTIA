package mx.cenidet.projects.tdah.entities;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "paciente_padres")

public class PacientePadres {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String estadoCivil;
	private String tiempo; 
	
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_padre")
	private Padre padre;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_madre")
	private Madre madre;
	
}
