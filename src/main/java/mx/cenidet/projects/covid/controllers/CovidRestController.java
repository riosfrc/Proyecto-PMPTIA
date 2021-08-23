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
import org.springframework.web.bind.annotation.PutMapping;
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
	public String saveImagesRepository(@RequestParam("file[]") MultipartFile[] images,
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
				String filename = filenameGenerator("R", tipoImagen, enfermedad, paciente.getIdPacienteRepositorio(), fecha, extension);
				FileOutputStream fos = new FileOutputStream(absolutePath + "/src/main/resources/static/projects/covid/uploads/" + filename);
				fos.write(image.getBytes());
				fos.close();
				// save the image information to the database
				ImagenRepositorio imagen = new ImagenRepositorio(filename, "/projects/covid/uploads/", tipoImagen, fecha, paciente);
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
		List<ImagenRepositorio> images = imagenRepositorioRepository.findAllBySort();
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
	public ResponseEntity<?> deleteImagesRepository(@RequestParam("nameImages") String nameImages) throws JSONException {
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
	
	
	@PutMapping("/covid/admin/updateImageRepository")
	public ResponseEntity<?> updateImageRepository(@RequestParam("nombreImagen") String nombreImagen,
												   @RequestParam("edad") String edad,
			   									   @RequestParam("peso") String peso,
			   									   @RequestParam("sexo") String sexo,
			   									   @RequestParam("saturacionOxigeno") String saturacionOxigeno,
			   									   @RequestParam("enfermedad") String enfermedad,
			   									   @RequestParam("faseEnfermedad") String faseEnfermedad,
			   									   @RequestParam("tipoImagen") String tipoImagen) {
		ImagenRepositorio imagenRepositorio = imagenRepositorioRepository.findByName(nombreImagen);
		PacienteRepositorio pacienteRepositorio = imagenRepositorio.getPaciente();
		
		// Update the patient "PacienteRepositorio" in the database with the new data
		pacienteRepositorio.setEdad(edad);
		pacienteRepositorio.setPeso(peso);
		pacienteRepositorio.setSexo(sexo);
		pacienteRepositorio.setSaturacionOxigeno(saturacionOxigeno);
		pacienteRepositorio.setEnfermedad(enfermedad);
		pacienteRepositorio.setFaseEnfermedad(faseEnfermedad);
		pacienteRepositorioRepository.save(pacienteRepositorio);
		
		Path currentPath = Paths.get(".");
		Path absolutePath = currentPath.toAbsolutePath();
		String relativePath = "/src/main/resources/static/projects/covid/uploads/";
		
		List<ImagenRepositorio> imagenes = pacienteRepositorio.getImagenes();
		
		for(ImagenRepositorio imagen : imagenes) {
			if(!imagen.getNombre().equals(nombreImagen)) {
				String extension = imagen.getNombre().substring(imagen.getNombre().lastIndexOf(".") + 1);
				// Generate a new image name with updated data for each patient "PacienteRepositorio" image "ImagenRepositorio"
				String newFilename = filenameGenerator("R", imagen.getTipo(), enfermedad, pacienteRepositorio.getIdPacienteRepositorio(), imagen.getFecha(), extension, imagen.getIdImagenRepositorio());
				
				// Rename the image "ImagenRepositorio" stored in the uploads folder
				File oldImageRepositorio = new File(absolutePath + relativePath + imagen.getNombre());
				File newImageRepositorio = new File(absolutePath + relativePath + newFilename);
				oldImageRepositorio.renameTo(newImageRepositorio);
				
				// Update the image "ImagenRepositorio" in the database with the new name
				ImagenRepositorio imagenRepo = imagenRepositorioRepository.findByName(imagen.getNombre());
				imagenRepo.setNombre(newFilename);
				imagenRepositorioRepository.save(imagenRepo);
			}
		}
		
		String extension = nombreImagen.substring(nombreImagen.lastIndexOf(".") + 1);
		// Generate a new image name with updated data for the image "ImagenRepositorio"
		String newFilename = filenameGenerator("R", tipoImagen, enfermedad, pacienteRepositorio.getIdPacienteRepositorio(), imagenRepositorio.getFecha(), extension, imagenRepositorio.getIdImagenRepositorio());
		
		// Rename the image "ImagenRepositorio" stored in the uploads folder
		File oldImageRepositorio = new File(absolutePath + relativePath + nombreImagen);
		File newImageRepositorio = new File(absolutePath + relativePath + newFilename);
		oldImageRepositorio.renameTo(newImageRepositorio);
		
		// Update the image "ImagenRepositorio" in the database with the new data
		imagenRepositorio.setTipo(tipoImagen);
		imagenRepositorio.setNombre(newFilename);
		imagenRepositorioRepository.save(imagenRepositorio);
		
		return new ResponseEntity<>("Se actualizó la imagen correctamente", HttpStatus.OK);
	}
	
	
	@PostMapping("/covid/diagnostico/save")
	public String saveImagesDetection(@RequestParam("file[]") MultipartFile[] images,
			   		   @Valid @RequestParam("edad") String edad,
			   		   @Valid @RequestParam("peso") String peso,
			   		   @Valid @RequestParam("sexo") String sexo,
			   		   @Valid @RequestParam("tipoImagen") String tipoImagen) {
		try {
			String diseaseDetected = "COVID-19";
			String reliability = "90.00";
			// save the patient information to the database
			PacienteDeteccion pacienteDeteccion = new PacienteDeteccion(edad, peso, sexo, diseaseDetected, reliability);
			pacienteDeteccionRepository.save(pacienteDeteccion);
			// get upload date
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			String fecha = dtf.format(LocalDateTime.now());
			
			int count = 0;
			for(MultipartFile image : images) {
				String extension = image.getOriginalFilename();
				String filename = "";
				Path currentPath = Paths.get(".");
				Path absolutePath = currentPath.toAbsolutePath();
				
				// store uploaded image in the specified directory
				filename = filenameGenerator("D", tipoImagen, diseaseDetected, pacienteDeteccion.getIdPacienteDeteccion(), fecha, extension);
				FileOutputStream uploadedImageFos = new FileOutputStream(absolutePath + "/src/main/resources/static/projects/covid/detection/" + filename);
				uploadedImageFos.write(image.getBytes());
				uploadedImageFos.close();
				// save uploaded image information to the database
				ImagenDeteccion imagenDeteccion = new ImagenDeteccion(filename, "/projects/covid/detection/", tipoImagen, fecha, pacienteDeteccion);
				imagenDeteccionRepository.save(imagenDeteccion);
				
				// store result image in the specified directory
				filename = filenameGenerator("M", tipoImagen, diseaseDetected, pacienteDeteccion.getIdPacienteDeteccion(), fecha, extension);
				FileOutputStream resultImageFos = new FileOutputStream(absolutePath + "/src/main/resources/static/projects/covid/detection/" + filename);
				resultImageFos.write(image.getBytes());
				resultImageFos.close();
				// save result image information to the database
				ImagenMapa imagenMapa = new ImagenMapa(filename, "/projects/covid/detection/", imagenDeteccion);
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
	
	
	@GetMapping("/covid/admin/getImagesDetection")
	public ResponseEntity<?> getImagesDetection() throws JSONException {
		List<ImagenDeteccion> images = imagenDeteccionRepository.findAllBySort();
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
	
	
	@DeleteMapping("/covid/admin/deleteImagesDetecciones")
	public ResponseEntity<?> deleteImagesDetection(@RequestParam("nameImages") String nameImages) throws JSONException {
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
	
	
	@PutMapping("/covid/admin/updateImageDetection")
	public ResponseEntity<?> updateImageDetection(@RequestParam("nombreImagen") String nombreImagen,
												   @RequestParam("edad") String edad,
			   									   @RequestParam("peso") String peso,
			   									   @RequestParam("sexo") String sexo,
			   									   @RequestParam("confiabilidad") String confiabilidad,
			   									   @RequestParam("enfermedad") String enfermedad,
			   									   @RequestParam("tipoImagen") String tipoImagen) {
		ImagenDeteccion imagenDeteccion = imagenDeteccionRepository.findByName(nombreImagen);
		ImagenMapa imagenMapa = imagenDeteccion.getImagenMapa();
		PacienteDeteccion pacienteDeteccion = imagenDeteccion.getPacienteDeteccion();
		
		// Update the patient "PacienteDeteccion" in the database with the new data
		pacienteDeteccion.setEdad(edad);
		pacienteDeteccion.setPeso(peso);
		pacienteDeteccion.setSexo(sexo);
		pacienteDeteccion.setConfiabilidad(confiabilidad);
		pacienteDeteccion.setEnfermedadDetectada(enfermedad);
		pacienteDeteccionRepository.save(pacienteDeteccion);
		
		Path currentPath = Paths.get(".");
		Path absolutePath = currentPath.toAbsolutePath();
		String relativePath = "/src/main/resources/static/projects/covid/detection/";
		
		List<ImagenDeteccion> imagenes = pacienteDeteccion.getImagenDeteccion();
		
		for(ImagenDeteccion imagen : imagenes) {
			if(!imagen.getNombre().equals(nombreImagen)) {				
				String extension = imagen.getNombre().substring(imagen.getNombre().lastIndexOf(".") + 1);
				// Generate a new image name with updated data for each patient "PacienteDeteccion" image "ImagenDeteccion"
				String newFilename = filenameGenerator("D", imagen.getTipo(), enfermedad, pacienteDeteccion.getIdPacienteDeteccion(), imagen.getFecha(), extension, imagen.getIdImagenDeteccion());
				
				// Rename the image "ImagenDeteccion" stored in the detection folder
				File oldImageDeteccion = new File(absolutePath + relativePath + imagen.getNombre());
				File newImageDeteccion = new File(absolutePath + relativePath + newFilename);
				oldImageDeteccion.renameTo(newImageDeteccion);
				
				// Update the image "ImagenDeteccion" in the database with the new name
				ImagenDeteccion imagenDet = imagenDeteccionRepository.findByName(imagen.getNombre());
				imagenDet.setNombre(newFilename);
				imagenDeteccionRepository.save(imagenDet);
				
				ImagenMapa imagenMap = imagen.getImagenMapa();
				// Generate a new image name with updated data for each patient image "ImagenMapa"
				newFilename = filenameGenerator("M", imagen.getTipo(), enfermedad, pacienteDeteccion.getIdPacienteDeteccion(), imagen.getFecha(), extension, imagenMap.getIdImagenMapa());
				
				// Rename the image "ImagenMapa" stored in the detection folder
				File oldImageMapa = new File(absolutePath + relativePath + imagenMap.getNombre());
				File newImageMapa = new File(absolutePath + relativePath + newFilename);
				oldImageMapa.renameTo(newImageMapa);
				
				// Update the image "ImagenMapa" in the database with the new name
				imagenMap.setNombre(newFilename);
				imagenMapaRepository.save(imagenMap);
			}
		}
		
		String extension = nombreImagen.substring(nombreImagen.lastIndexOf(".") + 1);
		// Generate a new image name with updated data for the image "ImagenDeteccion"
		String newFilename = filenameGenerator("D", tipoImagen, enfermedad, pacienteDeteccion.getIdPacienteDeteccion(), imagenDeteccion.getFecha(), extension, imagenDeteccion.getIdImagenDeteccion());
		
		// Rename the image "ImagenDeteccion" stored in the detection folder
		File oldImageDeteccion = new File(absolutePath + relativePath + nombreImagen);
		File newImageDeteccion = new File(absolutePath + relativePath + newFilename);
		oldImageDeteccion.renameTo(newImageDeteccion);
		
		// Update the image "ImagenDeteccion" in the database with the new data
		imagenDeteccion.setTipo(tipoImagen);
		imagenDeteccion.setNombre(newFilename);
		imagenDeteccionRepository.save(imagenDeteccion);
		
		// Generate a new image name with updated data for the image "ImagenMapa"
		newFilename = filenameGenerator("M", tipoImagen, enfermedad, pacienteDeteccion.getIdPacienteDeteccion(), imagenDeteccion.getFecha(), extension, imagenMapa.getIdImagenMapa());
		
		// Rename the image "ImagenMapa" stored in the detection folder
		File oldImageMapa = new File(absolutePath + relativePath + imagenMapa.getNombre());
		File newImageMapa = new File(absolutePath + relativePath + newFilename);
		oldImageMapa.renameTo(newImageMapa);
		
		// Update the image "ImagenMapa" in the database with the new name
		imagenMapa.setNombre(newFilename);
		imagenMapaRepository.save(imagenMapa);
		
		return new ResponseEntity<>("Se actualizó la imagen correctamente", HttpStatus.OK);
	}
	
	
	
	public String filenameGenerator(String clase, String tipo, String enfermedad, String idPaciente, String fecha, String extension, String...paramId) {
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
		
		if(paramId.length == 0) {
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
		}
		if(paramId.length > 0) {
			if (!(paramId[0] instanceof String)) { 
				throw new IllegalArgumentException("...");
			}
			id = Long.parseLong(paramId[0].substring(paramId[0].lastIndexOf("_") + 1));
		}
		
		return clase + "_" + acronimoTipo + "_" + acronimoEnfermedad + "_" + idPaciente + "_" + acronimoFecha + "_" + id.toString() + "." + extension;
	}
}
