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

	public final static String CSV_HEADER = "Patient_Id,Instance_Id,TumorPathologyEvent_ParentRef,TumorPathologyEvent_Type,TumorPathologyEvent_StartDate,TumorPathologyEvent_PerformanceStatus,TumorPathologyEvent_G8,TumorPathologyEvent_HistologicalGradeType,TumorPathologyEvent_HistologicalGradeValue,TumorPathologyEvent_StadeType,TumorPathologyEvent_StadeValue,TumorPathologyEvent_DiagnosisDate,TumorPathologyEvent_TopographyCode,TumorPathologyEvent_MorphologyCode,TumorPathologyEvent_Laterality";

	public String getCsvData() {
		StringBuffer sb = new StringBuffer();
		sb.append((getPatient() != null ? getPatient().getId() : "") + ",");
		sb.append(getId() + ",");
		sb.append((getParent() != null ? getParent().getId() : "") + ",");
		sb.append((tumorPathologyEvent_Type != null ? tumorPathologyEvent_Type.getValueMeaning() : "") + ",");
		sb.append((tumorPathologyEvent_StartDate != null
				? new SimpleDateFormat("yyyy-MM-dd").format(tumorPathologyEvent_StartDate)
				: "") + ",");
		sb.append(tumorPathologyEvent_PerformanceStatus + ",");
		sb.append(tumorPathologyEvent_G8 + ",");
		sb.append(tumorPathologyEvent_HistologicalGradeType + ",");
		sb.append(tumorPathologyEvent_HistologicalGradeValue + ",");
		sb.append(tumorPathologyEvent_StadeType + ",");
		sb.append(tumorPathologyEvent_StadeValue + ",");
		sb.append((tumorPathologyEvent_DiagnosisDate != null
				? new SimpleDateFormat("yyyy-MM-dd").format(tumorPathologyEvent_DiagnosisDate)
				: "") + ",");
		sb.append(
				(tumorPathologyEvent_TopographyCode != null ? tumorPathologyEvent_TopographyCode.getValueMeaning() : "")
						+ ",");
		sb.append(
				(tumorPathologyEvent_MorphologyCode != null ? tumorPathologyEvent_MorphologyCode.getValueMeaning() : "")
						+ ",");
		sb.append(tumorPathologyEvent_Laterality != null ? tumorPathologyEvent_Laterality.getValueMeaning() : "");
		return sb.toString();
	}

	@Transient
	@CsvBindByName(column = "Patient_Id")
	String patient_Id;

	@Transient
	@CsvBindByName(column = "TumorPathologyEvent_ParentRef")
	String tumorPathologyEvent_ParentRef;

	@Transient
	@CsvBindByName(column = "Instance_Id")
	String instance_Id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "patient_ref")
	Patient patient;

	@OneToMany(mappedBy = "parent", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	List<TumorPathologyEvent> children = new ArrayList<>();

	@ManyToOne
	@JoinColumn(name = "parent_id")
	@Builder.Default
	TumorPathologyEvent parent = null;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "eventtype_id")
	@CsvBindByName(column = "TumorPathologyEvent_Type")
	@CsvCustomBindByName(converter = EventTypeConverter.class)
	@Displayable(label = "Event type")
	EventType tumorPathologyEvent_Type;

	@CsvBindByName(column = "TumorPathologyEvent_StartDate")
	@CsvCustomBindByName(converter = DateConverter.class)
	@Displayable(label = "Start date")
	Date tumorPathologyEvent_StartDate;

	@NotEmpty
	@Builder.Default
	@CsvBindByName(column = "TumorPathologyEvent_PerformanceStatus")
	@Displayable(label = "Performance status")
	String tumorPathologyEvent_PerformanceStatus = "UMLS:C1272460";

	@NotEmpty
	@Builder.Default
	@CsvBindByName(column = "TumorPathologyEvent_G8")
	@Displayable(label = "G8")
	String tumorPathologyEvent_G8 = "UMLS:C0439673";

	@NotEmpty
	@Builder.Default
	@CsvBindByName(column = "TumorPathologyEvent_HistologicalGradeType")
	@Displayable(label = "Grade type")
	String tumorPathologyEvent_HistologicalGradeType = "SarcomaBCB";

	@NotEmpty
	@Builder.Default
	@CsvBindByName(column = "TumorPathologyEvent_HistologicalGradeValue")
	@Displayable(label = "Grade value")
	String tumorPathologyEvent_HistologicalGradeValue = "UMLS:C1272460";

	@NotEmpty
	@Builder.Default
	@CsvBindByName(column = "TumorPathologyEvent_StadeType")
	@Displayable(label = "Stade type")
	String tumorPathologyEvent_StadeType = "UMLS:C0439673";

	@NotEmpty
	@Builder.Default
	@CsvBindByName(column = "TumorPathologyEvent_StadeValue")
	@Displayable(label = "Stade value")
	String tumorPathologyEvent_StadeValue = "UMLS:C0439673";

	@CsvBindByName(column = "TumorPathologyEvent_DiagnosisDate")
	@CsvCustomBindByName(converter = DateConverter.class)
	@Displayable(label = "Diagnosis date")
	Date tumorPathologyEvent_DiagnosisDate;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "topography_id")
	@CsvBindByName(column = "TumorPathologyEvent_TopographyCode")
	@CsvCustomBindByName(converter = TopographyConverter.class)
	@Displayable(label = "Topography")
	Topography tumorPathologyEvent_TopographyCode;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "morphology_id")
	@CsvBindByName(column = "TumorPathologyEvent_MorphologyCode")
	@CsvCustomBindByName(converter = MorphologyConverter.class)
	@Displayable(label = "Morphology")
	Morphology tumorPathologyEvent_MorphologyCode;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "laterality_id")
	@CsvBindByName(column = "TumorPathologyEvent_Laterality")
	@CsvCustomBindByName(converter = LateralityConverter.class)
	@Displayable(label = "Laterality")
	Laterality tumorPathologyEvent_Laterality;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "tnm_id")
	Tnm tnm;

	@OneToMany(mappedBy = "tumorPathologyEvent", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Builder.Default
	List<Treatment> treatments = new ArrayList<>();

	@OneToMany(mappedBy = "tumorPathologyEvent", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Builder.Default
	List<BiologicalSample> biologicalSamples = new ArrayList<>();

}
