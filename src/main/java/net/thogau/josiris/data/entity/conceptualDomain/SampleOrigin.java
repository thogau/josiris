package net.thogau.josiris.data.entity.conceptualDomain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("SampleOrigin")
@Getter
@Setter
@NoArgsConstructor
public class SampleOrigin extends AbstractConceptualDomain {
}
