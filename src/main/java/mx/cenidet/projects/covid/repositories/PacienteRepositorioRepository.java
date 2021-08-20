package mx.cenidet.projects.covid.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.cenidet.projects.covid.entities.PacienteRepositorio;

public interface PacienteRepositorioRepository extends JpaRepository<PacienteRepositorio, String> {
	
}
