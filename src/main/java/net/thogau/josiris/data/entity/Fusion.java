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

	@ManyToOne
	@JoinColumn(name = "analysis_id")
	private Analysis analysis;

	@Transient
	@CsvBindByName(column = "Instance_Id")
	private String instance_Id;

	@Transient
	@Builder.Default
	@CsvBindByName(column = "Analysis_Ref")
	private String analysis_Ref = "UMLS:C0439673";

	@NotNull
	@ManyToOne
	@JoinColumn(name = "alterationType_id")
	@CsvBindByName(column = "AlterationOnSample_AlterationType")
	@CsvCustomBindByName(converter = AlterationTypeConverter.class)
	private AlterationType alterationOnSample_AlterationType;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "chromosome5prime_id")
	@CsvBindByName(column = "Fusion_Chromosome5prime")
	@CsvCustomBindByName(converter = ChromosomeConverter.class)
	private Chromosome fusion_Chromosome5prime;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "chromosome3prime_id")
	@CsvBindByName(column = "Fusion_Chromosome3prime")
	@CsvCustomBindByName(converter = ChromosomeConverter.class)
	private Chromosome fusion_Chromosome3prime;

	private String alterationOnSample_Pathogenicity;
	private String alterationOnSample_Actionability;
	private String alterationOnSample_ProposedForOrientation;
	private String fusion_Type;
	private String fusion_Point5prime;
	private String fusion_Point3prime;
	private String fusion_NbSpanningPair;
	private String fusion_NbSplitReads;
	private String fusion_InFrame;
	private String validation_Type;
	private String validation_Method;
	private String validation_Status;

	@OneToMany(mappedBy = "fusion", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Builder.Default
	private List<Annotation> annotations = new ArrayList<>();
}
