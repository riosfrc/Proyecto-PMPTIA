package mx.cenidet.security.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mx.cenidet.projects.tdah.responses.ResponseMessage;
import mx.cenidet.security.entities.Rol;
import mx.cenidet.security.entities.Usuario;
import mx.cenidet.security.repositories.RolRepository;
import mx.cenidet.security.repositories.UsuarioRepository;

@RestController
public class SecurityController {
	
	@Autowired
	RolRepository rolRepository;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
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
