package net.thogau.josiris.data.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import net.thogau.josiris.data.entity.conceptualDomain.AbstractConceptualDomain;
import net.thogau.josiris.data.entity.conceptualDomain.AlterationType;
import net.thogau.josiris.data.entity.conceptualDomain.AminoAcidChangeType;
import net.thogau.josiris.data.entity.conceptualDomain.AnalysisType;
import net.thogau.josiris.data.entity.conceptualDomain.Boolean;
import net.thogau.josiris.data.entity.conceptualDomain.CauseOfDeath;
import net.thogau.josiris.data.entity.conceptualDomain.Chromosome;
import net.thogau.josiris.data.entity.conceptualDomain.Drug;
import net.thogau.josiris.data.entity.conceptualDomain.EventType;
import net.thogau.josiris.data.entity.conceptualDomain.FusionPrimeEnd;
import net.thogau.josiris.data.entity.conceptualDomain.Gender;
import net.thogau.josiris.data.entity.conceptualDomain.GenomeEntityDatabase;
import net.thogau.josiris.data.entity.conceptualDomain.GenomeEntityType;
import net.thogau.josiris.data.entity.conceptualDomain.LastNewsStatus;
import net.thogau.josiris.data.entity.conceptualDomain.Laterality;
import net.thogau.josiris.data.entity.conceptualDomain.M;
import net.thogau.josiris.data.entity.conceptualDomain.Morphology;
import net.thogau.josiris.data.entity.conceptualDomain.N;
import net.thogau.josiris.data.entity.conceptualDomain.SampleNature;
import net.thogau.josiris.data.entity.conceptualDomain.SampleOrigin;
import net.thogau.josiris.data.entity.conceptualDomain.StorageTemperature;
import net.thogau.josiris.data.entity.conceptualDomain.SurgeryResectionQuality;
import net.thogau.josiris.data.entity.conceptualDomain.T;
import net.thogau.josiris.data.entity.conceptualDomain.TechnicalProtocol;
import net.thogau.josiris.data.entity.conceptualDomain.Topography;
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

	public CauseOfDeath getCauseOfDeath(String value) {
		return repository.getCauseOfDeath(value);
	}

	public EventType getEventType(String value) {
		return repository.getEventType(value);
	}

	public Gender getGender(String value) {
		return repository.getGender(value);
	}

	public LastNewsStatus getLastNewsStatus(String value) {
		return repository.getLastNewsStatus(value);
	}

	public Laterality getLaterality(String value) {
		return repository.getLaterality(value);
	}

	public Morphology getMorphology(String value) {
		return repository.getMorphology(value);
	}

	public Topography getTopography(String value) {
		return repository.getTopography(value);
	}

	public T getT(String value) {
		return repository.getT(value);
	}

	public M getM(String value) {
		return repository.getM(value);
	}

	public N getN(String value) {
		return repository.getN(value);
	}

	public Boolean getBoolean(String value) {
		return repository.getBoolean(value);
	}

	public SurgeryResectionQuality getSurgeryResectionQuality(String value) {
		return repository.getSurgeryResectionQuality(value);
	}

	public Drug getDrug(String value) {
		return repository.getDrug(value);
	}

	public SampleNature getSampleNature(String value) {
		return repository.getSampleNature(value);
	}

	public SampleOrigin getSampleOrigin(String value) {
		return repository.getSampleOrigin(value);
	}

	public StorageTemperature getStorageTemperature(String value) {
		return repository.getStorageTemperature(value);
	}

	public AnalysisType getAnalysisType(String value) {
		return repository.getAnalysisType(value);
	}

	public TechnicalProtocol getTechnicalProtocol(String value) {
		return repository.getTechnicalProtocol(value);
	}

	public AlterationType getAlterationType(String value) {
		return repository.getAlterationType(value);
	}

	public AminoAcidChangeType getAminoAcidChangeType(String value) {
		return repository.getAminoAcidChangeType(value);
	}

	public Chromosome getChromosome(String value) {
		return repository.getChromosome(value);
	}

	public GenomeEntityDatabase getGenomeEntityDatabase(String value) {
		return repository.getGenomeEntityDatabase(value);
	}

	public GenomeEntityType getGenomeEntityType(String value) {
		return repository.getGenomeEntityType(value);
	}

	public FusionPrimeEnd getFusionPrimeEnd(String value) {
		return repository.getFusionPrimeEnd(value);
	}
}
