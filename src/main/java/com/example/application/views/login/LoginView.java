package com.example.application.views.login;

import java.util.Optional;

import com.example.application.entity.User;
import com.example.application.repo.UserRepo;
import com.example.application.views.main.MainView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import org.springframework.beans.factory.annotation.Autowired;

import at.favre.lib.crypto.bcrypt.BCrypt;

@Route("login")
public class LoginView extends LoginOverlay {
    @Autowired
    private UserRepo userRepo;

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        // TODO Auto-generated method stub
        if (attachEvent.isInitialAttach()) {
            setDescription("admin : admin untuk login");
            setOpened(true);
            addLoginListener(loginEvent -> {
                Optional<User> optionalUser = userRepo.findById(loginEvent.getUsername());
                if (optionalUser.isEmpty()) {
                    setError(true);
                } else {
                    User user = optionalUser.get();
                    BCrypt.Result result = BCrypt.verifyer().verify(loginEvent.getPassword().toCharArray(),
                            user.getPassword());
                    if (result.verified) {
                        VaadinSession.getCurrent().setAttribute("user", user);
                        getUI().get().navigate(MainView.class);
                    } else {
                        setError(true);
                    }
                }

            });
        }
    }

}
