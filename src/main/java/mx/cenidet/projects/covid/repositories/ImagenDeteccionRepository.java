package mx.cenidet.projects.covid.repositories;

import java.util.List;

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
	
	@Query("SELECT i FROM ImagenDeteccion i ORDER BY LENGTH(i.idImagenDeteccion)")
	List<ImagenDeteccion> findAllBySort();
	
	@Query("SELECT i FROM ImagenDeteccion i WHERE i.nombre = :name")
	public ImagenDeteccion findByName(@Param("name") String name);

}
