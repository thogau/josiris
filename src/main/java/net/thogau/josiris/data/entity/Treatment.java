package net.thogau.josiris.data.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;

import jakarta.persistence.Entity;
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
import net.thogau.josiris.data.entity.conceptualDomain.Boolean;
import net.thogau.josiris.data.entity.conceptualDomain.Drug;
import net.thogau.josiris.data.entity.conceptualDomain.SurgeryResectionQuality;

@Entity
@Table(name = "treatment")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Treatment extends AbstractEntity {

	@ManyToOne
	@JoinColumn(name = "tumorPathologyEvent_id")
	private TumorPathologyEvent tumorPathologyEvent;

	@Transient
	@CsvBindByName(column = "Instance_Id")
	private String instance_Id;

	@Transient
	@Builder.Default
	@CsvBindByName(column = "TumorPathologyEvent_Ref")
	private String tumorPathologyEvent_Ref = "UMLS:C0439673";

	@NotEmpty
	@Builder.Default
	@CsvBindByName(column = "Treatment_LineNumber")
	private String treatment_LineNumber = "UMLS:C0439673";

	@NotEmpty
	@Builder.Default
	@CsvBindByName(column = "Treatment_ActivityCode")
	private String treatment_ActivityCode = "UMLS:C0439673";

	@CsvBindByName(column = "Treatment_StartDate")
	@CsvCustomBindByName(converter = DateConverter.class)
	private Date treatment_StartDate;

	@CsvBindByName(column = "Treatment_EndDate")
	@CsvCustomBindByName(converter = DateConverter.class)
	private Date treatment_EndDate;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "clinicalTrialContext_id")
	@CsvBindByName(column = "Treatment_ClinicalTrialContext")
	@CsvCustomBindByName(converter = BooleanConverter.class)
	private Boolean treatment_ClinicalTrialContext;

	@CsvBindByName(column = "Treatment_ClinicalTrialName")
	private String treatment_ClinicalTrialName;

	@CsvBindByName(column = "Treatment_ClinicalTrialId")
	private String treatment_ClinicalTrialId;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "surgeryResectionQuality_id")
	@CsvBindByName(column = "Treatment_SurgeryResectionQuality")
	@CsvCustomBindByName(converter = SurgeryResectionQualityConverter.class)
	private SurgeryResectionQuality treatment_SurgeryResectionQuality;

	@NotEmpty
	@Builder.Default
	@CsvBindByName(column = "Treatment_SurgeryNature")
	private String treatment_SurgeryNature = "UMLS:C0439673";

	@ManyToMany
	@JoinTable(name = "treatment_drug", joinColumns = @JoinColumn(name = "treatment_id"), inverseJoinColumns = @JoinColumn(name = "drug_id"), uniqueConstraints = {
			@UniqueConstraint(name = "duplicate_drug_for_treatment", columnNames = { "treatment_id", "drug_id" }) })
	@Builder.Default
	private Set<Drug> drugs = new HashSet<>();

}
