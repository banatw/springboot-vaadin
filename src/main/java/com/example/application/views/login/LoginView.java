package com.example.application.views.login;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.application.entity.User;
import com.example.application.entity.Menu;
import com.example.application.repo.UserRepo;
import com.example.application.views.main.MainView;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.UIScope;

import org.springframework.beans.factory.annotation.Autowired;

import at.favre.lib.crypto.bcrypt.BCrypt;

@Route("login")
@UIScope
public class LoginView extends LoginOverlay {
    @Autowired
    private UserRepo userRepo;

    public LoginView() {
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
                    VaadinSession.getCurrent().setAttribute("user", user.getUsername());

                    user.getRoles().stream().forEachOrdered(role -> {
                        List<Menu> mList = role.getMenus().stream().collect(Collectors.toList());
                        VaadinSession.getCurrent().setAttribute("menus", mList);
                    });
                    getUI().get().navigate(MainView.class);
                } else {
                    setError(true);
                }
            }

        });
    }

}
