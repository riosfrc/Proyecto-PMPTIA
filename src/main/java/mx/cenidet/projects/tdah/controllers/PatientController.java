package mx.cenidet.projects.tdah.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;
import mx.cenidet.projects.tdah.entities.Paciente;
import mx.cenidet.projects.tdah.entities.Terapeuta;
import mx.cenidet.projects.tdah.services.PatientService;
import mx.cenidet.security.custom.CustomUserDetails;
import mx.cenidet.security.entities.Usuario;
import mx.cenidet.security.repositories.UsuarioRepository;

@Controller
@Slf4j
@RequestMapping("/tdah/patient")
public class PatientController {
	
	@Autowired
	PatientService patientService;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@GetMapping("/new")
	public String registerPatient(Paciente paciente, Model model) {
		model.addAttribute("paciente", paciente);
		return "projects/tdah/form";
	}
	
	@PostMapping("/save")
	public String savePatient(Paciente paciente, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirAttrs) {
		String email = userDetails.getUsername();
		Usuario usuario = usuarioRepository.getUserByEmail(email);
		Terapeuta terapeuta = usuario.getTerapeuta();
		paciente.setTerapeuta(terapeuta);
		
		patientService.save(paciente);
		log.info("Saved patient: " + paciente.toString());
		// Attribute that indicates to the GetMapping("/tdah/profile") if the form was submitted successfully
		redirAttrs.addFlashAttribute("success", "Saved the patient successfully");
		return "redirect:/tdah/profile";
	}
	
	@GetMapping("/edit/{idPaciente}")
	public String editPatient(Paciente paciente, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
		paciente = patientService.find(paciente);
		
		// Check if the patient belongs to the therapist
		Terapeuta patientTherapist = paciente.getTerapeuta();	
		Usuario loggedInUser = usuarioRepository.getUserByEmail(userDetails.getEmail());
		Terapeuta loggedInTherapist = loggedInUser.getTerapeuta();		
		if (patientTherapist.getIdTerapeuta() != loggedInTherapist.getIdTerapeuta()) {
			return "redirect:/403";
		}
		
		model.addAttribute("paciente", paciente);
		return "projects/tdah/form";
	}
	
	@GetMapping("/delete/{idPaciente}")
	public String deletePatient(Paciente paciente, @AuthenticationPrincipal CustomUserDetails userDetails) {
		paciente = patientService.find(paciente);
		
		// Check if the patient belongs to the therapist
		Terapeuta patientTherapist = paciente.getTerapeuta();	
		Usuario loggedInUser = usuarioRepository.getUserByEmail(userDetails.getEmail());
		Terapeuta loggedInTherapist = loggedInUser.getTerapeuta();		
		if (patientTherapist.getIdTerapeuta() != loggedInTherapist.getIdTerapeuta()) {
			return "redirect:/403";
		}
		
		patientService.delete(paciente.getIdPaciente());
		return "redirect:/tdah/profile";
	}
	
	@GetMapping("{idPaciente}/recorded-sessions")
	public String showRecordedSessions(@PathVariable Long idPaciente, Model model) {
		model.addAttribute("idPaciente", idPaciente);
		return "projects/tdah/registro-sesiones";
	}
	
	@GetMapping("{idPaciente}/new-session")
	public String recordNewSession(@PathVariable Long idPaciente, Model model) {
		model.addAttribute("idPaciente", idPaciente);
		return "projects/tdah/nueva-sesion";
	}
}
