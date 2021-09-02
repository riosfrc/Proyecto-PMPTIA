package mx.cenidet.security.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import mx.cenidet.projects.tdah.entities.Terapeuta;
import mx.cenidet.projects.tdah.responses.ResponseMessage;
import mx.cenidet.projects.tdah.services.TherapistService;
import mx.cenidet.security.entities.Rol;
import mx.cenidet.security.entities.Usuario;
import mx.cenidet.security.repositories.RolRepository;
import mx.cenidet.security.repositories.UsuarioRepository;

@Controller
public class LoginController {
	
	@Autowired
	RolRepository rolRepository;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	TherapistService therapistService;
	
	@GetMapping("/login")
	public String showLoginPage() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			return "login";
        }
		
		return "redirect:/";
	}
	
	@ResponseBody
	@PostMapping("/process_register")
	public ResponseEntity<ResponseMessage> processRegister(@Valid @RequestParam("nombre") String nombre,
														   @Valid @RequestParam("correo") String correo,
			   											   @Valid @RequestParam("contraseña") String contraseña,
			   											   @Valid @RequestParam("rol") String rol) {
		String message = "";
		try {
			if(rol.equals("Terapeuta") || rol.equals("Robos")) {
				Rol role = rolRepository.findByName(rol);
				
				Usuario user = new Usuario(nombre, correo, contraseña);
				
				BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
				String encodedPassword = passwordEncoder.encode(user.getContraseña());
				user.setContraseña(encodedPassword);
				
				user.addRole(role);
				
				usuarioRepository.save(user);
				
				if(rol.equals("Terapeuta")) {
					Terapeuta therapist = new Terapeuta();
					therapist.setUsuario(user);
					therapistService.save(therapist);
				}
				
				message = "User registered successfully: " + correo;
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
			} else {
				throw new Exception("Invalid role");
			}
		} catch(Exception e) {
			message = "Could not register the user!";
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}
	}
}
