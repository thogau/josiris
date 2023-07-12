package net.thogau.josiris.views.patient;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.AbstractBackEndHierarchicalDataProvider;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalQuery;
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

	Patient patient;
	PatientService service;
	TreeItem root;
	Button close = new Button("Close");

	public PatientView(PatientService service) {
		this.service = service;
	}

	@Override
	public void setParameter(BeforeEvent event, @OptionalParameter String id) {
		patient = service.get(Long.parseLong(id));
		root = patient.buildTree();

		setHeightFull();

		var treeGrid = new TreeGrid<TreeItem>();
		treeGrid.setItems(Arrays.asList(root), this::getChildren);
		treeGrid.addHierarchyColumn(item -> item.getLabel());
		treeGrid.addColumn(TreeItem::getValue);

		var dataProvider = new AbstractBackEndHierarchicalDataProvider<TreeItem, Void>() {

			@Override
			public int getChildCount(HierarchicalQuery<TreeItem, Void> query) {
				if (query.getParent() == null) {
					return root.getChildren().size();
				} else {
					return query.getParent().getChildren().size();
				}
			}

			@Override
			public boolean hasChildren(TreeItem item) {
				return item.getChildren().size() > 0;
			}

			@Override
			protected Stream<TreeItem> fetchChildrenFromBackEnd(HierarchicalQuery<TreeItem, Void> query) {
				if (query.getParent() == null) {
					return root.getChildren().stream();
				} else {
					return query.getParent().getChildren().stream();
				}
			}
		};

		treeGrid.setDataProvider(dataProvider);

		treeGrid.setHeightFull();

		add(treeGrid);
	}

	private List<TreeItem> getChildren(TreeItem item) {
		return item.getChildren();
	}

}