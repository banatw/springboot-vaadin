package com.example.application.views.pegawai.form;

import java.util.Optional;

import com.example.application.entity.Customer;
import com.example.application.repo.CustomerRepo;
import com.example.application.views.Action;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

public class PegawaiForm extends Dialog {

    private VerticalLayout vLayout = new VerticalLayout();
    private HorizontalLayout hLayout = new HorizontalLayout();
    private Customer customer;
    private TextField firstName = new TextField("First Name");
    private TextField lastName = new TextField("Last Name");

    public PegawaiForm(CustomerRepo customerRepo, Action act, Optional<Long> customerId, Grid<Customer> customerGrid) {
        Binder<Customer> binder = new Binder<>(Customer.class);

        binder.forField(firstName).asRequired("masukan nama depan").bind(Customer::getFirstName,
                Customer::setFirstName);
        binder.forField(lastName).asRequired("masukkan nama belakang").bind(Customer::getLastName,
                Customer::setLastName);
        // vbBinder.
        if (act.equals(Action.EDIT)) {
            customer = customerRepo.findById(customerId.get()).get();
            binder.readBean(customer);
        }

        Button btnSimpan = new Button("Simpan", e -> {
            if (binder.validate().isOk()) {
                if (act.equals(Action.EDIT)) {
                    try {
                        binder.writeBean(customer);
                    } catch (ValidationException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    customerRepo.save(customer);
                } else {
                    Customer newCustomer = new Customer();
                    try {
                        binder.writeBean(newCustomer);
                    } catch (ValidationException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    customerRepo.save(newCustomer);
                }
                customerGrid.getDataProvider().refreshAll();
                customerGrid.scrollToStart();
                close();
            }

        });
        Button btnCancel = new Button("Batal", e -> {
            close();
        });
        hLayout.add(btnSimpan, btnCancel);
        vLayout.add(firstName, lastName, hLayout);
        add(vLayout);
    }
}
