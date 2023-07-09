package net.thogau.josiris.data.entity.conceptualDomain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("GenomeEntityDatabase")
@Getter
@Setter
@NoArgsConstructor
public class GenomeEntityDatabase extends AbstractConceptualDomain {
}
