package net.thogau.josiris.data.csv;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thogau.josiris.data.csv.converter.DrugConverter;
import net.thogau.josiris.data.entity.conceptualDomain.Drug;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CsvDrug {

	@Transient
	@Builder.Default
	@CsvBindByName(column = "Treatment_Ref")
	private String treatment_Ref = "UMLS:C0439673";

	@NotNull
	@CsvBindByName(column = "Drug_Code")
	@CsvCustomBindByName(converter = DrugConverter.class)
	private Drug drug_Code;

}
