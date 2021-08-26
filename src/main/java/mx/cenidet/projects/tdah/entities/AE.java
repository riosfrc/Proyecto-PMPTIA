package mx.cenidet.projects.tdah.entities;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name="a_e")
public class AE {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id; 
	
	private Boolean guarderia;
	
	private String guarderiaPuPri;
	
	private String añosCursadosGuarderia;
	
	private Boolean prescolar;
	
	private String prescolarPuPri;
	
	private String añosCursadosPrescolar;
	
	private Boolean primaria;
	
	private String primariaPuPri;
	
	private String añosCursadosPrimaria;
	
	private Boolean educacionEspecial;
	
	private String educacionEspecialPuPri;
	
	private String añosCursadosEducacionEspecial;
	
	private Boolean estimulacionTemprana;
	
	private String añosCursados;
	
	private Boolean clasesDeRegularizacion;
	
	private Boolean escuelaDeIdiomas;
	
	private Boolean terapiaDeLenguaje;
	
	///////////////////////////////////////////////
	
	private String añosDeEstudio;
	
	private String cursoEscolarActual;
	
	private String perdioAñoEscolar;
	
	private String porquePerdioAñoEscolar;
	
	private String faltaAClases;
	
	private String porqueFaltaAClases;
	
	private String aCambiadoDeEscuela;
	
	private String porqueCambiaEscuela;
	
	private String gustaEscuela;
	
	private String porqueLeGustaEscuela;
	
	private String quejasDeLosMaestros;
	
	private String porqueQuejasDeLosMaestros;
	
	private String opinionMaestrosDeAprendizaje;
	
	private String porqueLosproblemasEnEscuela;
	
	private String aprendeFacil; 

	private String actividadesFaciles; 
	
	private String actividadesDificiles; 

	private String promedioActual; 
	//////////////////////////////////////////////
	private Boolean lenguaje;
	
	private Boolean lectura;
	
	private Boolean escritura;
	
	private Boolean matematicas;
	
	private Boolean motricidad;

	private String otroProblema; 
	////////////////////////////////////////////////
	private String edadParaLeer; 

	private String comoLee; 
	////////////////////////////////////////////////
	
	private Boolean omiteLetra; 
	
	private Boolean titubea;
	
	private Boolean confundeLetras;
	
	private Boolean diceLetraPorOtra;
	
	private Boolean saltaRenglones;
	
	private Boolean equivocaDeLinea;
	
	private Boolean repitePalabras;
	
	private Boolean cambiaPalabras;

	private String otroCuandoLee; 
	//////////////////////////////////////////////
	private String comenzoAEscribir; 
	
	private String trabajoAlEscribir; 

	private String porqueSeLeDificultaEscribir; 
	//////////////////////////////////////////////
	private Boolean confundeLetrasAlEscribir; 
	
	private Boolean deletreaLetras;
	
	private Boolean ecribeConLentitud;
	
	private Boolean tieneMalaOrtografia;
	
	private Boolean esIncomprensible;
	
	private String otroAlEscribir; 
	////////////////////////////////////////////
	
	private String edadQueComenzoAContar; 
	
	private String edadParaOperacionesSencillas; 
	////////////////////////////////////////////
	private Boolean escribeMalLosNumeros;
	
	private Boolean noReconoceNumeros;

	private Boolean confundeSignos;

	private Boolean confundeNumeroEnDictado;
	
	private Boolean acomodaMalLasCifras;
	
	private Boolean trabajoEnOperacionesLineales;
	
	private Boolean trabajoEnOperacionesVerticales;
	
	private Boolean noSabeSumar;
	
	private Boolean noSabeRestar;
	
	private Boolean noSabeMultiplicar;
	
	private Boolean noSabeDividir;

	private String otroProblemaDeOperaciones; 
	/////////////////////////////////////////////
	private Boolean caminar;
	
	private Boolean correr;
	
	private Boolean subirEscaleras;
	
	private Boolean sujetar;
	
	private Boolean lanzarObjetos;
	
	private Boolean jugarConCosasGrandes;
	
	private Boolean abotonarse;

	private Boolean vestirse;
	
	private Boolean amarrarseLasAgujetas;
	
	private Boolean recortar;
	
	private Boolean escribir;
	
	private Boolean iluminar;
	
	private String otroProblemaGeneral; 
	////////////////////////////////////////////////
	private Boolean decirOraciones; 
	
	private String decirOracionesTex;
	
	private Boolean comprenderLoQueDice; 
	
	private String comprenderLoQueDiceText;
	
	private Boolean hablarMenosQueOtros; 
	
	private String hablarMenosQueOtrosText;
	
	private Boolean hablarMasQueOtros;
	
	private String hablarMasQueOtrosText;
	
	private Boolean dificultadParaDecirLetras; 
	
	private String dificultadParaDecirLetrasText;
	
	private String otroProblemaLetras; 
	
}
