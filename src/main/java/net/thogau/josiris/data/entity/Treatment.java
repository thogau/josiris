package net.thogau.josiris.data.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thogau.josiris.data.csv.converter.BooleanConverter;
import net.thogau.josiris.data.csv.converter.DateConverter;
import net.thogau.josiris.data.csv.converter.SurgeryResectionQualityConverter;
import net.thogau.josiris.data.csv.converter.TreatmentTypeConverter;
import net.thogau.josiris.data.entity.conceptualDomain.Boolean;
import net.thogau.josiris.data.entity.conceptualDomain.Drug;
import net.thogau.josiris.data.entity.conceptualDomain.SurgeryResectionQuality;
import net.thogau.josiris.data.entity.conceptualDomain.TreatmentType;

@Entity
@Table(name = "treatment")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Treatment extends AbstractEntity {

	public static final String CSV_HEADER = "Patient_Id,Instance_Id,TumorPathologyEvent_Ref,Treatment_Type,Treatment_LineNumber,Treatment_ActivityCode,Treatment_StartDate,Treatment_EndDate,Treatment_ClinicalTrialContext,Treatment_ClinicalTrialName,Treatment_ClinicalTrialId,Treatment_SurgeryResectionQuality,Treatment_SurgeryNature";

	public String getCsvData() {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append((getTumorPathologyEvent().getPatient().getId() != null
					? getTumorPathologyEvent().getPatient().getId()
					: "") + ",");
		} catch (Exception e) {
			sb.append((getTumorPathologyEvent().getParent().getPatient().getId() != null
					? getTumorPathologyEvent().getParent().getPatient().getId()
					: "") + ",");
		}
		sb.append(getId() + ",");
		sb.append(getTumorPathologyEvent().getId() + ",");
		sb.append((treatment_Type != null ? treatment_Type.getValueMeaning() : "") + ",");
		sb.append(treatment_LineNumber + ",");
		sb.append(treatment_ActivityCode + ",");
		sb.append((treatment_StartDate != null ? new SimpleDateFormat("yyyy-MM-dd").format(treatment_StartDate) : "")
				+ ",");
		sb.append(
				(treatment_EndDate != null ? new SimpleDateFormat("yyyy-MM-dd").format(treatment_EndDate) : "") + ",");
		sb.append(
				(treatment_ClinicalTrialContext != null ? treatment_ClinicalTrialContext.getValueMeaning() : "") + ",");
		sb.append(treatment_ClinicalTrialName + ",");
		sb.append(treatment_ClinicalTrialId + ",");
		sb.append((treatment_SurgeryResectionQuality != null ? treatment_SurgeryResectionQuality.getValueMeaning() : "")
				+ ",");
		sb.append(treatment_SurgeryNature);

		return sb.toString();
	}

	@ManyToOne
	@JoinColumn(name = "tumorPathologyEvent_id")
	TumorPathologyEvent tumorPathologyEvent;

	@Transient
	@CsvBindByName(column = "Instance_Id")
	String instance_Id;

	@Transient
	@Builder.Default
	@CsvBindByName(column = "TumorPathologyEvent_Ref")
	String tumorPathologyEvent_Ref = "UMLS:C0439673";

	@NotNull
	@ManyToOne
	@JoinColumn(name = "treatment_type_id")
	@CsvBindByName(column = "Treatment_Type")
	@CsvCustomBindByName(converter = TreatmentTypeConverter.class)
	@Displayable(label = "Treatment type")
	TreatmentType treatment_Type;

	@NotEmpty
	@Builder.Default
	@CsvBindByName(column = "Treatment_LineNumber")
	@Displayable(label = "Line number")
	String treatment_LineNumber = "UMLS:C0439673";

	@NotEmpty
	@Builder.Default
	@CsvBindByName(column = "Treatment_ActivityCode")
	@Displayable(label = "Activity code")
	String treatment_ActivityCode = "UMLS:C0439673";

	@CsvBindByName(column = "Treatment_StartDate")
	@CsvCustomBindByName(converter = DateConverter.class)
	@Displayable(label = "Start date")
	Date treatment_StartDate;

	@CsvBindByName(column = "Treatment_EndDate")
	@CsvCustomBindByName(converter = DateConverter.class)
	@Displayable(label = "End date")
	Date treatment_EndDate;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "clinicalTrialContext_id")
	@CsvBindByName(column = "Treatment_ClinicalTrialContext")
	@CsvCustomBindByName(converter = BooleanConverter.class)
	@Displayable(label = "Clinical trial")
	Boolean treatment_ClinicalTrialContext;

	@CsvBindByName(column = "Treatment_ClinicalTrialName")
	@Displayable(label = "Clinical trial name")
	String treatment_ClinicalTrialName;

	@CsvBindByName(column = "Treatment_ClinicalTrialId")
	@Displayable(label = "Clinical trial ID")
	String treatment_ClinicalTrialId;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "surgeryResectionQuality_id")
	@CsvBindByName(column = "Treatment_SurgeryResectionQuality")
	@CsvCustomBindByName(converter = SurgeryResectionQualityConverter.class)
	@Displayable(label = "Surgery resection quality")
	SurgeryResectionQuality treatment_SurgeryResectionQuality;

	@NotEmpty
	@Builder.Default
	@CsvBindByName(column = "Treatment_SurgeryNature")
	@Displayable(label = "Surgery nature")
	String treatment_SurgeryNature = "UMLS:C0439673";

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "treatment_drug", joinColumns = @JoinColumn(name = "treatment_id"), inverseJoinColumns = @JoinColumn(name = "drug_id"), uniqueConstraints = {
			@UniqueConstraint(name = "duplicate_drug_for_treatment", columnNames = { "treatment_id", "drug_id" }) })
	@Builder.Default
	Set<Drug> drugs = new HashSet<>();

}
