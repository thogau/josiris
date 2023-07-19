package net.thogau.josiris.data.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import net.thogau.josiris.data.entity.conceptualDomain.Gender;

@DataJpaTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ConceptualDomainRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private ConceptualDomainRepository repository;

	@Test
	public void should_find_existing_AbstractConceptualDomain() {
		Gender g = new Gender();
		g.setConceptualDomain("ConceptualDomain");
		g.setFormatConceptualDomain("FormatConceptualDomain");
		g.setLabelValueMeaning("Male");
		g.setReferentiel("Referentiel");
		g.setTypeConceptualDomain("TypeConceptualDomain");
		g.setUrl("Url");
		g.setValueMeaning("M");

		entityManager.persist(g);

		g = new Gender();
		g.setConceptualDomain("ConceptualDomain");
		g.setFormatConceptualDomain("FormatConceptualDomain");
		g.setLabelValueMeaning("Female");
		g.setReferentiel("Referentiel");
		g.setTypeConceptualDomain("TypeConceptualDomain");
		g.setUrl("Url");
		g.setValueMeaning("F");

		entityManager.persist(g);

		g = repository.getValue(Gender.class, "M");
		assertThat(g).isNotNull();
	}

}
