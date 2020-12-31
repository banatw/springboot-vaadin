package com.example.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.application.entity.Customer;
import com.example.application.entity.Matakuliah;
import com.example.application.entity.Menu;
import com.example.application.entity.Nilai;
import com.example.application.entity.Role;
import com.example.application.entity.User;
import com.example.application.repo.CustomerRepo;
import com.example.application.repo.MatakuliahRepo;
import com.example.application.repo.MenuRepo;
import com.example.application.repo.NilaiRepo;
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
import org.vaadin.artur.helpers.LaunchUtil;

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
    @Autowired
    private MatakuliahRepo matakuliahRepo;
    @Autowired
    private NilaiRepo nilaiRepo;

    public static void main(String[] args) {
        LaunchUtil.launchBrowserInDevelopmentMode(SpringApplication.run(Application.class, args));
    }

    private String generatePassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();

        matakuliahRepo.save(new Matakuliah(1, "Matematika"));
        matakuliahRepo.save(new Matakuliah(2, "Bahasa Inggris"));
        matakuliahRepo.save(new Matakuliah(3, "Fisika"));
        matakuliahRepo.save(new Matakuliah(4, "Biologi"));

        for (int i = 0; i < 100; i++) {
            List<Nilai> nilais = new ArrayList<>();
            Nilai nilai1 = new Nilai(matakuliahRepo.findById(1).get(), 0d);
            Nilai nilai2 = new Nilai(matakuliahRepo.findById(2).get(), 0d);
            Nilai nilai3 = new Nilai(matakuliahRepo.findById(3).get(), 0d);
            Nilai nilai4 = new Nilai(matakuliahRepo.findById(4).get(), 0d);
            nilaiRepo.save(nilai1);
            nilaiRepo.save(nilai2);
            nilaiRepo.save(nilai3);
            nilaiRepo.save(nilai4);
            nilais.add(nilai1);
            nilais.add(nilai2);
            nilais.add(nilai3);
            nilais.add(nilai4);
            customerRepo.save(new Customer(faker.name().firstName(), faker.name().lastName(), nilais));
        }

        menuRepo.save(new Menu("Pegawai", "pegawai", true));
        menuRepo.save(new Menu("About", "about", true));
        menuRepo.save(new Menu("Hello", "hello", true));
        menuRepo.save(new Menu("Admin", "admin", true));
        menuRepo.save(new Menu("User", "user", true));
        // menuRepo.save(new Menu("Pegawai Form", "pegawai/form", false));

        roleRepo.save(new Role("ROLE_ADMIN", menuRepo.findAll()));

        List<Menu> menuUser = new ArrayList<Menu>();
        menuUser.add(menuRepo.findByPath("user"));
        menuUser.add(menuRepo.findByPath("about"));

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
                    allowedMenus.add(menu.getPath());
                });
            });
            return allowedMenus;
        }

        @Override
        public void serviceInit(ServiceInitEvent serviceInitEvent) {
            serviceInitEvent.getSource().addUIInitListener(uiInitEvent -> {
                uiInitEvent.getUI().addBeforeEnterListener(beforeEnterEvent -> {
                    String currPage = beforeEnterEvent.getLocation().getFirstSegment();
                    // System.out.println(currPage);
                    if (VaadinSession.getCurrent().getAttribute("user") != null) {
                        User user = (User) VaadinSession.getCurrent().getAttribute("user");
                        if (!currPage.contains("login") && !currPage.contains("mainview")) {
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
