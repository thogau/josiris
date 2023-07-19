package net.thogau.josiris.views;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import net.thogau.josiris.data.service.PatientService;

@Route(value = "", layout = MainLayout.class)
@AnonymousAllowed
public class HomeView extends VerticalLayout {

	public HomeView(PatientService service, AutowireCapableBeanFactory beanFactory) {
		add(new H2("Welcome to Josiris"));
		add(new H4("With this tools you can :"));
		add(new UnorderedList(new ListItem("Import OSIRIS pivot files into a relational database"),
				new ListItem("Browse the imported patients"),
				new ListItem("Export the patients into OSIRIS pivot files with anonymizing support")));
	}

}
