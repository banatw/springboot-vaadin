package com.example.application.views.admin;

import com.example.application.views.main.MainView;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "admin", layout = MainView.class)
@PageTitle("Admin")
@CssImport("./styles/views/admin/admin-view.css")
public class AdminPage extends Div {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public AdminPage() {
        setId("admin-view");
        add(new Label("Halaman Admin"));
        setSizeFull();
    }
}
