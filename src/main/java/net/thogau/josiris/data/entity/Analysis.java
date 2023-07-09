package net.thogau.josiris.data.entity;

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

	@ManyToOne
	@JoinColumn(name = "biologicalSample_id")
	private BiologicalSample biologicalSample;

	@Transient
	@CsvBindByName(column = "Instance_Id")
	private String instance_Id;

	@Transient
	@Builder.Default
	@CsvBindByName(column = "BiologicalSample_Ref")
	private String BiologicalSample_Ref = "UMLS:C0439673";

	@Builder.Default
	@CsvBindByName(column = "Analysis_Code")
	private String analysis_Code = "UMLS:C0439673";

	@CsvBindByName(column = "Analysis_Date")
	@CsvCustomBindByName(converter = DateConverter.class)
	private Date analysis_Date;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "analysisType_id")
	@CsvBindByName(column = "analysis_Type")
	@CsvCustomBindByName(converter = AnalysisTypeConverter.class)
	private AnalysisType analysis_Type;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "technicalProtocol_id")
	@CsvBindByName(column = "Technology_TechnicalProtocol")
	@CsvCustomBindByName(converter = TechnicalProtocolConverter.class)
	private TechnicalProtocol Technology_TechnicalProtocol;

	private String technology_PlatformName;
	private String technology_PlatformAccession;
	private String technology_DateOfExperiment;
	private String panel_Name;
	private String analysisProcess_AnalyticPipelineCode;
	private String omicAnalysis_AlgorithmicCellularity;
	private String omicAnalysis_AlgorithmicPloidy;
	private String omicAnalysis_NumberOfBreakPoints;

	@OneToMany(mappedBy = "analysis", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Builder.Default
	private List<Fusion> fusions = new ArrayList<>();

}
