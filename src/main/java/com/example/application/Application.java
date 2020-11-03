package com.example.application;

import com.example.application.entity.Customer;
import com.example.application.entity.User;
import com.example.application.repo.CustomerRepo;
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

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();

        for (int i = 0; i < 300; i++) {
            customerRepo.save(new Customer(faker.name().firstName(), faker.name().lastName()));
        }
        String password = "admin";
        String bcryptHashString = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        userRepo.save(new User("admin", bcryptHashString));
    }

    @Service
    class MyServiceInitListener implements VaadinServiceInitListener {

        @Override
        public void serviceInit(ServiceInitEvent serviceInitEvent) {
            serviceInitEvent.getSource().addUIInitListener(uiInitEvent -> {
                uiInitEvent.getUI().addBeforeEnterListener(beforeEnterEvent -> {
                    if (VaadinSession.getCurrent().getAttribute("user") == null) {
                        beforeEnterEvent.forwardTo(LoginView.class);
                    }
                });
            });
        }
    }
}
