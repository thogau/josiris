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
	@CsvBindByName(column = "TNM_TNMVersion")
	@Displayable(label = "TNM version")
	String tnm_TNMVersion = "UMLS:C0439673";

	@NotEmpty
	@Builder.Default
	@CsvBindByName(column = "TNM_G8")
	@Displayable(label = "TNM G8")
	String tnm_G8 = "UMLS:C0439673";

	@Override
	public String toString() {
		return "Tnm [getId()=" + getId() + ", tumorPathologyEvent=" + tumorPathologyEvent + ", tumorPathologyEvent_Ref="
				+ tumorPathologyEvent_Ref + ", tnm_T=" + tnm_T + ", tmn_N=" + tnm_N + ", tnm_M=" + tnm_M
				+ ", tnm_TNMVersion=" + tnm_TNMVersion + ", tnm_G8=" + tnm_G8 + "]";
	}

}
