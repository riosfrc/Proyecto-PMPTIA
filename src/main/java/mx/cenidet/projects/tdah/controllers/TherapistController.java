package mx.cenidet.projects.tdah.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import mx.cenidet.projects.tdah.entities.Paciente;
import mx.cenidet.projects.tdah.services.PatientService;

@Controller
public class TherapistController {
	
	@Autowired
	PatientService patientService;
	
	@GetMapping("/tdah/profile")
	public String tablePatients(Model model) {
		List<Paciente> pacientes = patientService.findAll();
		
		for(Paciente paciente : pacientes) {
			String age = patientService.calculateAge(paciente.getFechaNacimiento());
			paciente.setFechaNacimiento(age);
		}
		
		model.addAttribute("pacientes", pacientes);
		return "projects/tdah/perfil";
	}

}
