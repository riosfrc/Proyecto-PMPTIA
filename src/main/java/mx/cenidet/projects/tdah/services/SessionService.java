package mx.cenidet.projects.tdah.services;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import mx.cenidet.projects.tdah.entities.Paciente;
import mx.cenidet.projects.tdah.entities.Sesion;
import mx.cenidet.projects.tdah.repositories.SesionRepository;

@Service
public class SessionService {
	
	@Autowired
	SesionRepository sesionRepository;
	
	@Autowired
	PatientService patientService;
	
	private final Path ABSOLUTE_PATH = Paths.get(".").toAbsolutePath();
	private final String RELATIVE_PATH = "/src/main/resources/static/projects/tdah/uploads/";
	private final String SHORT_RELATIVE_PATH = "/projects/tdah/uploads/";
	
	public void save(MultipartFile file) throws Exception {
		// store recording in the specified directory
		FileOutputStream fos = new FileOutputStream(ABSOLUTE_PATH + RELATIVE_PATH + file.getOriginalFilename());
		fos.write(file.getBytes());
		fos.close();
		
		// get upload date
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String fecha = dtf.format(LocalDateTime.now());
		
		// save recording information to the database
		Sesion sesion = new Sesion(file.getOriginalFilename(), SHORT_RELATIVE_PATH, fecha);
		
		Paciente paciente = new Paciente("Fernando", "Rios", "09/06/2001");
		patientService.save(paciente);
		
		sesion.setPaciente(paciente);
		sesionRepository.save(sesion);
	}
	
	public List<Sesion> findAll() throws Exception {
		return sesionRepository.findAll();
	}
	
	public Sesion findByName(String name) throws Exception {
		return sesionRepository.findByName(name);
	}
	
	public void delete(String name) throws Exception {
		// Delete the session information from the database
		Sesion sesion = this.findByName(name);
		sesionRepository.delete(sesion);
		
		// Delete video in the specified directory
		File video = new File(ABSOLUTE_PATH + RELATIVE_PATH + name);
		video.delete();
	}
}
