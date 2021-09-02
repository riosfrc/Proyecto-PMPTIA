package mx.cenidet.projects.tdah.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import mx.cenidet.projects.tdah.entities.Paciente;
import mx.cenidet.projects.tdah.entities.Terapeuta;
import mx.cenidet.projects.tdah.services.PatientService;
import mx.cenidet.security.custom.CustomUserDetails;
import mx.cenidet.security.entities.Usuario;
import mx.cenidet.security.repositories.UsuarioRepository;

@Controller
public class TherapistController {
	
	@Autowired
	PatientService patientService;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@GetMapping("/tdah/profile")
	public String tablePatients(Model model, @AuthenticationPrincipal CustomUserDetails userDetails, @ModelAttribute("success") String success) {		
		String email = userDetails.getUsername();
		Usuario usuario = usuarioRepository.getUserByEmail(email);
		usuario.setContraseña(null);
		System.out.println("Usuario que hizo login: " + usuario);
		Terapeuta terapeuta = usuario.getTerapeuta();
		
		List<Paciente> pacientes = terapeuta.getPacientes();
				
		for(Paciente paciente : pacientes) {
			String age = patientService.calculateAge(paciente.getFechaNacimiento());
			paciente.setFechaNacimiento(age);
		}
		
		model.addAttribute("usuario", usuario);
		model.addAttribute("pacientes", pacientes);
		// Attribute success passed by PostMapping("/save") in the PatientController
		model.addAttribute("success", success);
		return "projects/tdah/perfil";
	}

}
