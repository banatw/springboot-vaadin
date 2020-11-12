package com.example.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.example.application.entity.Customer;
import com.example.application.entity.Menu;
import com.example.application.entity.Role;
import com.example.application.entity.User;
import com.example.application.repo.CustomerRepo;
import com.example.application.repo.MenuRepo;
import com.example.application.repo.RoleRepo;
import com.example.application.repo.UserRepo;
import com.example.application.views.login.LoginView;
import com.github.javafaker.Faker;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.VaadinSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.stereotype.Service;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * The entry point of the Spring Boot application.
 */
@SpringBootApplication
public class Application extends SpringBootServletInitializer implements CommandLineRunner {
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private MenuRepo menuRepo;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    private String generatePassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();

        for (int i = 0; i < 300; i++) {
            customerRepo.save(new Customer(faker.name().firstName(), faker.name().lastName()));
        }

        menuRepo.save(new Menu("Pegawai"));
        menuRepo.save(new Menu("AboutView"));
        menuRepo.save(new Menu("HelloWorldView"));
        menuRepo.save(new Menu("AdminPage"));
        menuRepo.save(new Menu("UserPage"));

        roleRepo.save(new Role("ROLE_ADMIN", menuRepo.findAll()));

        List<Menu> menuUser = new ArrayList<Menu>();
        menuUser.add(menuRepo.findByMenuName("UserPage"));
        menuUser.add(menuRepo.findByMenuName("AboutView"));

        roleRepo.save(new Role("ROLE_USER", menuUser));

        List<Role> roles = new ArrayList<Role>();
        roles.add(roleRepo.findByRoleName("ROLE_ADMIN"));
        roles.add(roleRepo.findByRoleName("ROLE_USER"));
        // String password = "admin";
        // String bcryptHashString = BCrypt.withDefaults().hashToString(12,
        // password.toCharArray());
        User user = new User("admin", generatePassword("admin"));
        user.setRoles(new ArrayList<>(
                Arrays.asList(roleRepo.findByRoleName("ROLE_ADMIN"), roleRepo.findByRoleName("ROLE_USER"))));
        userRepo.save(user);
        user = new User("user", generatePassword("user"));
        user.setRoles(new ArrayList<>(Arrays.asList(roleRepo.findByRoleName("ROLE_USER"))));
        userRepo.save(user);
    }

    @Service
    class MyServiceInitListener implements VaadinServiceInitListener {

        /**
         *
         */
        private static final long serialVersionUID = 1L;

        List<String> getMenus(User user) {
            List<String> allowedMenus = new ArrayList<String>();
            user.getRoles().stream().forEach(role -> {
                role.getMenus().stream().forEach(menu -> {
                    allowedMenus.add(menu.getMenuName());
                });
            });
            return allowedMenus;
        }

        @Override
        public void serviceInit(ServiceInitEvent serviceInitEvent) {
            serviceInitEvent.getSource().addUIInitListener(uiInitEvent -> {
                uiInitEvent.getUI().addBeforeEnterListener(beforeEnterEvent -> {
                    String currPage = beforeEnterEvent.getNavigationTarget().getSimpleName();
                    if (VaadinSession.getCurrent().getAttribute("user") != null) {
                        User user = (User) VaadinSession.getCurrent().getAttribute("user");
                        if (!currPage.equalsIgnoreCase("LoginView") && !currPage.equalsIgnoreCase("MainView")) {
                            if (!getMenus(user).contains(currPage))
                                beforeEnterEvent.forwardTo(LoginView.class);
                        }
                    } else {
                        beforeEnterEvent.forwardTo(LoginView.class);
                    }

                });
            });
        }
    }
}
