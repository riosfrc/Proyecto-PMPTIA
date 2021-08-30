package mx.cenidet.security.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mx.cenidet.security.entities.Rol;

public interface RolRepository extends JpaRepository<Rol, Long> {
	
	@Query("SELECT r FROM Rol r WHERE r.nombre = :name")
	public Rol findByName(@Param("name") String name);
}
