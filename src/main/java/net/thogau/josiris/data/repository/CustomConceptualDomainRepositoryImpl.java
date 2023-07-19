package net.thogau.josiris.data.repository;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import net.thogau.josiris.data.entity.conceptualDomain.AbstractConceptualDomain;

@Repository
public class CustomConceptualDomainRepositoryImpl implements CustomConceptualDomainRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public <CD extends AbstractConceptualDomain> CD getValue(Class<CD> type, String value) {
		var jpql = "from " + type.getSimpleName() + " where valueMeaning = '" + value + "'";
		Query query = entityManager.createQuery(jpql.toString());
		return type.cast(query.getSingleResult());
	}

	@Override
	public <CD extends AbstractConceptualDomain> CD getRandomValue(Class<CD> type) {
		var jpql = "from " + type.getSimpleName();
		Query query = entityManager.createQuery(jpql.toString());
		@SuppressWarnings("unchecked")
		List<CD> all = query.getResultList();
		return type.cast(all.get(new Random().nextInt(all.size())));
	}

}
