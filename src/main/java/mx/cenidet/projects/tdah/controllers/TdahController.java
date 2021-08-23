package mx.cenidet.projects.tdah.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.extern.slf4j.Slf4j;
import mx.cenidet.projects.tdah.entities.Paciente;
import mx.cenidet.projects.tdah.repositories.PacienteRepository;

@Controller
@Slf4j
public class TdahController {
	
	@Autowired
	PacienteRepository pacienteRepository;
	
	
	@GetMapping("/tdah/form") // URL del botón
	public String registerPatient(Paciente paciente, Model model) {
		
		model.addAttribute("paciente", paciente);
		
		log.info("The form was entered");
		return "projects/tdah/form"; // archivo html
	}
	
	
	@PostMapping("/tdah/form/save")
	public String savePatient(Paciente paciente) {
		
		pacienteRepository.save(paciente);
		
		log.info("Saved patient: " + paciente.toString());
		return "projects/tdah/perfil";
	}

}
