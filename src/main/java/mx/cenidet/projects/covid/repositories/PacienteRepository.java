package mx.cenidet.projects.covid.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.cenidet.projects.covid.entities.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, String> {
	
}
