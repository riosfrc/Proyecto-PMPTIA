package mx.cenidet.security.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import mx.cenidet.security.entities.Rol;
import mx.cenidet.security.entities.Usuario;

public class UserDetailsImpl implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	
	private Usuario usuario;
	
	public UserDetailsImpl(Usuario usuario) {
        this.usuario = usuario;
    }
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<Rol> roles = usuario.getRoles();
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		
		for(Rol rol : roles) {
			 authorities.add(new SimpleGrantedAuthority(rol.getNombre()));
		}
		
		return authorities;
	}

	@Override
	public String getPassword() {
		return usuario.getContraseña();
	}

	@Override
	public String getUsername() {
		// Instead of username we use the email
		return usuario.getCorreo();
	}
	
	public String getEmail() {
		return usuario.getCorreo();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
