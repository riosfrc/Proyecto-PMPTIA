package mx.cenidet.projects.tdah.entities;

import javax.persistence.*;

import lombok.Data;

@Data 
@Entity
@Table(name="a_p_y_e")

public class APYE {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; 
	
	private Boolean miedoso;
	private Boolean berrinchudo;
	private Boolean celoso;
	private Boolean rebelde;
	private Boolean autolastima;
	private Boolean agresivo;
	private Boolean aislado;
	private Boolean nervioso;
	private Boolean desapegado;
	private String descripcionPersonalidad;
	
	
}
