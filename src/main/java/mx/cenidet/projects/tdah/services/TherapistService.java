package mx.cenidet.projects.tdah.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.cenidet.projects.tdah.entities.Terapeuta;
import mx.cenidet.projects.tdah.repositories.TerapeutaRepository;

@Service
public class TherapistService {
	
	@Autowired
	TerapeutaRepository terapeutaRepository;
	
	public List<Terapeuta> findAll() {
		return (List<Terapeuta>) terapeutaRepository.findAll();
	}

	public Terapeuta find(Terapeuta terapeuta) {
		return terapeutaRepository.findById(terapeuta.getIdTerapeuta()).orElse(null);
	}

	public void save(Terapeuta terapeuta) {
		terapeutaRepository.save(terapeuta);
	}

	public void delete(Long idTerapeuta) {
		terapeutaRepository.deleteById(idTerapeuta);
	}
	
}
