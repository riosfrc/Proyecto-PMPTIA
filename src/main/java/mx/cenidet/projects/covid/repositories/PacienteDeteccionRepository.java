package mx.cenidet.projects.covid.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.cenidet.projects.covid.entities.PacienteDeteccion;

public interface PacienteDeteccionRepository extends JpaRepository<PacienteDeteccion, Long> {

}
