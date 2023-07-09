package net.thogau.josiris.data.entity;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thogau.josiris.data.csv.converter.FusionPrimeEndConverter;
import net.thogau.josiris.data.csv.converter.GenomeEntityDatabaseConverter;
import net.thogau.josiris.data.csv.converter.GenomeEntityTypeConverter;
import net.thogau.josiris.data.entity.conceptualDomain.FusionPrimeEnd;
import net.thogau.josiris.data.entity.conceptualDomain.GenomeEntityDatabase;
import net.thogau.josiris.data.entity.conceptualDomain.GenomeEntityType;

@Entity
@Table(name = "Annotation")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Annotation extends AbstractEntity {

	@ManyToOne
	@JoinColumn(name = "fusion_id")
	private Fusion fusion;

	@Transient
	@CsvBindByName(column = "Instance_Id")
	private String instance_Id;

	@Transient
	@Builder.Default
	@CsvBindByName(column = "Alteration_Ref")
	private String alteration_Ref = "UMLS:C0439673";

	@NotNull
	@ManyToOne
	@JoinColumn(name = "genomeEntityType_id")
	@CsvBindByName(column = "GenomeEntity_Type")
	@CsvCustomBindByName(converter = GenomeEntityTypeConverter.class)
	private GenomeEntityType genomeEntity_Type;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "genomeEntityDatabase_id")
	@CsvBindByName(column = "GenomeEntity_Database")
	@CsvCustomBindByName(converter = GenomeEntityDatabaseConverter.class)
	private GenomeEntityDatabase genomeEntity_Database;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "fusionPrimeEnd_id")
	@CsvBindByName(column = "Annotation_FusionPrimeEnd")
	@CsvCustomBindByName(converter = FusionPrimeEndConverter.class)
	private FusionPrimeEnd Annotation_FusionPrimeEnd;

	@Builder.Default
	@CsvBindByName(column = "GenomeEntity_Id")
	private String genomeEntity_Id = "UMLS:C0439673";

	@Builder.Default
	@CsvBindByName(column = "GenomeEntity_Symbol")
	private String genomeEntity_Symbol = "UMLS:C0439673";

	private String Annotation_ReferenceType;
	private String Annotation_ReferenceDatabase;
	private String Annotation_ReferenceValue;
	private String Annotation_MutationPredictionAlgorithm;
	private String Annotation_MutationPredictionValue;
	private String Annotation_MutationPredictionScore;
	private String Annotation_PfamDomain;
	private String Annotation_PfamId;
	private String Annotation_DNARegionName;
	private String Annotation_DNASequenceVariation;
	private String Annotation_AminoAcidChange;
	private String Annotation_GenomicSequenceVariation;
	private String Annotation_RNASequenceVariation;
	private String Annotation_AminoAcidChangeType;
	private String Annotation_Strand;

}
