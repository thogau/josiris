package net.thogau.josiris.views;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import com.opencsv.bean.CsvToBeanBuilder;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import net.thogau.josiris.data.csv.CsvDrug;
import net.thogau.josiris.data.csv.strategy.AnalysisMappingStrategy;
import net.thogau.josiris.data.csv.strategy.AnnotationMappingStrategy;
import net.thogau.josiris.data.csv.strategy.BiologicalSampleMappingStrategy;
import net.thogau.josiris.data.csv.strategy.DrugMappingStrategy;
import net.thogau.josiris.data.csv.strategy.FusionMappingStrategy;
import net.thogau.josiris.data.csv.strategy.PatientMappingStrategy;
import net.thogau.josiris.data.csv.strategy.TnmMappingStrategy;
import net.thogau.josiris.data.csv.strategy.TreatmentMappingStrategy;
import net.thogau.josiris.data.csv.strategy.TumorPathologyEventMappingStrategy;
import net.thogau.josiris.data.entity.Analysis;
import net.thogau.josiris.data.entity.Annotation;
import net.thogau.josiris.data.entity.BiologicalSample;
import net.thogau.josiris.data.entity.Fusion;
import net.thogau.josiris.data.entity.Patient;
import net.thogau.josiris.data.entity.Tnm;
import net.thogau.josiris.data.entity.Treatment;
import net.thogau.josiris.data.entity.TumorPathologyEvent;
import net.thogau.josiris.data.service.PatientService;
import net.thogau.josiris.views.patient.PatientListView;

@Route(value = "", layout = MainLayout.class)
@AnonymousAllowed
public class HomeView extends VerticalLayout {

	PatientService service;
	AutowireCapableBeanFactory beanFactory;
	MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
	Upload upload = new Upload(buffer);
	Map<String, InputStream> streams = new HashMap<>();

	public final static String[] supportedFiles = new String[] { "OSIRIS_pivot_Patient.csv",
			"OSIRIS_pivot_TumorPathologyEvent.csv", "OSIRIS_pivot_TNM.csv", "OSIRIS_pivot_Treatment.csv",
			"OSIRIS_pivot_Drug.csv", "OSIRIS_pivot_BiologicalSample.csv", "OSIRIS_pivot_Analysis.csv",
			"OSIRIS_pivot_Fusion.csv", "OSIRIS_pivot_Annotation.csv" };

	public HomeView(PatientService service, AutowireCapableBeanFactory beanFactory) {
		this.service = service;
		this.beanFactory = beanFactory;

		if (service.count() == 0) {
			upload.addAllFinishedListener(event -> {
				LoggerFactory.getLogger(HomeView.class.getName()).warn("Processing pivot files...");
				try {
					populateDatabase();
					LoggerFactory.getLogger(HomeView.class.getName()).warn("Processing pivot files done.");
				} catch (Exception e) {
					Notification.show("Import failed", 5000, Position.TOP_CENTER).addThemeName("error");
				}
			});

			upload.addStartedListener(event -> {
				if (!Arrays.asList(supportedFiles).contains(event.getFileName())) {
					Notification.show("File " + event.getFileName() + " in not supported", 5000, Position.TOP_CENTER);
				}
			});

			upload.setAcceptedFileTypes("text/csv", ".csv");
			upload.setUploadButton(new Button("Upload Osiris pivot files..."));
			add(upload);
		} else {
			UI.getCurrent().getPage().setLocation("patients");
		}

	}

	private void populateDatabase() {
		try {
			// instance Ids
			Map<String, Patient> patientIds = new HashMap<>();
			Map<String, TumorPathologyEvent> tpeIds = new HashMap<>();
			Map<String, Treatment> tttIds = new HashMap<>();
			Map<String, BiologicalSample> bsIds = new HashMap<>();
			Map<String, Analysis> anaIds = new HashMap<>();
			Map<String, Fusion> fusionIds = new HashMap<>();

			// spring aware MappingStrategy
			PatientMappingStrategy patientStrategy = new PatientMappingStrategy(this.beanFactory);
			patientStrategy.setType(Patient.class);
			TumorPathologyEventMappingStrategy tpeStrategy = new TumorPathologyEventMappingStrategy(this.beanFactory);
			tpeStrategy.setType(TumorPathologyEvent.class);
			TnmMappingStrategy tnmStrategy = new TnmMappingStrategy(this.beanFactory);
			tnmStrategy.setType(Tnm.class);
			TreatmentMappingStrategy treatmentStrategy = new TreatmentMappingStrategy(this.beanFactory);
			treatmentStrategy.setType(Treatment.class);
			DrugMappingStrategy drugStrategy = new DrugMappingStrategy(this.beanFactory);
			drugStrategy.setType(CsvDrug.class);
			BiologicalSampleMappingStrategy bsStrategy = new BiologicalSampleMappingStrategy(this.beanFactory);
			bsStrategy.setType(BiologicalSample.class);
			AnalysisMappingStrategy anaStrategy = new AnalysisMappingStrategy(this.beanFactory);
			anaStrategy.setType(Analysis.class);
			FusionMappingStrategy fusionStrategy = new FusionMappingStrategy(this.beanFactory);
			fusionStrategy.setType(Fusion.class);
			AnnotationMappingStrategy annotationStrategy = new AnnotationMappingStrategy(this.beanFactory);
			annotationStrategy.setType(Annotation.class);

			// Parse pivot files
			List<Patient> patients = new ArrayList<>();
			List<TumorPathologyEvent> tpes = new ArrayList<>();
			List<Tnm> tnms = new ArrayList<>();
			List<Treatment> ttts = new ArrayList<>();
			List<CsvDrug> drugs = new ArrayList<>();
			List<BiologicalSample> biologicalSamples = new ArrayList<>();
			List<Analysis> analysises = new ArrayList<>();
			List<Fusion> fusions = new ArrayList<>();
			;
			List<Annotation> annotations = new ArrayList<>();
			try {
				patients = new CsvToBeanBuilder<Patient>(
						new InputStreamReader(buffer.getInputStream("OSIRIS_pivot_Patient.csv")))
						.withType(Patient.class).withMappingStrategy(patientStrategy).build().parse();
			} catch (Exception e) {
				Notification
						.show("Error parsing OSIRIS_pivot_Patient.csv : " + e.getMessage(), 5000, Position.TOP_CENTER)
						.addThemeName("error");
				throw new RuntimeException();
			}
			try {
				tpes = new CsvToBeanBuilder<TumorPathologyEvent>(
						new InputStreamReader(buffer.getInputStream("OSIRIS_pivot_TumorPathologyEvent.csv")))
						.withType(TumorPathologyEvent.class).withMappingStrategy(tpeStrategy).build().parse();
			} catch (Exception e) {
				Notification.show("Error parsing OSIRIS_pivot_TumorPathologyEvent.csv : " + e.getMessage(), 5000,
						Position.TOP_CENTER).addThemeName("error");
				throw new RuntimeException();
			}
			try {
				tnms = new CsvToBeanBuilder<Tnm>(new InputStreamReader(buffer.getInputStream("OSIRIS_pivot_TNM.csv")))
						.withType(Tnm.class).withMappingStrategy(tnmStrategy).build().parse();
			} catch (Exception e) {
				Notification.show("Error parsing OSIRIS_pivot_TNM.csv : " + e.getMessage(), 5000, Position.TOP_CENTER)
						.addThemeName("error");
				throw new RuntimeException();
			}
			try {
				ttts = new CsvToBeanBuilder<Treatment>(
						new InputStreamReader(buffer.getInputStream("OSIRIS_pivot_Treatment.csv")))
						.withType(Treatment.class).withMappingStrategy(treatmentStrategy).build().parse();
			} catch (Exception e) {
				Notification
						.show("Error parsing OSIRIS_pivot_Treatment.csv : " + e.getMessage(), 5000, Position.TOP_CENTER)
						.addThemeName("error");
				throw new RuntimeException();
			}
			try {
				drugs = new CsvToBeanBuilder<CsvDrug>(
						new InputStreamReader(buffer.getInputStream("OSIRIS_pivot_Drug.csv"))).withType(CsvDrug.class)
						.withMappingStrategy(drugStrategy).build().parse();
			} catch (Exception e) {
				Notification.show("Error parsing OSIRIS_pivot_Drug.csv : " + e.getMessage(), 5000, Position.TOP_CENTER)
						.addThemeName("error");
				throw new RuntimeException();
			}
			try {
				biologicalSamples = new CsvToBeanBuilder<BiologicalSample>(
						new InputStreamReader(buffer.getInputStream("OSIRIS_pivot_BiologicalSample.csv")))
						.withType(BiologicalSample.class).withMappingStrategy(bsStrategy).build().parse();
			} catch (Exception e) {
				Notification.show("Error parsing OSIRIS_pivot_BiologicalSample.csv : " + e.getMessage(), 5000,
						Position.TOP_CENTER).addThemeName("error");
				throw new RuntimeException();
			}
			try {
				analysises = new CsvToBeanBuilder<Analysis>(
						new InputStreamReader(buffer.getInputStream("OSIRIS_pivot_Analysis.csv")))
						.withType(Analysis.class).withMappingStrategy(anaStrategy).build().parse();
			} catch (Exception e) {
				Notification
						.show("Error parsing OSIRIS_pivot_Analysis.csv : " + e.getMessage(), 5000, Position.TOP_CENTER)
						.addThemeName("error");
				throw new RuntimeException();
			}
			try {
				fusions = new CsvToBeanBuilder<Fusion>(
						new InputStreamReader(buffer.getInputStream("OSIRIS_pivot_Fusion.csv"))).withType(Fusion.class)
						.withMappingStrategy(fusionStrategy).build().parse();
			} catch (Exception e) {
				Notification
						.show("Error parsing OSIRIS_pivot_Fusion.csv : " + e.getMessage(), 5000, Position.TOP_CENTER)
						.addThemeName("error");
				throw new RuntimeException();
			}
			try {
				annotations = new CsvToBeanBuilder<Annotation>(
						new InputStreamReader(buffer.getInputStream("OSIRIS_pivot_Annotation.csv")))
						.withType(Annotation.class).withMappingStrategy(annotationStrategy).build().parse();
			} catch (Exception e) {
				Notification.show("Error parsing OSIRIS_pivot_Annotation.csv : " + e.getMessage(), 5000,
						Position.TOP_CENTER).addThemeName("error");
				throw new RuntimeException();
			}

			// build patients
			for (Patient patient : patients) {
				patientIds.put(patient.getPatient_Id(), patient);
			}

			for (TumorPathologyEvent tpe : tpes) {
				if (tpe.getTumorPathologyEvent_ParentRef().trim().isEmpty()) {
					// primary tymour
					Patient patient = patientIds.get(tpe.getPatient_Id());
					patient.getTumorPathologyEvents().add(tpe);
					tpe.setPatient(patient);
					tpeIds.put(tpe.getInstance_Id(), tpe);
				} else {
					// metastasis or recurrence
					TumorPathologyEvent parent = tpeIds.get(tpe.getTumorPathologyEvent_ParentRef());
					parent.getChildren().add(tpe);
					tpe.setParent(parent);
					tpeIds.put(tpe.getInstance_Id(), tpe);
				}
			}

			for (Tnm tnm : tnms) {
				TumorPathologyEvent parent = tpeIds.get(tnm.getTumorPathologyEvent_Ref());
				parent.setTnm(tnm);
				tnm.setTumorPathologyEvent(parent);
			}

			for (Treatment ttt : ttts) {
				TumorPathologyEvent parent = tpeIds.get(ttt.getTumorPathologyEvent_Ref());
				parent.getTreatments().add(ttt);
				ttt.setTumorPathologyEvent(parent);
				tttIds.put(ttt.getInstance_Id(), ttt);
			}

			for (CsvDrug csvDrug : drugs) {
				Treatment parent = tttIds.get(csvDrug.getTreatment_Ref());
				parent.getDrugs().add(csvDrug.getDrug_Code());
			}

			for (BiologicalSample bs : biologicalSamples) {
				TumorPathologyEvent parent = tpeIds.get(bs.getTumorPathologyEvent_Ref());
				parent.getBiologicalSamples().add(bs);
				bs.setTumorPathologyEvent(parent);
				bsIds.put(bs.getInstance_Id(), bs);
			}

			for (Analysis ana : analysises) {
				BiologicalSample parent = bsIds.get(ana.getBiologicalSample_Ref());
				parent.getAnalysises().add(ana);
				ana.setBiologicalSample(parent);
				anaIds.put(ana.getInstance_Id(), ana);
			}

			for (Fusion fusion : fusions) {
				Analysis parent = anaIds.get(fusion.getAnalysis_Ref());
				parent.getFusions().add(fusion);
				fusion.setAnalysis(parent);
				fusionIds.put(fusion.getInstance_Id(), fusion);
			}

			for (Annotation ann : annotations) {
				Fusion parent = fusionIds.get(ann.getAlteration_Ref());
				parent.getAnnotations().add(ann);
				ann.setFusion(parent);
			}

			// save patients
			for (Patient patient : patientIds.values()) {
				try {
					service.save(patient);
				} catch (Exception e) {
					Notification.show("Error importing patient " + patient.getPatient_Id() + " : " + e.getMessage(),
							5000, Position.TOP_CENTER).addThemeName("error");
					throw new RuntimeException();
				}
			}
			Notification.show(patientIds.values().size() + " patients were imported", 5000, Position.TOP_CENTER);
			UI.getCurrent().navigate(PatientListView.class);

		} catch (IllegalStateException e) {
			Notification.show("Unexpected error : " + e.getMessage(), 5000, Position.TOP_CENTER).addThemeName("error");
			throw new RuntimeException();
		}
	}

}
