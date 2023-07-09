package net.thogau.josiris.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

public interface ConceptualDomainRepository
		extends JpaRepository<AbstractConceptualDomain, Long>, JpaSpecificationExecutor<AbstractConceptualDomain> {

	@Query("select c from CauseOfDeath c where c.valueMeaning = :value")
	CauseOfDeath getCauseOfDeath(@Param("value") String value);

	@Query("select c from EventType c where c.valueMeaning = :value")
	EventType getEventType(@Param("value") String value);

	@Query("select c from Gender c where c.valueMeaning = :value")
	Gender getGender(@Param("value") String value);

	@Query("select c from LastNewsStatus c where c.valueMeaning = :value")
	LastNewsStatus getLastNewsStatus(@Param("value") String value);

	@Query("select c from Laterality c where c.valueMeaning = :value")
	Laterality getLaterality(@Param("value") String value);

	@Query("select c from Morphology c where c.valueMeaning = :value")
	Morphology getMorphology(@Param("value") String value);

	@Query("select c from Topography c where c.valueMeaning = :value")
	Topography getTopography(@Param("value") String value);

	@Query("select c from T c where c.valueMeaning = :value")
	T getT(@Param("value") String value);

	@Query("select c from N c where c.valueMeaning = :value")
	N getN(@Param("value") String value);

	@Query("select c from M c where c.valueMeaning = :value")
	M getM(@Param("value") String value);

	@Query("select c from Boolean c where c.valueMeaning = :value")
	Boolean getBoolean(@Param("value") String value);

	@Query("select c from SurgeryResectionQuality c where c.valueMeaning = :value")
	SurgeryResectionQuality getSurgeryResectionQuality(@Param("value") String value);

	@Query("select c from Drug c where c.valueMeaning = :value")
	Drug getDrug(@Param("value") String value);

	@Query("select c from SampleNature c where c.valueMeaning = :value")
	SampleNature getSampleNature(@Param("value") String value);

	@Query("select c from SampleOrigin c where c.valueMeaning = :value")
	SampleOrigin getSampleOrigin(@Param("value") String value);

	@Query("select c from StorageTemperature c where c.valueMeaning = :value")
	StorageTemperature getStorageTemperature(@Param("value") String value);

	@Query("select c from AnalysisType c where c.valueMeaning = :value")
	AnalysisType getAnalysisType(@Param("value") String value);

	@Query("select c from TechnicalProtocol c where c.valueMeaning = :value")
	TechnicalProtocol getTechnicalProtocol(@Param("value") String value);

	@Query("select c from AlterationType c where c.valueMeaning = :value")
	AlterationType getAlterationType(@Param("value") String value);

	@Query("select c from AminoAcidChangeType c where c.valueMeaning = :value")
	AminoAcidChangeType getAminoAcidChangeType(@Param("value") String value);

	@Query("select c from Chromosome c where c.valueMeaning = :value")
	Chromosome getChromosome(@Param("value") String value);

	@Query("select c from GenomeEntityDatabase c where c.valueMeaning = :value")
	GenomeEntityDatabase getGenomeEntityDatabase(@Param("value") String value);

	@Query("select c from GenomeEntityType c where c.valueMeaning = :value")
	GenomeEntityType getGenomeEntityType(@Param("value") String value);

	@Query("select c from FusionPrimeEnd c where c.valueMeaning = :value")
	FusionPrimeEnd getFusionPrimeEnd(@Param("value") String value);
}
