package mx.cenidet.projects.tdah.entities;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "a_p_p")
public class APP {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Boolean meningitis;
	
	private Boolean asma;
	
	private Boolean poliomielitis;
	
	private Boolean encefalitis;
	
	private Boolean varicela;
	
	private Boolean apnea;
	
	private Boolean cancer;
	
	private Boolean sarampion;
	
	private Boolean epilepsia;
	
	private String otraEnfermedadPresentada;
	
	private String actualmenteEnfermo;
	
	private String tratamientoActualmenteEnfermo;
	
	private Boolean golpesMedulaEspinal;
	
	private Boolean golpesCabeza;
	
	private Boolean tumores;
	
	private Boolean alergias;
	
	private String especificarGolpes;
	
	private String enfermedadesNeurologicas;
	
	private String alteracionPsicologicas;
	
	private String secuelas;
	
	private String secuelasTipo;
	
	private Boolean temperaturaMayor40;
	
	private Boolean convulsiones;
	
	private Boolean perdidaConciencia;
	
	private String espacificar;
	
	private String problemasVision;
	
	private String usaLentes;
	
	private String usaLentesDesde;
	
	private String motricidad;
	
	private String requiereAparatosOrtipedicos;
	
	private String requiereAparatosOrtipedicosDesde;
	
	private String actualmenteTomaMedicamentos;
	
	private String posologia;
	
	private String tomaMedicamentosDesde;
	
	private String tomaMedicamentosPara;
	
	private String reaccionesAdversas;
	
	@OneToOne(mappedBy = "app", cascade = CascadeType.ALL)
	private Paciente paciente;
}
