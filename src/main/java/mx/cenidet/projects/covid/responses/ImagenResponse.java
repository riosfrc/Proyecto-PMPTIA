package mx.cenidet.projects.covid.responses;

import lombok.Data;

/**
 *  Response entity for covid administrator request
 */
@Data
public class ImagenResponse {
	
	private String edad;
	
	private String enfermedad;
	
	private String faseEnfermedad;
	
	private String saturacionOxigeno;
	
	private String peso;
	
	private String sexo;
	
	private String tipoImagen;
	
	private String nombreImagen;
	
	private String rutaImagen;
	
	public ImagenResponse() {
		
	}

	public ImagenResponse(String edad, String enfermedad, String faseEnfermedad, String saturacionOxigeno,
						  String peso, String sexo, String tipoImagen, String nombreImagen, String rutaImagen) {
		this.edad = edad;
		this.enfermedad = enfermedad;
		this.faseEnfermedad = faseEnfermedad;
		this.saturacionOxigeno = saturacionOxigeno;
		this.peso = peso;
		this.sexo = sexo;
		this.tipoImagen = tipoImagen;
		this.nombreImagen = nombreImagen;
		this.rutaImagen = rutaImagen;
	}
	
}
