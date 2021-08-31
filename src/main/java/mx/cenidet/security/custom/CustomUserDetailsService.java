package mx.cenidet.security.custom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import mx.cenidet.security.entities.Usuario;
import mx.cenidet.security.repositories.UsuarioRepository;

public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.getUserByEmail(email);
		
		if(usuario == null) {
			throw new UsernameNotFoundException("Could not find user");
		}
		
		return new CustomUserDetails(usuario);
	}
	
}
