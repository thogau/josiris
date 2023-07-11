package net.thogau.josiris.data.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import net.thogau.josiris.data.entity.Patient;
import net.thogau.josiris.data.repository.PatientRepository;

@Service
public class PatientService {

	private final PatientRepository repository;

	public PatientService(PatientRepository repository) {
		this.repository = repository;
	}

	public Patient get(Long id) {
		Optional<Patient> p = repository.findById(id);
		if (p.isPresent()) {
			return p.get();
		}
		return null;
	}

	public Patient save(Patient p) {
		return repository.save(p);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public List<Patient> getAll() {
		return repository.findAll();
	}

	public int count() {
		return (int) repository.count();
	}

	public Stream<Patient> paginate(PageRequest pr) {
		return repository.findAll(pr).stream();
	}
}
