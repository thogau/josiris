package net.thogau.josiris.data.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thogau.josiris.data.csv.converter.CauseOfDeathConverter;
import net.thogau.josiris.data.csv.converter.DateConverter;
import net.thogau.josiris.data.csv.converter.GenderConverter;
import net.thogau.josiris.data.csv.converter.LastNewsStatusConverter;
import net.thogau.josiris.data.entity.conceptualDomain.CauseOfDeath;
import net.thogau.josiris.data.entity.conceptualDomain.Gender;
import net.thogau.josiris.data.entity.conceptualDomain.LastNewsStatus;

@Entity
@Table(name = "patient")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Patient extends AbstractEntity {

	@Column(unique = true)
	@NotEmpty
	private String originalId;

	@Transient
	@CsvBindByName(column = "Patient_Id")
	private String patient_Id;

	public void setPatient_Id(String s) {
		this.originalId = s;
	}

	@NotNull
	@ManyToOne
	@JoinColumn(name = "gender_id")
	@CsvBindByName(column = "Patient_Gender")
	@CsvCustomBindByName(converter = GenderConverter.class)
	private Gender patient_Gender;

	@NotEmpty
	@Builder.Default
	@CsvBindByName(column = "Patient_Ethnicity")
	private String patient_Ethnicity = "UMLS:C0439673";

	@NotNull
	@CsvBindByName(column = "Patient_BirthDate")
	@CsvCustomBindByName(converter = DateConverter.class)
	private Date patient_BirthDate;

	@CsvBindByName(column = "Patient_DeathDate")
	@CsvCustomBindByName(converter = DateConverter.class)
	private Date patient_DeathDate;

	@NotEmpty
	@Builder.Default
	@CsvBindByName(column = "Patient_ProviderCenterId")
	private String patient_ProviderCenterId = "75 001 079 5";

	@NotEmpty
	@Builder.Default
	@CsvBindByName(column = "Patient_OriginCenterId")
	private String patient_OriginCenterId = "75 001 079 5";

	@ManyToOne
	@JoinColumn(name = "causeofdeath_id")
	@CsvBindByName(column = "Patient_CauseOfDeath")
	@CsvCustomBindByName(converter = CauseOfDeathConverter.class)
	private CauseOfDeath patient_CauseOfDeath;

	@CsvBindByName(column = "Patient_LastNewsDate")
	@CsvCustomBindByName(converter = DateConverter.class)
	private Date patient_LastNewsDate;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "lastnewsstatus_id")
	@CsvBindByName(column = "Patient_LastNewsStatus")
	@CsvCustomBindByName(converter = LastNewsStatusConverter.class)
	private LastNewsStatus patient_LastNewsStatus;

	@OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Builder.Default
	private List<TumorPathologyEvent> tumorPathologyEvents = new ArrayList<>();

	@Override
	public String toString() {
		return "Patient [getId()=" + getId() + ", originalId=" + originalId + ", patient_Gender=" + patient_Gender
				+ ", patient_Ethnicity=" + patient_Ethnicity + ", patient_BirthDate=" + patient_BirthDate
				+ ", patient_DeathDate=" + patient_DeathDate + ", patient_ProviderCenterId=" + patient_ProviderCenterId
				+ ", patient_OriginCenterId=" + patient_OriginCenterId + ", patient_CauseOfDeath="
				+ patient_CauseOfDeath + ", patient_LastNewsDate=" + patient_LastNewsDate + ", patient_LastNewsStatus="
				+ patient_LastNewsStatus + "]";
	}

}
