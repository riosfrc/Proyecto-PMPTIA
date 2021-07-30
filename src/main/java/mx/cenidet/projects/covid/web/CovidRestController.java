package mx.cenidet.projects.covid.web;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
				imagenRepository.save(new Imagen(image.getOriginalFilename(), "/projects/covid/uploads/", tipoImagen, paciente));
				count++;
			}
			log.info("Imagenes subidas correctamente: " + count);
			if(count == 1) return "200 OK\nSe ha subido correctamente " + count + " imagen clínica.";
			return "200 OK\nSe han subido correctamente " + count + " imagenes clínicas.";
		} catch(Exception e) {
			return "422 Unprocessable Entity\nRequired String parameter is not present";
		}
	}
	
	@DeleteMapping("/covid/form/deleteImages")
	public String deleteImages() {
		Path currentPath = Paths.get(".");
		Path absolutePath = currentPath.toAbsolutePath();
		File folder = new File(absolutePath + "/src/main/resources/static/projects/covid/uploads/");
		File[] files = folder.listFiles();
		for (File file: files) {
			file.delete();
		}
		imagenRepository.deleteAll();
		pacienteRepository.deleteAll();
		return "Se han borrado todas las imagenes correctamente";
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
	
	
	@GetMapping("/covid/admin/getImages")
	public List<ImagenResponse> getImages() {
		List<Imagen> images = imagenRepository.findAll();
		List<ImagenResponse> imagenResponseList = new ArrayList<>();
		for(Imagen image : images) {
			Paciente paciente = image.getPaciente();
			imagenResponseList.add(new ImagenResponse(paciente.getEdad(), paciente.getEnfermedad(), paciente.getFaseEnfermedad(), paciente.getSaturacionOxigeno(), 
								   paciente.getPeso(), paciente.getSexo(), image.getTipo(), image.getNombre(), image.getRuta()));
		}
		return imagenResponseList;
	}
}
