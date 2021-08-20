package mx.cenidet.projects.covid.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.json.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import mx.cenidet.projects.covid.entities.ImagenRepositorio;
import mx.cenidet.projects.covid.entities.ImagenDeteccion;
import mx.cenidet.projects.covid.entities.ImagenMapa;
import mx.cenidet.projects.covid.entities.PacienteRepositorio;
import mx.cenidet.projects.covid.entities.PacienteDeteccion;
import mx.cenidet.projects.covid.repositories.ImagenDeteccionRepository;
import mx.cenidet.projects.covid.repositories.ImagenRepositorioRepository;
import mx.cenidet.projects.covid.repositories.ImagenMapaRepository;
import mx.cenidet.projects.covid.repositories.PacienteDeteccionRepository;
import mx.cenidet.projects.covid.repositories.PacienteRepositorioRepository;

@Slf4j
@RestController
public class CovidRestController {
	
	@Autowired
	PacienteRepositorioRepository pacienteRepositorioRepository;
	
	@Autowired
	PacienteDeteccionRepository pacienteDeteccionRepository;
	
	@Autowired
	ImagenRepositorioRepository imagenRepositorioRepository;
	
	@Autowired
	ImagenDeteccionRepository imagenDeteccionRepository;
	
	@Autowired
	ImagenMapaRepository imagenMapaRepository;
	
	@PostMapping("/covid/form/save")
	public String save(@RequestParam("file[]") MultipartFile[] images,
					   @Valid @RequestParam("edad") String edad,
					   @Valid @RequestParam("peso") String peso,
					   @Valid @RequestParam("sexo") String sexo,
					   @RequestParam("saturacionOxigeno") String saturacionOxigeno,
					   @Valid @RequestParam("enfermedad") String enfermedad,
					   @Valid @RequestParam("faseEnfermedad") String faseEnfermedad,
					   @Valid @RequestParam("tipoImagen") String tipoImagen) {
		try {
			// save the patient information to the database
			PacienteRepositorio paciente = new PacienteRepositorio(edad, peso, sexo, saturacionOxigeno, enfermedad, faseEnfermedad);
			pacienteRepositorioRepository.save(paciente);
			// get upload date
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			String fecha = dtf.format(LocalDateTime.now());
			
			int count = 0;
			for(MultipartFile image : images) {
				// store the image in the specified directory
				Path currentPath = Paths.get(".");
				Path absolutePath = currentPath.toAbsolutePath();
				String extension = image.getOriginalFilename();
				String fileName = fileNameGenerator("R", tipoImagen, enfermedad, paciente.getIdPacienteRepositorio(), fecha, extension);
				FileOutputStream fos = new FileOutputStream(absolutePath + "/src/main/resources/static/projects/covid/uploads/" + fileName);
				fos.write(image.getBytes());
				fos.close();
				// save the image information to the database
				ImagenRepositorio imagen = new ImagenRepositorio(fileName, "/projects/covid/uploads/", tipoImagen, fecha, paciente);
				imagenRepositorioRepository.save(imagen);
				count++;
			}
			log.info("Imagenes subidas correctamente: " + count);
			if(count == 1) return "200 OK\nSe ha subido correctamente " + count + " imagen clínica.";
			return "200 OK\nSe han subido correctamente " + count + " imagenes clínicas.";
		} catch(Exception e) {
			e.printStackTrace();
			return "422 Unprocessable Entity\nRequired String parameter is not present";
		}
	}
	
	
	@GetMapping("/covid/admin/getImagesRepository")
	public ResponseEntity<?> getImagesRepository() throws JSONException {
		List<ImagenRepositorio> images = imagenRepositorioRepository.findAll();
		List<JSONObject> jsonResponse = new ArrayList<JSONObject>();
		
		for(ImagenRepositorio image : images) {
			// Create a new jsonitem for each image in the database
			JSONObject jsonItem = new JSONObject();
			PacienteRepositorio paciente = image.getPaciente();
			jsonItem.put("edad", paciente.getEdad());
			jsonItem.put("enfermedad", paciente.getEnfermedad());
			jsonItem.put("faseEnfermedad", paciente.getFaseEnfermedad());
			jsonItem.put("saturacionOxigeno", paciente.getSaturacionOxigeno());
			jsonItem.put("peso", paciente.getPeso());
			jsonItem.put("sexo", paciente.getSexo());
			jsonItem.put("tipoImagen", image.getTipo());
			jsonItem.put("nombreImagen", image.getNombre());
			jsonItem.put("rutaImagen", image.getRuta());
			jsonItem.put("fecha", image.getFecha());
			jsonResponse.add(jsonItem);
		}
		
		return new ResponseEntity<>(jsonResponse.toString(), HttpStatus.OK);
	}
	
	
	@DeleteMapping("/covid/admin/deleteImagesRepositorio")
	public ResponseEntity<?> deleteImagesRepositorio(@RequestParam("nameImages") String nameImages) throws JSONException {
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
			imagenRepositorioRepository.deleteByName(name);
			// Delete image in the specified directory
			File image = new File(absolutePath + relativePath + name);
			image.delete();
		}
		
		List<PacienteRepositorio> pacientes = pacienteRepositorioRepository.findAll();
		for(PacienteRepositorio paciente : pacientes) {
			System.out.println(paciente.getImagenes().isEmpty());
			// Delete the patient when doesn't have images
			if(paciente.getImagenes().isEmpty()) {
				pacienteRepositorioRepository.delete(paciente);
			}
		}
		
		return new ResponseEntity<>(jsonResponse.toString(), HttpStatus.OK);
	}
	
	
	@PostMapping("/covid/diagnostico/save")
	public String Save(@RequestParam("file[]") MultipartFile[] images,
			   		   @Valid @RequestParam("edad") String edad,
			   		   @Valid @RequestParam("peso") String peso,
			   		   @Valid @RequestParam("sexo") String sexo,
			   		   @Valid @RequestParam("tipoImagen") String tipoImagen) {
		try {
			String diseaseDetected = "COVID-19";
			String reliability = "90";
			// save the patient information to the database
			PacienteDeteccion pacienteDeteccion = new PacienteDeteccion(edad, peso, sexo, diseaseDetected, reliability);
			pacienteDeteccionRepository.save(pacienteDeteccion);
			// get upload date
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			String fecha = dtf.format(LocalDateTime.now());
			
			int count = 0;
			for(MultipartFile image : images) {
				String extension = image.getOriginalFilename();
				String fileName = "";
				Path currentPath = Paths.get(".");
				Path absolutePath = currentPath.toAbsolutePath();
				
				// store uploaded image in the specified directory
				fileName = fileNameGenerator("D", tipoImagen, diseaseDetected, pacienteDeteccion.getIdPacienteDeteccion(), fecha, extension);
				FileOutputStream uploadedImageFos = new FileOutputStream(absolutePath + "/src/main/resources/static/projects/covid/detection/" + fileName);
				uploadedImageFos.write(image.getBytes());
				uploadedImageFos.close();
				// save uploaded image information to the database
				ImagenDeteccion imagenDeteccion = new ImagenDeteccion(fileName, "/projects/covid/detection/", tipoImagen, fecha, pacienteDeteccion);
				imagenDeteccionRepository.save(imagenDeteccion);
				
				// store result image in the specified directory
				fileName = fileNameGenerator("M", tipoImagen, diseaseDetected, pacienteDeteccion.getIdPacienteDeteccion(), fecha, extension);
				FileOutputStream resultImageFos = new FileOutputStream(absolutePath + "/src/main/resources/static/projects/covid/detection/" + fileName);
				resultImageFos.write(image.getBytes());
				resultImageFos.close();
				// save result image information to the database
				ImagenMapa imagenMapa = new ImagenMapa(fileName, "/projects/covid/detection/", imagenDeteccion);
				imagenMapaRepository.save(imagenMapa);
				
				count++;
			}
			log.info("Imagenes subidas correctamente: " + count);
			return "200 OK\nImagenes clínicas subidas correctamente: " + count;
		} catch(Exception e) {
			e.printStackTrace();
			return "422 Unprocessable Entity\nRequired String parameter is not present";
		}
	}
	
	
	@DeleteMapping("/covid/admin/deleteImagesDetecciones")
	public ResponseEntity<?> deleteImagesDetecciones(@RequestParam("nameImages") String nameImages) throws JSONException {
		Path currentPath = Paths.get(".");
		Path absolutePath = currentPath.toAbsolutePath();
		String relativePath = "/src/main/resources/static/projects/covid/detection/";
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
			ImagenDeteccion imagenDeteccion = imagenDeteccionRepository.findByName(name);
			ImagenMapa imagenMapa = imagenDeteccion.getImagenMapa();
			imagenMapaRepository.delete(imagenMapa);
			imagenDeteccionRepository.deleteByName(name);
			// Delete image in the specified directory
			File imageDeteccion = new File(absolutePath + relativePath + name);
			imageDeteccion.delete();
			File imageMapa = new File(absolutePath + relativePath + imagenMapa.getNombre());
			imageMapa.delete();
		}
		
		List<PacienteDeteccion> pacientes = pacienteDeteccionRepository.findAll();
		for(PacienteDeteccion paciente : pacientes) {
			System.out.println(paciente.getImagenDeteccion().isEmpty());
			// Delete the patient when doesn't have images
			if(paciente.getImagenDeteccion().isEmpty()) {
				pacienteDeteccionRepository.delete(paciente);
			}
		}
		
		return new ResponseEntity<>(jsonResponse.toString(), HttpStatus.OK);
	}
	
	
	@GetMapping("/covid/admin/getImagesDetection")
	public ResponseEntity<?> getImagesDetection() throws JSONException {
		List<ImagenDeteccion> images = imagenDeteccionRepository.findAll();
		List<JSONObject> jsonResponse = new ArrayList<JSONObject>();
		
		for(ImagenDeteccion image : images) {
			// Create a new jsonitem for each image in the database
			JSONObject jsonItem = new JSONObject();
			PacienteDeteccion paciente = image.getPacienteDeteccion();
			jsonItem.put("edad", paciente.getEdad());
			jsonItem.put("prediagnostico", paciente.getEnfermedadDetectada());
			jsonItem.put("confiabilidad", paciente.getConfiabilidad());
			jsonItem.put("peso", paciente.getPeso());
			jsonItem.put("sexo", paciente.getSexo());
			jsonItem.put("fecha", image.getFecha());
			jsonItem.put("tipoImagen", image.getTipo());
			jsonItem.put("rutaImagen", image.getRuta());
			jsonItem.put("nombreImagenDeteccion", image.getNombre());
			jsonItem.put("nombreImagenMapa", image.getImagenMapa().getNombre());
			jsonResponse.add(jsonItem);
		}
		
		return new ResponseEntity<>(jsonResponse.toString(), HttpStatus.OK);
	}
	
	public String fileNameGenerator(String clase, String tipo, String enfermedad, String idPaciente, String fecha, String extensión) {
		String acronimoTipo = "";
		String acronimoEnfermedad = "";
		String acronimoFecha = "";
		Long id = 1L;
		
		if(tipo.equals("Radiografía de toráx")) acronimoTipo = "RT";
		if(tipo.equals("Tomografía computarizada")) acronimoTipo = "TC";
		
		if(enfermedad.equals("COVID-19")) acronimoEnfermedad = "COVID";
		if(enfermedad.equals("Neumonía viral")) acronimoEnfermedad = "VIRAL";
		if(enfermedad.equals("Neumonía bacteriana")) acronimoEnfermedad = "BACTERIANA";
		if(enfermedad.equals("Sano")) acronimoEnfermedad = "SANO";
		
		int i = idPaciente.lastIndexOf('_');
		if (i > 0) idPaciente = "P" + idPaciente.substring(i+1);
		
		acronimoFecha = fecha.replace("/", "");
		
		if(clase == "R") {
			List<ImagenRepositorio> imagenes =  imagenRepositorioRepository.findAll();
			if(imagenes.size() != 0) {
				String idImagen = imagenes.get(imagenes.size() - 1).getIdImagenRepositorio();
				id = Long.parseLong(idImagen.replace("R_IMAGEN_", "")) + 1;
			}
		}
		if(clase == "D") {
			List<ImagenDeteccion> imagenes =  imagenDeteccionRepository.findAll();
			if(imagenes.size() != 0) {
				String idImagen = imagenes.get(imagenes.size() - 1).getIdImagenDeteccion();
				id = Long.parseLong(idImagen.replace("D_IMAGEN_", "")) + 1;
			}
		}
		if(clase == "M") {
			List<ImagenMapa> imagenes =  imagenMapaRepository.findAll();
			if(imagenes.size() != 0) {
				String idImagen = imagenes.get(imagenes.size() - 1).getIdImagenMapa();
				id = Long.parseLong(idImagen.replace("M_IMAGEN_", "")) + 1;
			}
		}
		
		return clase + "_" + acronimoTipo + "_" + acronimoEnfermedad + "_" + idPaciente + "_" + acronimoFecha + "_" + id.toString() + "." + extensión;
	}
}
