//PARTE FORMULARIO DE DINAMICA FAMILIAR 
package mx.cenidet.projects.tdah.entities;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "d_f")

public class DF {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String descripcionFamiliar;
	private String tiempoDedicadoMadre;
	private String tiempoDedicadoPadre;
	private String miembroFamiliarMejor;
	// Checkbox de dinamica Familiar
	private Boolean divorcio;
	private Boolean problemasFinancieros;
	private Boolean separaciones;
	private Boolean cambioEscuela;
	private Boolean enfermedades;
	private Boolean perdidaTrabajo;
	private Boolean cambioVivienda;
	private Boolean problemasLegales;
	// TERMINA CHECKBOX
	private String sucesoFamiliar;
	private String cualSuceso;
	private String manera;

}
