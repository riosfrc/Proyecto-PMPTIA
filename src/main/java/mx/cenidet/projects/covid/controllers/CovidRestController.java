package mx.cenidet.projects.covid.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.json.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import mx.cenidet.projects.covid.entities.Imagen;
import mx.cenidet.projects.covid.entities.ImagenDeteccion;
import mx.cenidet.projects.covid.entities.ImagenResultado;
import mx.cenidet.projects.covid.entities.Paciente;
import mx.cenidet.projects.covid.entities.PacienteDeteccion;
import mx.cenidet.projects.covid.repositories.ImagenDeteccionRepository;
import mx.cenidet.projects.covid.repositories.ImagenRepository;
import mx.cenidet.projects.covid.repositories.ImagenResultadoRepository;
import mx.cenidet.projects.covid.repositories.PacienteDeteccionRepository;
import mx.cenidet.projects.covid.repositories.PacienteRepository;
import mx.cenidet.projects.covid.responses.ImagenResponse;

@Slf4j
@RestController
public class CovidRestController {
	
	@Autowired
	PacienteRepository pacienteRepository;
	
	@Autowired
	PacienteDeteccionRepository pacienteDeteccionRepository;
	
	@Autowired
	ImagenRepository imagenRepository;
	
	@Autowired
	ImagenDeteccionRepository imagenDeteccionRepository;
	
	@Autowired
	ImagenResultadoRepository imagenResultadoRepository;
	
	@PostMapping("/covid/form/save")
	public String save(@RequestParam("file[]") MultipartFile[] images,
					   @Valid @RequestParam("edad") String edad,
					   @Valid @RequestParam("peso") String peso,
					   @Valid @RequestParam("sexo") String sexo,
					   @Valid @RequestParam("fecha") String fecha,
					   @RequestParam("saturacionOxigeno") String saturacionOxigeno,
					   @Valid @RequestParam("enfermedad") String enfermedad,
					   @Valid @RequestParam("faseEnfermedad") String faseEnfermedad,
					   @Valid @RequestParam("tipoImagen") String tipoImagen) {
		try {
			// save the patient information to the database
			Paciente paciente = new Paciente(edad, peso, sexo, saturacionOxigeno, enfermedad, faseEnfermedad);
			pacienteRepository.save(paciente);
			
			int count = 0;
			for(MultipartFile image : images) {
				// store the image in the specified directory
				Path currentPath = Paths.get(".");
				Path absolutePath = currentPath.toAbsolutePath();
				FileOutputStream fos = new FileOutputStream(absolutePath + "/src/main/resources/static/projects/covid/uploads/" + image.getOriginalFilename());
				fos.write(image.getBytes());
				fos.close();
				// save the image information to the database
				imagenRepository.save(new Imagen(image.getOriginalFilename(), "/projects/covid/uploads/", tipoImagen, fecha, paciente));
				count++;
			}
			log.info("Imagenes subidas correctamente: " + count);
			if(count == 1) return "200 OK\nSe ha subido correctamente " + count + " imagen clínica.";
			return "200 OK\nSe han subido correctamente " + count + " imagenes clínicas.";
		} catch(Exception e) {
			return "422 Unprocessable Entity\nRequired String parameter is not present";
		}
	}
	
	@GetMapping("/covid/admin/getImages")
	public List<ImagenResponse> getImages() {
		List<Imagen> images = imagenRepository.findAll();
		List<ImagenResponse> imagenResponseList = new ArrayList<>();
		for(Imagen image : images) {
			Paciente paciente = image.getPaciente();
			imagenResponseList.add(new ImagenResponse(paciente.getEdad(), paciente.getEnfermedad(), paciente.getFaseEnfermedad(), paciente.getSaturacionOxigeno(), 
								   paciente.getPeso(), paciente.getSexo(), image.getTipo(), image.getNombre(), image.getRuta(), image.getFecha()));
		}
		return imagenResponseList;
	}
	
	@DeleteMapping("/covid/admin/deleteImages")
	public ResponseEntity<?> deleteImages(@RequestParam("nameImages") String nameImages) throws JSONException {
		Path currentPath = Paths.get(".");
		Path absolutePath = currentPath.toAbsolutePath();
		String relativePath = "/src/main/resources/static/projects/covid/uploads/";
		// Convert the request param to JSONObject
		JSONObject root = new JSONObject(nameImages);
		JSONArray items = root.getJSONArray("item");
		
		List<JSONObject> jsonResponse = new ArrayList<JSONObject>();
		
		int i;
		for(i = 0; i < items.length(); i++) {
			JSONObject jsonItem = items.getJSONObject(i);
			String name = jsonItem.getString("id");
			jsonResponse.add(jsonItem);
			// Delete image information from to the database
			imagenRepository.deleteByName(name);
			// Delete image in the specified directory
			File image = new File(absolutePath + relativePath + name);
			image.delete();
		}
		
		List<Paciente> pacientes = pacienteRepository.findAll();
		for(Paciente paciente : pacientes) {
			System.out.println(paciente.getImagenes().isEmpty());
			// Delete the patient when doesn't have images
			if(paciente.getImagenes().isEmpty()) {
				pacienteRepository.delete(paciente);
			}
		}
		
		return ResponseEntity.ok(jsonResponse.toString());
	}
	
	
	@PostMapping("/covid/diagnostico/save")
	public String Save(@RequestParam("file[]") MultipartFile[] images,
			   		   @Valid @RequestParam("edad") String edad,
			   		   @Valid @RequestParam("peso") String peso,
			   		   @Valid @RequestParam("sexo") String sexo) {
		try {
			// save the patient information to the database
			PacienteDeteccion pacienteDeteccion = new PacienteDeteccion(edad, peso, sexo);
			pacienteDeteccionRepository.save(pacienteDeteccion);
			
			int count = 0;
			for(MultipartFile image : images) {
				// store the image in the specified directory
				Path currentPath = Paths.get(".");
				Path absolutePath = currentPath.toAbsolutePath();
				FileOutputStream fos = new FileOutputStream(absolutePath + "/src/main/resources/static/projects/covid/deteccion/" + image.getOriginalFilename());
				fos.write(image.getBytes());
				fos.close();
				// save the image information to the database
				imagenDeteccionRepository.save(new ImagenDeteccion(image.getOriginalFilename(), "/projects/covid/deteccion/", pacienteDeteccion));
				imagenResultadoRepository.save(new ImagenResultado(pacienteDeteccion));
				count++;
			}
			log.info("Imagenes subidas correctamente: " + count);
			return "200 OK\nImagenes clínicas subidas correctamente: " + count;
		} catch(Exception e) {
			return "422 Unprocessable Entity\nRequired String parameter is not present";
		}
	}
}
