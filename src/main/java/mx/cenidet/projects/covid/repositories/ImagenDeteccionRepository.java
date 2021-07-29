package mx.cenidet.projects.covid.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.cenidet.projects.covid.entities.ImagenDeteccion;

public interface ImagenDeteccionRepository extends JpaRepository<ImagenDeteccion, Long>{

}
