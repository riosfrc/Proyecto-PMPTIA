package mx.cenidet.projects.covid.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import mx.cenidet.projects.covid.entities.ImagenDeteccion;

public interface ImagenDeteccionRepository extends JpaRepository<ImagenDeteccion, String>{
	
	@Transactional
	@Modifying
	@Query("DELETE FROM ImagenDeteccion i WHERE i.nombre = :name")
	public void deleteByName(@Param("name") String name);
	
	@Query("SELECT i from ImagenDeteccion i WHERE i.nombre = :name")
	public ImagenDeteccion findByName(@Param("name") String name);

}
