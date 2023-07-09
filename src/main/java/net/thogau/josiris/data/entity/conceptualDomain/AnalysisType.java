package net.thogau.josiris.data.entity.conceptualDomain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("AnalysisType")
@Getter
@Setter
@NoArgsConstructor
public class AnalysisType extends AbstractConceptualDomain {
}
