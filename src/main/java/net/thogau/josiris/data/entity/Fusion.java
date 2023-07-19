package net.thogau.josiris.data.entity;

import java.util.ArrayList;
import java.util.List;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thogau.josiris.data.csv.converter.AlterationTypeConverter;
import net.thogau.josiris.data.csv.converter.ChromosomeConverter;
import net.thogau.josiris.data.entity.conceptualDomain.AlterationType;
import net.thogau.josiris.data.entity.conceptualDomain.Chromosome;

@Entity
@Table(name = "Fusion")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Fusion extends AbstractEntity {

	public static final String CSV_HEADER = "Patient_Id,Instance_Id,Analysis_Ref,AlterationOnSample_AlterationType,AlterationOnSample_Pathogenicity,AlterationOnSample_Actionability,AlterationOnSample_ProposedForOrientation,Fusion_Type,Fusion_Chromosome5prime,Fusion_Point5prime,Fusion_Chromosome3prime,Fusion_Point3prime,Fusion_NbSpanningPair,Fusion_NbSplitReads,Fusion_InFrame,Validation_Type,Validation_Method,Validation_Status";

	public String getCsvData() {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append((getAnalysis().getBiologicalSample().getTumorPathologyEvent().getPatient().getId() != null
					? getAnalysis().getBiologicalSample().getTumorPathologyEvent().getPatient().getId()
					: "") + ",");
		} catch (Exception e) {
			sb.append((getAnalysis().getBiologicalSample().getTumorPathologyEvent().getParent().getPatient()
					.getId() != null
							? getAnalysis().getBiologicalSample().getTumorPathologyEvent().getParent().getPatient()
									.getId()
							: "")
					+ ",");
		}
		sb.append(getId() + ",");
		sb.append(getAnalysis().getId() + ",");
		sb.append((alterationOnSample_AlterationType != null ? alterationOnSample_AlterationType.getValueMeaning() : "")
				+ ",");
		sb.append((alterationOnSample_Pathogenicity != null ? alterationOnSample_Pathogenicity : "") + ",");
		sb.append((alterationOnSample_Actionability != null ? alterationOnSample_Actionability : "") + ",");
		sb.append((alterationOnSample_ProposedForOrientation != null ? alterationOnSample_ProposedForOrientation : "")
				+ ",");
		sb.append((fusion_Type != null ? fusion_Type : "") + ",");
		sb.append((fusion_Chromosome5prime != null ? fusion_Chromosome5prime.getValueMeaning() : "") + ",");
		sb.append((fusion_Point5prime != null ? fusion_Point5prime : "") + ",");
		sb.append((fusion_Chromosome3prime != null ? fusion_Chromosome3prime.getValueMeaning() : "") + ",");
		sb.append((fusion_Point3prime != null ? fusion_Point3prime : "") + ",");
		sb.append((fusion_NbSpanningPair != null ? fusion_NbSpanningPair : "") + ",");
		sb.append((fusion_NbSplitReads != null ? fusion_NbSplitReads : "") + ",");
		sb.append((fusion_InFrame != null ? fusion_InFrame : "") + ",");
		sb.append((validation_Type != null ? validation_Type : "") + ",");
		sb.append((validation_Method != null ? validation_Method : "") + ",");
		sb.append(validation_Status != null ? validation_Status : "");

		return sb.toString();
	}

	@ManyToOne
	@JoinColumn(name = "analysis_id")
	Analysis analysis;

	@Transient
	@CsvBindByName(column = "Instance_Id")
	String instance_Id;

	@Transient
	@Builder.Default
	@CsvBindByName(column = "Analysis_Ref")
	String analysis_Ref = "UMLS:C0439673";

	@NotNull
	@ManyToOne
	@JoinColumn(name = "alterationType_id")
	@CsvBindByName(column = "AlterationOnSample_AlterationType")
	@CsvCustomBindByName(converter = AlterationTypeConverter.class)
	@Displayable(label = "Alteration type")
	AlterationType alterationOnSample_AlterationType;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "chromosome5prime_id")
	@CsvBindByName(column = "Fusion_Chromosome5prime")
	@CsvCustomBindByName(converter = ChromosomeConverter.class)
	@Displayable(label = "Chromosome 5'")
	Chromosome fusion_Chromosome5prime;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "chromosome3prime_id")
	@CsvBindByName(column = "Fusion_Chromosome3prime")
	@CsvCustomBindByName(converter = ChromosomeConverter.class)
	@Displayable(label = "Chromosome 3'")
	Chromosome fusion_Chromosome3prime;

	String alterationOnSample_Pathogenicity;
	String alterationOnSample_Actionability;
	String alterationOnSample_ProposedForOrientation;
	String fusion_Type;
	String fusion_Point5prime;
	String fusion_Point3prime;
	String fusion_NbSpanningPair;
	String fusion_NbSplitReads;
	String fusion_InFrame;
	String validation_Type;
	String validation_Method;
	String validation_Status;

	@OneToMany(mappedBy = "fusion", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Builder.Default
	List<Annotation> annotations = new ArrayList<>();
}
