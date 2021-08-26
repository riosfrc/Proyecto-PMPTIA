package mx.cenidet.projects.tdah.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mx.cenidet.projects.tdah.entities.Sesion;

public interface SesionRepository extends JpaRepository<Sesion, Long> {
	
	@Query("SELECT s FROM Sesion s WHERE s.nombre = :name")
	public Sesion findByName(@Param("name") String name);
}
