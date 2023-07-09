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
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thogau.josiris.data.csv.converter.DateConverter;
import net.thogau.josiris.data.csv.converter.SampleNatureConverter;
import net.thogau.josiris.data.csv.converter.SampleOriginConverter;
import net.thogau.josiris.data.csv.converter.StorageTemperatureConverter;
import net.thogau.josiris.data.entity.conceptualDomain.SampleNature;
import net.thogau.josiris.data.entity.conceptualDomain.SampleOrigin;
import net.thogau.josiris.data.entity.conceptualDomain.StorageTemperature;

@Entity
@Table(name = "biologicalSample")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BiologicalSample extends AbstractEntity {

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
	@CsvBindByName(column = "Consent_Ref")
	private String consent_Ref = "UMLS:C0439673";

	@NotEmpty
	@Builder.Default
	@CsvBindByName(column = "BiologicalSample_ExternalAccession")
	private String biologicalSample_ExternalAccession = "UMLS:C0439673";

	@NotEmpty
	@Builder.Default
	@CsvBindByName(column = "BiologicalSample_ParentExternalAccession")
	private String biologicalSample_ParentExternalAccession = "UMLS:C1272460";

	@NotEmpty
	@Builder.Default
	@CsvBindByName(column = "BiologicalSample_TumorCellularity")
	private String BiologicalSample_TumorCellularity = "UMLS:C1272460";

	@CsvBindByName(column = "BiologicalSample_CollectDate")
	@CsvCustomBindByName(converter = DateConverter.class)
	private Date biologicalSample_CollectDate;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "sampleNature_id")
	@CsvBindByName(column = "BiologicalSample_Nature")
	@CsvCustomBindByName(converter = SampleNatureConverter.class)
	private SampleNature biologicalSample_Nature;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "sampleOrigin_id")
	@CsvBindByName(column = "BiologicalSample_Origin")
	@CsvCustomBindByName(converter = SampleOriginConverter.class)
	private SampleOrigin biologicalSample_Origin;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "storageTemperature_id")
	@CsvBindByName(column = "BiologicalSample_StorageTemperature")
	@CsvCustomBindByName(converter = StorageTemperatureConverter.class)
	private StorageTemperature BiologicalSample_StorageTemperature;

	@OneToMany(mappedBy = "biologicalSample", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Builder.Default
	private List<Analysis> analysises = new ArrayList<>();
}
