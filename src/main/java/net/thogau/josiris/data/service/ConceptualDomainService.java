package net.thogau.josiris.data.service;

import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import net.thogau.josiris.data.entity.conceptualDomain.AbstractConceptualDomain;
import net.thogau.josiris.data.repository.ConceptualDomainRepository;

@Service
public class ConceptualDomainService {

	private final ConceptualDomainRepository repository;

	public ConceptualDomainService(ConceptualDomainRepository repository) {
		this.repository = repository;
	}

	public AbstractConceptualDomain get(Long id) {
		Optional<AbstractConceptualDomain> acd = repository.findById(id);
		if (acd.isPresent()) {
			return acd.get();
		}
		return null;
	}

	public AbstractConceptualDomain save(AbstractConceptualDomain acd) {
		return repository.save(acd);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public <CD extends AbstractConceptualDomain> CD getValue(Class<CD> type, String value) {
		try {
			return repository.getValue(type, value);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public <CD extends AbstractConceptualDomain> CD getRandomValue(Class<CD> type) {
		return repository.getRandomValue(type);
	}
}
