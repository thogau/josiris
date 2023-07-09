package net.thogau.josiris.data.entity.conceptualDomain;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thogau.josiris.data.entity.AbstractEntity;

@Entity
@Table(name = "conceptual_domain", uniqueConstraints = {
		@UniqueConstraint(name = "unique_type_and_value", columnNames = { "type", "valueMeaning" }) })
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING, length = 128)
@Getter
@Setter
@NoArgsConstructor
public abstract class AbstractConceptualDomain extends AbstractEntity {

	@NotEmpty
	private String valueMeaning;

	@NotEmpty
	private String labelValueMeaning;

	@NotEmpty
	private String referentiel;

	@NotEmpty
	private String url;

	@NotEmpty
	private String typeConceptualDomain;

	@NotEmpty
	private String formatConceptualDomain;

	@NotEmpty
	private String conceptualDomain;

	@Override
	public String toString() {
		return "AbstractConceptualDomain [getId()=" + getId() + ", valueMeaning=" + valueMeaning + "]";
	}

}
