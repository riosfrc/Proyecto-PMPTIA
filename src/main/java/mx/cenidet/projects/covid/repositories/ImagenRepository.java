package mx.cenidet.projects.covid.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import mx.cenidet.projects.covid.entities.Imagen;

public interface ImagenRepository extends JpaRepository<Imagen, String> {
	
	@Transactional
	@Modifying
	@Query("DELETE FROM Imagen i WHERE i.nombre = :name")
	public void deleteByName(@Param("name") String name);
	
	@Query("SELECT i from Imagen i WHERE i.nombre = :name")
	public Imagen findByName(@Param("name") String name);
}
