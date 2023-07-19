package net.thogau.josiris.data.repository;

import net.thogau.josiris.data.entity.conceptualDomain.AbstractConceptualDomain;

public interface CustomConceptualDomainRepository {

	public <CD extends AbstractConceptualDomain> CD getValue(Class<CD> type, String value);

	public <CD extends AbstractConceptualDomain> CD getRandomValue(Class<CD> type);
}
