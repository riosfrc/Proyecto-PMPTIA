package mx.cenidet.projects.tdah.entities;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name="a_p_n_p")
public class APNP {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String viveEn;
	private String lugarEs;
	private String serviciosCuenta;
	private String ocupaNiñoFamilia;
	private String tieneHabitacion;
	private String originariaFamilia;
	private String ingresoFamiliar;
	private String quienesContribuyen;
	private String niñoDependeEconomicamente;
	private String personasVivenCasa;
	//FAMILAIARES VIVEN EN CASA//	
	private String parentesco1;
	private String nombre1;
	private String edad1;
	private String escolaridad1;
	private String ocupacion1;
	
	private String parentesco2;
	private String nombre2;
	private String edad2;
	private String escolaridad2;
	private String ocupacion2;
	
	private String parentesco3;
	private String nombre3;
	private String edad3;
	private String escolaridad3;
	private String ocupacion3;
	
	private String parentesco4;
	private String nombre4;
	private String edad4;
	private String escolaridad4;
	private String ocupacion4;

	private String parentesco5;
	private String nombre5;
	private String edad5;
	private String escolaridad5;
	private String ocupacion5;
	
	private String actividadesPreferidas;
	private String juguetePreferido;
	private String descripcionTiempoLibre;
	private String actividadesExtraescolares;
	private String tieneAmigos;
	private String cuantosAmigos;
	private String dondeConoce;
	private String saleConEllos;
	private String frecuenciaSale;
	
}
