package net.thogau.josiris.data.entity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import net.thogau.josiris.data.csv.converter.AnalysisTypeConverter;
import net.thogau.josiris.data.csv.converter.DateConverter;
import net.thogau.josiris.data.csv.converter.TechnicalProtocolConverter;
import net.thogau.josiris.data.entity.conceptualDomain.AnalysisType;
import net.thogau.josiris.data.entity.conceptualDomain.TechnicalProtocol;

@Entity
@Table(name = "Analysis")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Analysis extends AbstractEntity {

	public static final String CSV_HEADER = "Patient_Id,Instance_Id,TumorPathologyEvent_Ref,BiologicalSample_Ref,Analysis_Code,Analysis_Type,Analysis_Date,Technology_TechnicalProtocol,Technology_PlatformName,Technology_PlatformAccession,Technology_DateOfExperiment,Panel_Name,AnalysisProcess_AnalyticPipelineCode,OmicAnalysis_AlgorithmicCellularity,OmicAnalysis_AlgorithmicPloidy,OmicAnalysis_NumberOfBreakPoints";

	public String getCsvData() {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append((getBiologicalSample().getTumorPathologyEvent().getPatient().getId() != null
					? getBiologicalSample().getTumorPathologyEvent().getPatient().getId()
					: "") + ",");
		} catch (Exception e) {
			sb.append((getBiologicalSample().getTumorPathologyEvent().getParent().getPatient().getId() != null
					? getBiologicalSample().getTumorPathologyEvent().getParent().getPatient().getId()
					: "") + ",");
		}
		sb.append(getId() + ",");
		sb.append(getBiologicalSample().getTumorPathologyEvent().getId() + ",");
		sb.append(getBiologicalSample().getId() + ",");
		sb.append(analysis_Code + ",");
		sb.append((analysis_Type != null ? analysis_Type.getValueMeaning() : "") + ",");
		sb.append((analysis_Date != null ? new SimpleDateFormat("yyyy-MM-dd").format(analysis_Date) : "") + ",");
		sb.append((technology_TechnicalProtocol != null ? technology_TechnicalProtocol.getValueMeaning() : "") + ",");
		sb.append((technology_PlatformName != null ? technology_PlatformName : "") + ",");
		sb.append((technology_PlatformAccession != null ? technology_PlatformAccession : "") + ",");
		sb.append((technology_DateOfExperiment != null ? technology_DateOfExperiment : "") + ",");
		sb.append((panel_Name != null ? panel_Name : "") + ",");
		sb.append((analysisProcess_AnalyticPipelineCode != null ? analysisProcess_AnalyticPipelineCode : "") + ",");
		sb.append((omicAnalysis_AlgorithmicCellularity != null ? omicAnalysis_AlgorithmicCellularity : "") + ",");
		sb.append((omicAnalysis_AlgorithmicPloidy != null ? omicAnalysis_AlgorithmicPloidy : "") + ",");
		sb.append((omicAnalysis_NumberOfBreakPoints != null ? omicAnalysis_NumberOfBreakPoints : ""));

		return sb.toString();
	}

	@ManyToOne
	@JoinColumn(name = "biologicalSample_id")
	BiologicalSample biologicalSample;

	@Transient
	@CsvBindByName(column = "Instance_Id")
	String instance_Id;

	@Transient
	@Builder.Default
	@CsvBindByName(column = "BiologicalSample_Ref")
	String BiologicalSample_Ref = "UMLS:C0439673";

	@Builder.Default
	@CsvBindByName(column = "Analysis_Code")
	@Displayable(label = "Code")
	String analysis_Code = "UMLS:C0439673";

	@CsvBindByName(column = "Analysis_Date")
	@CsvCustomBindByName(converter = DateConverter.class)
	@Displayable(label = "Date")
	Date analysis_Date;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "analysisType_id")
	@CsvBindByName(column = "analysis_Type")
	@CsvCustomBindByName(converter = AnalysisTypeConverter.class)
	@Displayable(label = "Type")
	AnalysisType analysis_Type;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "technicalProtocol_id")
	@CsvBindByName(column = "Technology_TechnicalProtocol")
	@CsvCustomBindByName(converter = TechnicalProtocolConverter.class)
	@Displayable(label = "Technical protocol")
	TechnicalProtocol technology_TechnicalProtocol;

	String technology_PlatformName;
	String technology_PlatformAccession;
	String technology_DateOfExperiment;
	String panel_Name;
	String analysisProcess_AnalyticPipelineCode;
	String omicAnalysis_AlgorithmicCellularity;
	String omicAnalysis_AlgorithmicPloidy;
	String omicAnalysis_NumberOfBreakPoints;

	@OneToMany(mappedBy = "analysis", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Builder.Default
	List<Fusion> fusions = new ArrayList<>();

}
