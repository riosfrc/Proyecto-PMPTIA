package mx.cenidet.projects.covid.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import mx.cenidet.projects.covid.entities.ImagenRepositorio;

public interface ImagenRepositorioRepository extends JpaRepository<ImagenRepositorio, String> {
	
	@Transactional
	@Modifying
	@Query("DELETE FROM ImagenRepositorio i WHERE i.nombre = :name")
	public void deleteByName(@Param("name") String name);
	
	@Query("SELECT i FROM ImagenRepositorio i ORDER BY LENGTH(i.idImagenRepositorio)")
	List<ImagenRepositorio> findAllBySort();
	
	@Query("SELECT i FROM ImagenRepositorio i WHERE i.nombre = :name")
	public ImagenRepositorio findByName(@Param("name") String name);
}
