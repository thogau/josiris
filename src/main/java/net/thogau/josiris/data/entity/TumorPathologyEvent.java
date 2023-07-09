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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thogau.josiris.data.csv.converter.DateConverter;
import net.thogau.josiris.data.csv.converter.EventTypeConverter;
import net.thogau.josiris.data.csv.converter.LateralityConverter;
import net.thogau.josiris.data.csv.converter.MorphologyConverter;
import net.thogau.josiris.data.csv.converter.TopographyConverter;
import net.thogau.josiris.data.entity.conceptualDomain.EventType;
import net.thogau.josiris.data.entity.conceptualDomain.Laterality;
import net.thogau.josiris.data.entity.conceptualDomain.Morphology;
import net.thogau.josiris.data.entity.conceptualDomain.Topography;

@Entity
@Table(name = "tumorpathologyevent")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TumorPathologyEvent extends AbstractEntity {

	@Transient
	@CsvBindByName(column = "Patient_Id")
	private String patient_Id;

	@Transient
	@CsvBindByName(column = "TumorPathologyEvent_ParentRef")
	private String tumorPathologyEvent_ParentRef;

	@Transient
	@CsvBindByName(column = "Instance_Id")
	private String instance_Id;

	@ManyToOne
	@JoinColumn(name = "patient_ref")
	private Patient patient;

	@OneToMany(mappedBy = "parent", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<TumorPathologyEvent> children = new ArrayList<>();

	@ManyToOne
	@JoinColumn(name = "parent_id")
	@Builder.Default
	private TumorPathologyEvent parent = null;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "eventtype_id")
	@CsvBindByName(column = "TumorPathologyEvent_Type")
	@CsvCustomBindByName(converter = EventTypeConverter.class)
	private EventType tumorPathologyEvent_Type;

	@CsvBindByName(column = "TumorPathologyEvent_StartDate")
	@CsvCustomBindByName(converter = DateConverter.class)
	private Date tumorPathologyEvent_StartDate;

	@NotEmpty
	@Builder.Default
	@CsvBindByName(column = "TumorPathologyEvent_PerformanceStatus")
	private String tumorPathologyEvent_PerformanceStatus = "UMLS:C1272460";

	@NotEmpty
	@Builder.Default
	@CsvBindByName(column = "TumorPathologyEvent_G8")
	private String TumorPathologyEvent_G8 = "UMLS:C0439673";

	@NotEmpty
	@Builder.Default
	@CsvBindByName(column = "TumorPathologyEvent_HistologicalGradeType")
	private String tumorPathologyEvent_HistologicalGradeType = "SarcomaBCB";

	@NotEmpty
	@Builder.Default
	@CsvBindByName(column = "TumorPathologyEvent_HistologicalGradeValue")
	private String tumorPathologyEvent_HistologicalGradeValue = "UMLS:C1272460";

	@NotEmpty
	@Builder.Default
	@CsvBindByName(column = "TumorPathologyEvent_StadeType")
	private String tumorPathologyEvent_StadeType = "UMLS:C0439673";

	@NotEmpty
	@Builder.Default
	@CsvBindByName(column = "TumorPathologyEvent_StadeValue")
	private String tumorPathologyEvent_StadeValue = "UMLS:C0439673";

	@CsvBindByName(column = "TumorPathologyEvent_DiagnosisDate")
	@CsvCustomBindByName(converter = DateConverter.class)
	private Date TumorPathologyEvent_DiagnosisDate;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "topography_id")
	@CsvBindByName(column = "TumorPathologyEvent_TopographyCode")
	@CsvCustomBindByName(converter = TopographyConverter.class)
	private Topography tumorPathologyEvent_TopographyCode;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "morphology_id")
	@CsvBindByName(column = "TumorPathologyEvent_MorphologyCode")
	@CsvCustomBindByName(converter = MorphologyConverter.class)
	private Morphology tumorPathologyEvent_MorphologyCode;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "laterality_id")
	@CsvBindByName(column = "TumorPathologyEvent_Laterality")
	@CsvCustomBindByName(converter = LateralityConverter.class)
	private Laterality tumorPathologyEvent_Laterality;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "tnm_id")
	private Tnm tnm;

	@OneToMany(mappedBy = "tumorPathologyEvent", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Builder.Default
	private List<Treatment> treatments = new ArrayList<>();

	@OneToMany(mappedBy = "tumorPathologyEvent", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Builder.Default
	private List<BiologicalSample> biologicalSamples = new ArrayList<>();

	@Override
	public String toString() {
		return "TumorPathologyEvent [getId()=" + getId() + ", patient_Id=" + patient_Id
				+ ", tumorPathologyEvent_ParentRef=" + tumorPathologyEvent_ParentRef + ", instance_Id=" + instance_Id
				+ ", children=" + children + ", tumorPathologyEvent_Type=" + tumorPathologyEvent_Type
				+ ", tumorPathologyEvent_StartDate=" + tumorPathologyEvent_StartDate
				+ ", tumorPathologyEvent_PerformanceStatus=" + tumorPathologyEvent_PerformanceStatus
				+ ", TumorPathologyEvent_G8=" + TumorPathologyEvent_G8 + ", tumorPathologyEvent_HistologicalGradeType="
				+ tumorPathologyEvent_HistologicalGradeType + ", tumorPathologyEvent_HistologicalGradeValue="
				+ tumorPathologyEvent_HistologicalGradeValue + ", tumorPathologyEvent_StadeType="
				+ tumorPathologyEvent_StadeType + ", tumorPathologyEvent_StadeValue=" + tumorPathologyEvent_StadeValue
				+ ", TumorPathologyEvent_DiagnosisDate=" + TumorPathologyEvent_DiagnosisDate
				+ ", tumorPathologyEvent_TopographyCode=" + tumorPathologyEvent_TopographyCode
				+ ", tumorPathologyEvent_MorphologyCode=" + tumorPathologyEvent_MorphologyCode
				+ ", tumorPathologyEvent_Laterality=" + tumorPathologyEvent_Laterality + "]";
	}

}
