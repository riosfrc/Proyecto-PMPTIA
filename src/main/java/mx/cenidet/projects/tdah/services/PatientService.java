package mx.cenidet.projects.tdah.services;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.cenidet.projects.tdah.entities.Paciente;
import mx.cenidet.projects.tdah.repositories.PacienteRepository;

@Service
public class PatientService {
	
	@Autowired
	PacienteRepository pacienteRepository;
	
	public List<Paciente> findAll() {
		return (List<Paciente>) pacienteRepository.findAll();
	}

	public Paciente find(Paciente paciente) {
		return pacienteRepository.findById(paciente.getIdPaciente()).orElse(null);
	}

	public void save(Paciente paciente) {
		pacienteRepository.save(paciente);
	}

	public void delete(Long idPaciente) {
		pacienteRepository.deleteById(idPaciente);
	}
	
	public String calculateAge(String fechaNacimiento) {
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate birthDate = LocalDate.parse(fechaNacimiento, fmt);
		LocalDate now = LocalDate.now();
		Period period = Period.between(birthDate, now);
		return String.valueOf(period.getYears());
	}
}
