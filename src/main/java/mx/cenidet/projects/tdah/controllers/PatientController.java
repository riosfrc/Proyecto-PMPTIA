package mx.cenidet.projects.tdah.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.extern.slf4j.Slf4j;
import mx.cenidet.projects.tdah.entities.Paciente;
import mx.cenidet.projects.tdah.services.PatientService;

@Controller
@Slf4j
public class PatientController {
	
	@Autowired
	PatientService patientService;
	
	@GetMapping("/tdah/patient/new")
	public String registerPatient(Paciente paciente, Model model) {
		model.addAttribute("paciente", paciente);
		log.info("The form was entered");
		return "projects/tdah/form";
	}
	
	@GetMapping("/tdah/patient/{idPaciente}")
	public String editPatient(Paciente paciente, Model model) {
		paciente = patientService.find(paciente);
		model.addAttribute("paciente", paciente);
		return "projects/tdah/form";
	}
	
	@PostMapping("/tdah/form/save")
	public String savePatient(Paciente paciente) {
		patientService.save(paciente);
		log.info("Saved patient: " + paciente.toString());
		return "redirect:/tdah/patient/" + paciente.getIdPaciente();
	}
}
