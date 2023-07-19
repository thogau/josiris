package net.thogau.josiris.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import net.thogau.josiris.data.entity.conceptualDomain.AbstractConceptualDomain;

public interface ConceptualDomainRepository extends JpaRepository<AbstractConceptualDomain, Long>,
		JpaSpecificationExecutor<AbstractConceptualDomain>, CustomConceptualDomainRepository {
}
