package mx.cenidet.projects.tdah.controllers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import mx.cenidet.projects.tdah.entities.Paciente;
import mx.cenidet.projects.tdah.entities.Sesion;
import mx.cenidet.projects.tdah.responses.ResponseMessage;
import mx.cenidet.projects.tdah.services.PatientService;
import mx.cenidet.projects.tdah.services.SessionService;

@RestController
@RequestMapping("/tdah/session")
public class SessionController {
	
	@Autowired
	SessionService sessionService;
	
	@Autowired
	PatientService patientService;
	
	@PostMapping("/{idPaciente}/upload")
	public ResponseEntity<ResponseMessage> uploadSession(@RequestParam("file") MultipartFile file, @PathVariable Long idPaciente) {
	    String message = "";
	    try {
	    	sessionService.save(file, idPaciente);
	    	message = "Uploaded the file successfully: " + file.getOriginalFilename();
	    	return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
	    } catch (Exception e) {
	    	message = "Could not upload the file: " + file.getOriginalFilename() + "!";
	    	e.printStackTrace();
	    	return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
	    }
	}
	
	@GetMapping("/{idPaciente}/getAll")
	public ResponseEntity<?> getAllSessions(@PathVariable Long idPaciente){
		try {
			Paciente paciente = patientService.find(idPaciente);
			List<Sesion> sesiones = paciente.getSesiones();
			List<JSONObject> jsonResponse = new ArrayList<JSONObject>();
			
			// Create a new jsonitem for each session in the database
			for(Sesion sesion : sesiones) {
				JSONObject jsonItem = new JSONObject();
				jsonItem.put("nombre", sesion.getNombre());
				jsonItem.put("ruta", sesion.getRuta());
				jsonItem.put("fecha", sesion.getFecha());
				jsonItem.put("nombrePaciente", paciente.getNombre());
				jsonItem.put("apellidoPaciente", paciente.getApellido());
				jsonItem.put("edadPaciente", patientService.calculateAge(paciente.getFechaNacimiento()));
				jsonResponse.add(jsonItem);
			}
			
			return ResponseEntity.status(HttpStatus.OK).body(jsonResponse.toString());
		} catch (Exception e) {
			String message = "Could not find the sessions!";
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<ResponseMessage> deleteSession(@RequestParam("name") String name) {
		String message = "";
		try {
			sessionService.delete(name);
			message = "Deleted the session successfully: " + name;
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		} catch (Exception e) {
			message = "Could not delete the session: " + name + "!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}
	}
}
