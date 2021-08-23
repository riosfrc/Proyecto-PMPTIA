package mx.cenidet.projects.tdah.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.cenidet.projects.tdah.entities.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Long>{

}
