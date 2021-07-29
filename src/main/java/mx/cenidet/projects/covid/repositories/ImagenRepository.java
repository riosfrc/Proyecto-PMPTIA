package mx.cenidet.projects.covid.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.cenidet.projects.covid.entities.Imagen;

public interface ImagenRepository extends JpaRepository<Imagen, String> {

}
