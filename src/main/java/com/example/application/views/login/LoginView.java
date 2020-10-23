package com.example.application.views.login;

import com.example.application.views.main.MainView;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route("login")
public class LoginView extends LoginOverlay {

    public LoginView() {
        // addAttachListener(event -> {
        // if (event.isInitialAttach()) {

        // }
        // });
        setOpened(true);
        addLoginListener(loginEvent -> {
            if (loginEvent.isFromClient()) {
                VaadinSession.getCurrent().setAttribute("user", "bana");
                getUI().get().navigate(MainView.class);
            }

        });
    }

}
