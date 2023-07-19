package net.thogau.josiris.data.entity;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
import net.thogau.josiris.data.csv.converter.MConverter;
import net.thogau.josiris.data.csv.converter.NConverter;
import net.thogau.josiris.data.csv.converter.TConverter;
import net.thogau.josiris.data.entity.conceptualDomain.M;
import net.thogau.josiris.data.entity.conceptualDomain.N;
import net.thogau.josiris.data.entity.conceptualDomain.T;

@Entity
@Table(name = "tnm")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tnm extends AbstractEntity {

	public final static String CSV_HEADER = "Patient_Id,Instance_Id,TumorPathologyEvent_Ref,TNM_Version,TNM_Type,TNM_T,TNM_N,TNM_M";

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
		sb.append(tnm_Version + ",");
		sb.append(tnm_Type + ",");
		sb.append((tnm_T != null ? tnm_T.getValueMeaning() : "") + ",");
		sb.append((tnm_N != null ? tnm_N.getValueMeaning() : "") + ",");
		sb.append(tnm_M != null ? tnm_M.getValueMeaning() : "");

		return sb.toString();
	}

	@OneToOne(mappedBy = "tnm")
	TumorPathologyEvent tumorPathologyEvent;

	@Transient
	@Builder.Default
	@CsvBindByName(column = "TumorPathologyEvent_Ref")
	String tumorPathologyEvent_Ref = "UMLS:C0439673";

	@NotNull
	@ManyToOne
	@JoinColumn(name = "t_id")
	@CsvBindByName(column = "TNM_T")
	@CsvCustomBindByName(converter = TConverter.class)
	@Displayable(label = "T")
	T tnm_T;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "n_id")
	@CsvBindByName(column = "TNM_N")
	@CsvCustomBindByName(converter = NConverter.class)
	@Displayable(label = "N")
	N tnm_N;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "m_id")
	@CsvBindByName(column = "TNM_M")
	@CsvCustomBindByName(converter = MConverter.class)
	@Displayable(label = "M")
	M tnm_M;

	@NotEmpty
	@Builder.Default
	@CsvBindByName(column = "TNM_Version")
	@Displayable(label = "TNM version")
	String tnm_Version = "UMLS:C0439673";

	@NotEmpty
	@Builder.Default
	@CsvBindByName(column = "TNM_Type")
	@Displayable(label = "TNM Type")
	String tnm_Type = "UMLS:C0439673";

	@Override
	public String toString() {
		return "Tnm [getId()=" + getId() + ", tumorPathologyEvent=" + tumorPathologyEvent + ", tumorPathologyEvent_Ref="
				+ tumorPathologyEvent_Ref + ", tnm_T=" + tnm_T + ", tmn_N=" + tnm_N + ", tnm_M=" + tnm_M
				+ ", tnm_TNMVersion=" + tnm_Version + ", tnm_G8=" + tnm_Type + "]";
	}

}
