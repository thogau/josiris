package net.thogau.josiris.views.patient;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import net.thogau.josiris.data.entity.Patient;
import net.thogau.josiris.data.service.PatientService;
import net.thogau.josiris.views.MainLayout;

@Route(value = "patient", layout = MainLayout.class)
@PageTitle("Patient")
@AnonymousAllowed
public class PatientView extends VerticalLayout implements HasUrlParameter<String> {

	Binder<Patient> binder = new BeanValidationBinder<>(Patient.class);
	PatientService service;

	public PatientView(PatientService service) {
		this.service = service;
		binder.bindInstanceFields(this);
	}

	@Override
	public void setParameter(BeforeEvent event, @OptionalParameter String id) {
		try {
			Patient p = service.get(Long.parseLong(id));
			setPatient(p);
		} catch (Exception e) {
			setPatient(new Patient());
		}

	}

	public void setPatient(Patient p) {
		if (p != null) {
			binder.setBean(p);
		}
	}
}