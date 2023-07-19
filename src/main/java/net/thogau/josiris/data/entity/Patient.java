package net.thogau.josiris.data.entity;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
import net.thogau.josiris.data.entity.conceptualDomain.AbstractConceptualDomain;
import net.thogau.josiris.data.entity.conceptualDomain.CauseOfDeath;
import net.thogau.josiris.data.entity.conceptualDomain.Drug;
import net.thogau.josiris.data.entity.conceptualDomain.Gender;
import net.thogau.josiris.data.entity.conceptualDomain.LastNewsStatus;
import net.thogau.josiris.views.patient.TreeItem;

@Entity
@Table(name = "patient")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Patient extends AbstractEntity {

	public final static String CSV_HEADER = "Patient_id,Instance_Id,Patient_Gender,Patient_Ethnicity,Patient_BirthDate,Patient_DeathDate,Patient_ProviderCenterId,Patient_OriginCenterId,Patient_CauseOfDeath,Patient_LastNewsDate,Patient_LastNewsStatus";

	public String getCsvData() {
		StringBuffer sb = new StringBuffer();
		sb.append(originalId + ",");
		sb.append(getId() + ",");
		sb.append((patient_Gender != null ? patient_Gender.getValueMeaning() : "") + ",");
		sb.append(patient_Ethnicity + ",");
		sb.append(
				(patient_BirthDate != null ? new SimpleDateFormat("yyyy-MM-dd").format(patient_BirthDate) : "") + ",");
		sb.append(
				(patient_DeathDate != null ? new SimpleDateFormat("yyyy-MM-dd").format(patient_DeathDate) : "") + ",");
		sb.append(patient_ProviderCenterId + ",");
		sb.append(patient_OriginCenterId + ",");
		sb.append((patient_CauseOfDeath != null ? patient_CauseOfDeath.getValueMeaning() : "") + ",");
		sb.append((patient_LastNewsDate != null ? new SimpleDateFormat("yyyy-MM-dd").format(patient_LastNewsDate) : "")
				+ ",");
		sb.append((patient_LastNewsStatus != null ? patient_LastNewsStatus.getValueMeaning() : ""));
		return sb.toString();
	}

	@Transient
	@CsvBindByName(column = "Patient_Id")
	String patient_Id;

	public void setPatient_Id(String s) {
		this.originalId = s;
	}

	@Column(unique = true)
	@NotEmpty
	@Displayable(label = "Original ID")
	String originalId;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "gender_id")
	@CsvBindByName(column = "Patient_Gender")
	@CsvCustomBindByName(converter = GenderConverter.class)
	@Displayable(label = "Gender")
	Gender patient_Gender;

	@NotEmpty
	@Builder.Default
	@CsvBindByName(column = "Patient_Ethnicity")
	@Displayable(label = "Ethnicity")
	String patient_Ethnicity = "UMLS:C0439673";

	@NotNull
	@CsvBindByName(column = "Patient_BirthDate")
	@CsvCustomBindByName(converter = DateConverter.class)
	@Displayable(label = "Birth date")
	Date patient_BirthDate;

	@CsvBindByName(column = "Patient_DeathDate")
	@CsvCustomBindByName(converter = DateConverter.class)
	@Displayable(label = "Death date")
	Date patient_DeathDate;

	@NotEmpty
	@Builder.Default
	@CsvBindByName(column = "Patient_ProviderCenterId")
	@Displayable(label = "Provider center")
	String patient_ProviderCenterId = "75 001 079 5";

	@NotEmpty
	@Builder.Default
	@CsvBindByName(column = "Patient_OriginCenterId")
	@Displayable(label = "Origin center")
	String patient_OriginCenterId = "75 001 079 5";

	@ManyToOne
	@JoinColumn(name = "causeofdeath_id")
	@CsvBindByName(column = "Patient_CauseOfDeath")
	@CsvCustomBindByName(converter = CauseOfDeathConverter.class)
	@Displayable(label = "Cause of death")
	CauseOfDeath patient_CauseOfDeath;

	@CsvBindByName(column = "Patient_LastNewsDate")
	@CsvCustomBindByName(converter = DateConverter.class)
	@Displayable(label = "Last news date")
	Date patient_LastNewsDate;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "lastnewsstatus_id")
	@CsvBindByName(column = "Patient_LastNewsStatus")
	@CsvCustomBindByName(converter = LastNewsStatusConverter.class)
	@Displayable(label = "Last news status")
	LastNewsStatus patient_LastNewsStatus;

	@OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Builder.Default
	List<TumorPathologyEvent> tumorPathologyEvents = new ArrayList<>();

	@Override
	public String toString() {
		return "Patient [getId()=" + getId() + ", originalId=" + originalId + ", patient_Gender=" + patient_Gender
				+ ", patient_Ethnicity=" + patient_Ethnicity + ", patient_BirthDate=" + patient_BirthDate
				+ ", patient_DeathDate=" + patient_DeathDate + ", patient_ProviderCenterId=" + patient_ProviderCenterId
				+ ", patient_OriginCenterId=" + patient_OriginCenterId + ", patient_CauseOfDeath="
				+ patient_CauseOfDeath + ", patient_LastNewsDate=" + patient_LastNewsDate + ", patient_LastNewsStatus="
				+ patient_LastNewsStatus + "]";
	}

	public TreeItem buildTree() {
		TreeItem root = TreeItem.builder().label("ROOT").value("ROOT").build();

		TreeItem patientItem = TreeItem.builder().label("Patient").value(this.getId().toString()).build();
		root.getChildren().add(patientItem);
		addFields(patientItem, this, Patient.class);

		for (TumorPathologyEvent tpe : this.getTumorPathologyEvents()) {
			TreeItem tpeItem = TreeItem.builder().label("Tumour event").value(tpe.getId().toString()).build();
			patientItem.getChildren().add(tpeItem);
			addFields(tpeItem, tpe, TumorPathologyEvent.class);

			if (tpe.getTnm() != null) {
				TreeItem tnmItem = TreeItem.builder().label("TNM").value(tpe.getTnm().getId().toString()).build();
				tpeItem.getChildren().add(tnmItem);
				addFields(tnmItem, tpe.getTnm(), Tnm.class);
			}

			for (BiologicalSample sam : tpe.getBiologicalSamples()) {
				TreeItem samItem = TreeItem.builder().label("Biological sample").value(sam.getId().toString()).build();
				tpeItem.getChildren().add(samItem);
				addFields(samItem, sam, BiologicalSample.class);
				for (Analysis ana : sam.getAnalysises()) {
					TreeItem anaItem = TreeItem.builder().label("Analysis").value(ana.getId().toString()).build();
					samItem.getChildren().add(anaItem);
					addFields(anaItem, ana, Analysis.class);
					for (Fusion fus : ana.getFusions()) {
						TreeItem fusItem = TreeItem.builder().label("Fusion").value(fus.getId().toString()).build();
						anaItem.getChildren().add(fusItem);
						addFields(fusItem, fus, Fusion.class);
						for (Annotation anno : fus.getAnnotations()) {
							TreeItem annoItem = TreeItem.builder().label("Annotation").value(anno.getId().toString())
									.build();
							fusItem.getChildren().add(annoItem);
							addFields(annoItem, anno, Annotation.class);
						}
					}
				}
			}

			for (Treatment ttt : tpe.getTreatments()) {
				TreeItem tttItem = TreeItem.builder().label("Treatment").value(ttt.getId().toString()).build();
				tpeItem.getChildren().add(tttItem);
				addFields(tttItem, ttt, Treatment.class);
				StringBuffer sb = new StringBuffer();
				for (Iterator<Drug> iterator = ttt.getDrugs().iterator(); iterator.hasNext();) {
					Drug d = (Drug) iterator.next();
					sb.append(d.getLabelValueMeaning());
					if (iterator.hasNext()) {
						sb.append(", ");
					}
				}
				tttItem.getChildren().add(TreeItem.builder().label("Drugs").value(sb.toString()).build());
			}

			for (TumorPathologyEvent tpe2 : tpe.getChildren()) {
				TreeItem tpe2Item = TreeItem.builder().label("Tumour event").value(tpe2.getId().toString()).build();
				tpeItem.getChildren().add(tpe2Item);
				addFields(tpe2Item, tpe2, TumorPathologyEvent.class);

				if (tpe2.getTnm() != null) {
					TreeItem tnmItem = TreeItem.builder().label("TNM").value(tpe2.getTnm().getId().toString()).build();
					tpe2Item.getChildren().add(tnmItem);
					addFields(tnmItem, tpe2.getTnm(), Tnm.class);
				}

				for (BiologicalSample sam : tpe2.getBiologicalSamples()) {
					TreeItem samItem = TreeItem.builder().label("Biological sample").value(sam.getId().toString())
							.build();
					tpe2Item.getChildren().add(samItem);
					addFields(samItem, sam, BiologicalSample.class);
				}

				for (Treatment ttt : tpe2.getTreatments()) {
					TreeItem tttItem = TreeItem.builder().label("Treatment").value(ttt.getId().toString()).build();
					tpe2Item.getChildren().add(tttItem);
					addFields(tttItem, ttt, Treatment.class);
				}
			}
		}

		return root;
	}

	private void addFields(TreeItem item, AbstractEntity e, Class<?> c) {
		for (Field field : c.getDeclaredFields()) {
			if (field.isAnnotationPresent(Displayable.class)) {
				String label = field.getAnnotation(Displayable.class).label();
				String value = null;
				try {
					var object = field.get(e);
					if (object != null) {
						if (object instanceof Date) {
							value = new SimpleDateFormat("yyyy-MM-dd").format(object);
						} else if (object instanceof AbstractConceptualDomain) {
							value = ((AbstractConceptualDomain) object).getLabelValueMeaning();
						} else {
							value = object.toString();
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				item.getChildren().add(TreeItem.builder().label(label).value(value).build());
			}
		}
	}

}
