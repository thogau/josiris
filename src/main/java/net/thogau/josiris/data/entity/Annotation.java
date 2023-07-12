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
	Fusion fusion;

	@Transient
	@CsvBindByName(column = "Instance_Id")
	String instance_Id;

	@Transient
	@Builder.Default
	@CsvBindByName(column = "Alteration_Ref")
	String alteration_Ref = "UMLS:C0439673";

	@NotNull
	@ManyToOne
	@JoinColumn(name = "genomeEntityType_id")
	@CsvBindByName(column = "GenomeEntity_Type")
	@CsvCustomBindByName(converter = GenomeEntityTypeConverter.class)
	@Displayable(label = "Entity type")
	GenomeEntityType genomeEntity_Type;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "genomeEntityDatabase_id")
	@CsvBindByName(column = "GenomeEntity_Database")
	@CsvCustomBindByName(converter = GenomeEntityDatabaseConverter.class)
	@Displayable(label = "Entity database")
	GenomeEntityDatabase genomeEntity_Database;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "fusionPrimeEnd_id")
	@CsvBindByName(column = "Annotation_FusionPrimeEnd")
	@CsvCustomBindByName(converter = FusionPrimeEndConverter.class)
	@Displayable(label = "Fusion prime end")
	FusionPrimeEnd Annotation_FusionPrimeEnd;

	@Builder.Default
	@CsvBindByName(column = "GenomeEntity_Id")
	@Displayable(label = "Entity ID")
	String genomeEntity_Id = "UMLS:C0439673";

	@Builder.Default
	@CsvBindByName(column = "GenomeEntity_Symbol")
	@Displayable(label = "Entity symbol")
	String genomeEntity_Symbol = "UMLS:C0439673";

	String Annotation_ReferenceType;
	String Annotation_ReferenceDatabase;
	String Annotation_ReferenceValue;
	String Annotation_MutationPredictionAlgorithm;
	String Annotation_MutationPredictionValue;
	String Annotation_MutationPredictionScore;
	String Annotation_PfamDomain;
	String Annotation_PfamId;
	String Annotation_DNARegionName;
	String Annotation_DNASequenceVariation;
	String Annotation_AminoAcidChange;
	String Annotation_GenomicSequenceVariation;
	String Annotation_RNASequenceVariation;
	String Annotation_AminoAcidChangeType;
	String Annotation_Strand;

}
