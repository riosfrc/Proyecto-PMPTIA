package mx.cenidet.main;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	@Override
	public void addViewControllers(ViewControllerRegistry registro) {
		registro.addViewController("/").setViewName("index");
		registro.addViewController("/login").setViewName("login");
		registro.addViewController("/covid/index").setViewName("projects/covid/index");
		registro.addViewController("/covid/form").setViewName("projects/covid/form");
		registro.addViewController("/covid/diagnostico").setViewName("projects/covid/diagnostico");
		registro.addViewController("/covid/deteccion").setViewName("projects/covid/deteccion");
		registro.addViewController("/covid/admin").setViewName("projects/covid/admin");
		registro.addViewController("/tdah/index").setViewName("projects/tdah/index");
		// registro.addViewController("/tdah/profile").setViewName("projects/tdah/perfil");
		// registro.addViewController("/tdah/form").setViewName("projects/tdah/form");
		// registro.addViewController("/tdah/recorded-sessions").setViewName("projects/tdah/registro-sesiones");
		// registro.addViewController("/tdah/new-session").setViewName("projects/tdah/sesion");
		registro.addViewController("/robos/index").setViewName("projects/robos/index");
		registro.addViewController("/robos/perfil").setViewName("projects/robos/perfil");
		registro.addViewController("/robos/subir-imagenes.html").setViewName("projects/robos/subir-imagenes");
		registro.addViewController("/robos/repositorio.html").setViewName("projects/robos/repositorio");
	}
}
