package net.thogau.josiris.views.utils;

import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;

public class KeepMenuBarOpenDecorator {

	public KeepMenuBarOpenDecorator(MenuItem menuItem) {
		menuItem.getElement().addEventListener("click", e -> {
		}).addEventData("event.preventDefault()");
	}

	public static void keepOpenOnClick(MenuItem menuItem) {
		new KeepMenuBarOpenDecorator(menuItem);
	}

	public static void keepOpenOnClick(SubMenu subMenu) {
		subMenu.getItems().forEach(KeepMenuBarOpenDecorator::keepOpenOnClick);
	}
}
