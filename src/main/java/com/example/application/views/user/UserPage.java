package com.example.application.views.user;

import com.example.application.views.main.MainView;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "user", layout = MainView.class)
@PageTitle("User")
@CssImport("./styles/views/user/user-view.css")
public class UserPage extends Div {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public UserPage() {
        setId("user-view");
        add(new Label("Halaman User"));
        setSizeFull();
    }
}
