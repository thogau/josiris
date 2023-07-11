package net.thogau.josiris.views.patient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import net.thogau.josiris.data.entity.Patient;
import net.thogau.josiris.data.entity.TumorPathologyEvent;
import net.thogau.josiris.data.entity.conceptualDomain.Morphology;
import net.thogau.josiris.data.entity.conceptualDomain.Topography;
import net.thogau.josiris.data.service.PatientService;
import net.thogau.josiris.views.MainLayout;

@Route(value = "patients", layout = MainLayout.class)
@PageTitle("Patients")
@AnonymousAllowed
public class PatientListView extends VerticalLayout {

	PatientService service;
	Grid<Patient> grid = new Grid<>(Patient.class);

	public PatientListView(PatientService service) {
		this.service = service;
		setSizeFull();
		configureGrid();
		add(grid);
		grid.setItems(query -> {
			var vaadinSortOrders = query.getSortOrders();
			var springSortOrders = new ArrayList<Sort.Order>();
			for (QuerySortOrder so : vaadinSortOrders) {
				String colKey = so.getSorted();
				if (so.getDirection() == SortDirection.ASCENDING) {
					springSortOrders.add(Sort.Order.asc(colKey));
				} else {
					springSortOrders.add(Sort.Order.desc(colKey));
				}
			}
			return service.paginate(PageRequest.of(query.getOffset(), query.getLimit(), Sort.by(springSortOrders)));
		});
		add(new Text("Total : " + service.count() + " Patients"));
	}

	private void configureGrid() {
		grid.setSizeFull();
		grid.removeAllColumns();

		grid.addColumn("originalId").setHeader("Original ID");

		grid.addColumn("id").setHeader("Josiris ID");

		Column<Patient> c = grid.addColumn(p -> p.getPatient_Gender().getLabelValueMeaning()).setHeader("Gender");

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		grid.addColumn(p -> sdf.format(p.getPatient_BirthDate())).setHeader("Birth date");

		c = grid.addColumn(p -> p.getTumorPathologyEvents().stream()
				.map(TumorPathologyEvent::getTumorPathologyEvent_MorphologyCode).collect(Collectors.toList()).stream()
				.map(Morphology::getLabelValueMeaning).collect(Collectors.joining(", ", "", "")))
				.setHeader("Morphology");
		c.setFlexGrow(3);

		c = grid.addColumn(p -> p.getTumorPathologyEvents().stream()
				.map(TumorPathologyEvent::getTumorPathologyEvent_TopographyCode).collect(Collectors.toList()).stream()
				.map(Topography::getLabelValueMeaning).collect(Collectors.joining(", ", "", "")))
				.setHeader("Topography");
		c.setFlexGrow(3);

		grid.getColumns().forEach(col -> col.setAutoWidth(true));
		grid.asSingleSelect().addValueChangeListener(event -> showPatient(event.getValue()));
	}

	public void showPatient(Patient p) {
		UI.getCurrent().navigate(PatientView.class, p.getId().toString());
	}

}