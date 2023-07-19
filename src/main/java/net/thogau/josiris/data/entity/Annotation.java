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

	public static final String CSV_HEADER = "Patient_Id,Instance_Id,Alteration_Ref,AlterationOnSample_AlterationType,GenomeEntity_Type,GenomeEntity_Database,GenomeEntity_Id,GenomeEntity_Symbol,Annotation_ReferenceType,Annotation_ReferenceDatabase,Annotation_ReferenceValue,Annotation_MutationPredictionAlgorithm,Annotation_MutationPredictionValue,Annotation_MutationPredictionScore,Annotation_PfamDomain,Annotation_PfamId,Annotation_DNARegionName,Annotation_DNASequenceVariation,Annotation_AminoAcidChange,Annotation_GenomicSequenceVariation,Annotation_RNASequenceVariation,Annotation_AminoAcidChangeType,Annotation_FusionPrimeEnd,Annotation_Strand";

	public String getCsvData() {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append((getFusion().getAnalysis().getBiologicalSample().getTumorPathologyEvent().getPatient()
					.getId() != null
							? getFusion().getAnalysis().getBiologicalSample().getTumorPathologyEvent().getPatient()
									.getId()
							: "")
					+ ",");
		} catch (Exception e) {
			sb.append((getFusion().getAnalysis().getBiologicalSample().getTumorPathologyEvent().getParent().getPatient()
					.getId() != null
							? getFusion().getAnalysis().getBiologicalSample().getTumorPathologyEvent().getParent()
									.getPatient().getId()
							: "")
					+ ",");
		}
		sb.append(getId() + ",");
		sb.append(getFusion().getId() + ",");
		sb.append((alterationOnSample_AlterationType != null ? alterationOnSample_AlterationType : "") + ",");
		sb.append((genomeEntity_Type != null ? genomeEntity_Type.getValueMeaning() : "") + ",");
		sb.append((genomeEntity_Database != null ? genomeEntity_Database.getValueMeaning() : "") + ",");
		sb.append((genomeEntity_Id != null ? genomeEntity_Id : "") + ",");
		sb.append((genomeEntity_Symbol != null ? genomeEntity_Symbol : "") + ",");
		sb.append((annotation_ReferenceType != null ? annotation_ReferenceType : "") + ",");
		sb.append((annotation_ReferenceDatabase != null ? annotation_ReferenceDatabase : "") + ",");
		sb.append((annotation_ReferenceValue != null ? annotation_ReferenceValue : "") + ",");
		sb.append((annotation_MutationPredictionAlgorithm != null ? annotation_MutationPredictionAlgorithm : "") + ",");
		sb.append((annotation_MutationPredictionValue != null ? annotation_MutationPredictionValue : "") + ",");
		sb.append((annotation_MutationPredictionScore != null ? annotation_MutationPredictionScore : "") + ",");
		sb.append((annotation_PfamDomain != null ? annotation_PfamDomain : "") + ",");
		sb.append((annotation_PfamId != null ? annotation_PfamId : "") + ",");
		sb.append((annotation_DNARegionName != null ? annotation_DNARegionName : "") + ",");
		sb.append((annotation_DNASequenceVariation != null ? annotation_DNASequenceVariation : "") + ",");
		sb.append((annotation_AminoAcidChange != null ? annotation_AminoAcidChange : "") + ",");
		sb.append((annotation_GenomicSequenceVariation != null ? annotation_GenomicSequenceVariation : "") + ",");
		sb.append((annotation_RNASequenceVariation != null ? annotation_RNASequenceVariation : "") + ",");
		sb.append((annotation_AminoAcidChangeType != null ? annotation_AminoAcidChangeType : "") + ",");
		sb.append((annotation_FusionPrimeEnd != null ? annotation_FusionPrimeEnd.getValueMeaning() : "") + ",");
		sb.append((annotation_Strand != null ? annotation_Strand : ""));
		return sb.toString();
	}

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
	FusionPrimeEnd annotation_FusionPrimeEnd;

	@Builder.Default
	@CsvBindByName(column = "GenomeEntity_Id")
	@Displayable(label = "Entity ID")
	String genomeEntity_Id = "UMLS:C0439673";

	@Builder.Default
	@CsvBindByName(column = "GenomeEntity_Symbol")
	@Displayable(label = "Entity symbol")
	String genomeEntity_Symbol = "UMLS:C0439673";

	String alterationOnSample_AlterationType;
	String annotation_ReferenceType;
	String annotation_ReferenceDatabase;
	String annotation_ReferenceValue;
	String annotation_MutationPredictionAlgorithm;
	String annotation_MutationPredictionValue;
	String annotation_MutationPredictionScore;
	String annotation_PfamDomain;
	String annotation_PfamId;
	String annotation_DNARegionName;
	String annotation_DNASequenceVariation;
	String annotation_AminoAcidChange;
	String annotation_GenomicSequenceVariation;
	String annotation_RNASequenceVariation;
	String annotation_AminoAcidChangeType;
	String annotation_Strand;

}
