package net.thogau.josiris.views.patient;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import net.thogau.josiris.data.entity.Analysis;
import net.thogau.josiris.data.entity.Annotation;
import net.thogau.josiris.data.entity.BiologicalSample;
import net.thogau.josiris.data.entity.Fusion;
import net.thogau.josiris.data.entity.Patient;
import net.thogau.josiris.data.entity.Tnm;
import net.thogau.josiris.data.entity.Treatment;
import net.thogau.josiris.data.entity.TumorPathologyEvent;
import net.thogau.josiris.data.entity.conceptualDomain.AbstractConceptualDomain;
import net.thogau.josiris.data.entity.conceptualDomain.CauseOfDeath;
import net.thogau.josiris.data.entity.conceptualDomain.Drug;
import net.thogau.josiris.data.entity.conceptualDomain.Gender;
import net.thogau.josiris.data.entity.conceptualDomain.LastNewsStatus;
import net.thogau.josiris.data.entity.conceptualDomain.Laterality;
import net.thogau.josiris.data.entity.conceptualDomain.Morphology;
import net.thogau.josiris.data.entity.conceptualDomain.Topography;
import net.thogau.josiris.data.service.ConceptualDomainService;
import net.thogau.josiris.data.service.PatientService;
import net.thogau.josiris.views.MainLayout;

@Route(value = "export", layout = MainLayout.class)
@AnonymousAllowed
public class ExportView extends HorizontalLayout {

	PatientService service;
	ConceptualDomainService cdService;
	Checkbox anonymizeCheckbox = new Checkbox();
	Button exportButton = new Button("Export");
	private static int patientCount = 1;
	private static int drugCount = 1;

	public final static String[] supportedFiles = new String[] { "OSIRIS_pivot_Patient.csv",
			"OSIRIS_pivot_TumorPathologyEvent.csv", "OSIRIS_pivot_TNM.csv", "OSIRIS_pivot_Treatment.csv",
			"OSIRIS_pivot_Drug.csv", "OSIRIS_pivot_BiologicalSample.csv", "OSIRIS_pivot_Analysis.csv",
			"OSIRIS_pivot_Fusion.csv", "OSIRIS_pivot_Annotation.csv" };

	public ExportView(PatientService service, ConceptualDomainService cdService,
			AutowireCapableBeanFactory beanFactory) {
		this.service = service;
		this.cdService = cdService;
		anonymizeCheckbox.setLabel("Anonymize patients");
		anonymizeCheckbox.addClassName("anonymizeCheckbox");
		exportButton.addClickListener(click -> export());
		add(exportButton, anonymizeCheckbox);
		setPadding(true);
	}

	private void export() {
		List<Patient> patients = service.getAll();
		if (anonymizeCheckbox.getValue()) {
			LoggerFactory.getLogger(getClass().getName()).info("Anonymizing patients.");
			for (Patient p : patients) {
				anonymizePatient(p);
			}
			LoggerFactory.getLogger(getClass().getName()).warn("Anonymising done.");
		}

		LoggerFactory.getLogger(getClass().getName()).warn("Exporting patients.");
		try (FileOutputStream patientFile = new FileOutputStream("export/OSIRIS_pivot_Patient.csv");
				FileOutputStream tpeFile = new FileOutputStream("export/OSIRIS_pivot_TumorPathologyEvent.csv");
				FileOutputStream tnmFile = new FileOutputStream("export/OSIRIS_pivot_TNM.csv");
				FileOutputStream tttFile = new FileOutputStream("export/OSIRIS_pivot_Treatment.csv");
				FileOutputStream drugFile = new FileOutputStream("export/OSIRIS_pivot_Drug.csv");
				FileOutputStream sampleFile = new FileOutputStream("export/OSIRIS_pivot_BiologicalSample.csv");
				FileOutputStream analysisFile = new FileOutputStream("export/OSIRIS_pivot_Analysis.csv");
				FileOutputStream fusionFile = new FileOutputStream("export/OSIRIS_pivot_Fusion.csv");
				FileOutputStream annotationFile = new FileOutputStream("export/OSIRIS_pivot_Annotation.csv")) {

			patientFile.write((Patient.CSV_HEADER + System.getProperty("line.separator")).getBytes());
			tpeFile.write((TumorPathologyEvent.CSV_HEADER + System.getProperty("line.separator")).getBytes());
			tnmFile.write((Tnm.CSV_HEADER + System.getProperty("line.separator")).getBytes());
			tttFile.write((Treatment.CSV_HEADER + System.getProperty("line.separator")).getBytes());
			sampleFile.write((BiologicalSample.CSV_HEADER + System.getProperty("line.separator")).getBytes());
			analysisFile.write((Analysis.CSV_HEADER + System.getProperty("line.separator")).getBytes());
			fusionFile.write((Fusion.CSV_HEADER + System.getProperty("line.separator")).getBytes());
			annotationFile.write((Annotation.CSV_HEADER + System.getProperty("line.separator")).getBytes());
			drugFile.write(
					("Patient_Id,Instance_Id,Treatment_Ref,Drug_Code,Drug_Name" + System.getProperty("line.separator"))
							.getBytes());

			int i = 1;
			for (Patient patient : patients) {
				if (i++ > 100) {
					break;
				}
				patientFile.write((patient.getCsvData() + System.getProperty("line.separator")).getBytes());
				for (TumorPathologyEvent tpe : patient.getTumorPathologyEvents()) {
					tpeFile.write((tpe.getCsvData() + System.getProperty("line.separator")).getBytes());
					if (tpe.getTnm() != null) {
						tnmFile.write((tpe.getTnm().getCsvData() + System.getProperty("line.separator")).getBytes());
					}
					for (Treatment ttt : tpe.getTreatments()) {
						tttFile.write((ttt.getCsvData() + System.getProperty("line.separator")).getBytes());
						for (Drug d : ttt.getDrugs()) {
							StringBuffer sb = new StringBuffer();
							try {
								sb.append((ttt.getTumorPathologyEvent().getPatient().getId() != null
										? ttt.getTumorPathologyEvent().getPatient().getId()
										: "") + ",");
							} catch (Exception e) {
								sb.append((ttt.getTumorPathologyEvent().getParent().getPatient().getId() != null
										? ttt.getTumorPathologyEvent().getParent().getPatient().getId()
										: "") + ",");
							}
							sb.append(drugCount++ + ",");
							sb.append(ttt.getId() + ",");
							sb.append(d.getValueMeaning() + ",");
							sb.append(d.getLabelValueMeaning());
							drugFile.write((sb.toString() + System.getProperty("line.separator")).getBytes());
						}
					}
					for (BiologicalSample sample : tpe.getBiologicalSamples()) {
						sampleFile.write((sample.getCsvData() + System.getProperty("line.separator")).getBytes());
						for (Analysis analysis : sample.getAnalysises()) {
							analysisFile
									.write((analysis.getCsvData() + System.getProperty("line.separator")).getBytes());
							for (Fusion fusion : analysis.getFusions()) {
								fusionFile
										.write((fusion.getCsvData() + System.getProperty("line.separator")).getBytes());
								for (Annotation annotation : fusion.getAnnotations()) {
									annotationFile
											.write((annotation.getCsvData() + System.getProperty("line.separator"))
													.getBytes());
								}
							}
						}
					}
					for (TumorPathologyEvent child : tpe.getChildren()) {
						tpeFile.write((child.getCsvData() + System.getProperty("line.separator")).getBytes());
						if (child.getTnm() != null) {
							tnmFile.write(
									(child.getTnm().getCsvData() + System.getProperty("line.separator")).getBytes());
						}
						for (Treatment ttt : child.getTreatments()) {
							tttFile.write((ttt.getCsvData() + System.getProperty("line.separator")).getBytes());
							for (Drug d : ttt.getDrugs()) {
								StringBuffer sb = new StringBuffer();
								try {
									sb.append((ttt.getTumorPathologyEvent().getPatient().getId() != null
											? ttt.getTumorPathologyEvent().getPatient().getId()
											: "") + ",");
								} catch (Exception e) {
									sb.append((ttt.getTumorPathologyEvent().getParent().getPatient().getId() != null
											? ttt.getTumorPathologyEvent().getParent().getPatient().getId()
											: "") + ",");
								}
								sb.append(drugCount++ + ",");
								sb.append(ttt.getId() + ",");
								sb.append(d.getValueMeaning() + ",");
								sb.append(d.getLabelValueMeaning());
								drugFile.write((sb.toString() + System.getProperty("line.separator")).getBytes());
							}
						}
						for (BiologicalSample sample : child.getBiologicalSamples()) {
							sampleFile.write((sample.getCsvData() + System.getProperty("line.separator")).getBytes());
							for (Analysis analysis : sample.getAnalysises()) {
								analysisFile.write(
										(analysis.getCsvData() + System.getProperty("line.separator")).getBytes());
								for (Fusion fusion : analysis.getFusions()) {
									fusionFile.write(
											(fusion.getCsvData() + System.getProperty("line.separator")).getBytes());
									for (Annotation annotation : fusion.getAnnotations()) {
										annotationFile
												.write((annotation.getCsvData() + System.getProperty("line.separator"))
														.getBytes());
									}
								}
							}
						}

					}
				}
			}
			LoggerFactory.getLogger(getClass().getName()).warn("Exporting patients done.");
			Notification.show(patients.size() + " patients were exported", 5000, Position.TOP_CENTER);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void anonymizePatient(Patient p) {
		int offset = new Random().nextInt(110 - 10) + 10;
		p.setOriginalId(patientCount++ + "");
		p.setId(Long.parseLong(p.getOriginalId()));
		p.setPatient_BirthDate(shiftDate(p.getPatient_BirthDate(), offset));
		p.setPatient_CauseOfDeath((CauseOfDeath) randomize(p.getPatient_CauseOfDeath()));
		p.setPatient_DeathDate(shiftDate(p.getPatient_BirthDate(), offset));
		p.setPatient_DeathDate(shiftDate(p.getPatient_DeathDate(), offset));
		p.setPatient_Gender((Gender) randomize(p.getPatient_Gender()));
		p.setPatient_LastNewsDate(shiftDate(p.getPatient_LastNewsDate(), offset));
		p.setPatient_LastNewsStatus((LastNewsStatus) randomize(p.getPatient_LastNewsStatus()));
		p.setPatient_OriginCenterId(randomize(p.getPatient_OriginCenterId()));
		p.setPatient_ProviderCenterId(randomize(p.getPatient_ProviderCenterId()));

		for (TumorPathologyEvent tpe : p.getTumorPathologyEvents()) {
			tpe.setTumorPathologyEvent_DiagnosisDate(shiftDate(p.getPatient_BirthDate(), offset));
			tpe.setTumorPathologyEvent_Laterality((Laterality) randomize(tpe.getTumorPathologyEvent_Laterality()));
			tpe.setTumorPathologyEvent_MorphologyCode(
					(Morphology) randomize(tpe.getTumorPathologyEvent_MorphologyCode()));
			tpe.setTumorPathologyEvent_StartDate(tpe.getTumorPathologyEvent_DiagnosisDate());
			tpe.setTumorPathologyEvent_TopographyCode(
					(Topography) randomize(tpe.getTumorPathologyEvent_TopographyCode()));

			for (TumorPathologyEvent child : tpe.getChildren()) {
				child.setTumorPathologyEvent_DiagnosisDate(shiftDate(p.getPatient_BirthDate(), offset));
				child.setTumorPathologyEvent_Laterality(
						(Laterality) randomize(tpe.getTumorPathologyEvent_Laterality()));
				child.setTumorPathologyEvent_MorphologyCode(
						(Morphology) randomize(tpe.getTumorPathologyEvent_MorphologyCode()));
				child.setTumorPathologyEvent_StartDate(tpe.getTumorPathologyEvent_DiagnosisDate());
				child.setTumorPathologyEvent_TopographyCode(
						(Topography) randomize(tpe.getTumorPathologyEvent_TopographyCode()));

				for (Treatment ttt : child.getTreatments()) {
					ttt.setTreatment_StartDate(shiftDate(ttt.getTreatment_StartDate(), offset));
					ttt.setTreatment_EndDate(shiftDate(ttt.getTreatment_EndDate(), offset));
					ttt.setTreatment_ClinicalTrialName(randomize(ttt.getTreatment_ClinicalTrialName()));
				}

				for (BiologicalSample sample : child.getBiologicalSamples()) {
					sample.setBiologicalSample_CollectDate(shiftDate(sample.getBiologicalSample_CollectDate(), offset));
					sample.setBiologicalSample_ExternalAccession(
							randomize(sample.getBiologicalSample_ExternalAccession()));
					sample.setBiologicalSample_ParentExternalAccession(
							randomize(sample.getBiologicalSample_ParentExternalAccession()));
				}
			}

			for (Treatment ttt : tpe.getTreatments()) {
				ttt.setTreatment_StartDate(shiftDate(ttt.getTreatment_StartDate(), offset));
				ttt.setTreatment_EndDate(shiftDate(ttt.getTreatment_EndDate(), offset));
				ttt.setTreatment_ClinicalTrialName(randomize(ttt.getTreatment_ClinicalTrialName()));
			}

			for (BiologicalSample sample : tpe.getBiologicalSamples()) {
				sample.setBiologicalSample_CollectDate(shiftDate(sample.getBiologicalSample_CollectDate(), offset));
				sample.setBiologicalSample_ExternalAccession(randomize(sample.getBiologicalSample_ExternalAccession()));
				sample.setBiologicalSample_ParentExternalAccession(
						randomize(sample.getBiologicalSample_ParentExternalAccession()));
			}
		}
	}

	private Date shiftDate(Date d, int offset) {
		if (d == null) {
			return null;
		}
		return Date.from(d.toInstant().plusSeconds(-offset * 60 * 60 * 24));
	}

	private String randomize(String s) {
		if (s == null) {
			return null;
		}
		return RandomStringUtils.random(10, true, true);
	}

	private AbstractConceptualDomain randomize(AbstractConceptualDomain acd) {
		if (acd == null) {
			return null;
		}
		return cdService.getRandomValue(acd.getClass());
	}

}
