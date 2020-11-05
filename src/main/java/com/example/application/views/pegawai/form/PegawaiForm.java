package com.example.application.views.pegawai.form;

import java.util.Optional;

import com.example.application.entity.Customer;
import com.example.application.repo.CustomerRepo;
import com.example.application.views.Action;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

public class PegawaiForm extends Dialog {
    Customer customer;

    public PegawaiForm(CustomerRepo customerRepo, Action act, Optional<Long> customerId, Grid<Customer> customerGrid) {
        FormLayout formLayout = new FormLayout();
        Binder<Customer> binder = new Binder<>(Customer.class);
        TextField tFieldFirstName = new TextField("First Name");
        TextField tFieldLastName = new TextField("Last Name");
        if (!customerId.isEmpty())
            customer = customerRepo.findById(customerId.get()).get();
        binder.bind(tFieldFirstName, Customer::getFirstName, Customer::setFirstName);
        binder.bind(tFieldLastName, Customer::getLastName, Customer::setLastName);

        binder.readBean(customer);

        formLayout.add(tFieldFirstName, tFieldLastName);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Button btnSimpan = new Button("Simpan", e -> {
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
                newCustomer.setFirstName(tFieldFirstName.getValue());
                newCustomer.setLastName(tFieldLastName.getValue());
                customerRepo.save(newCustomer);
            }
            customerGrid.getDataProvider().refreshAll();
            customerGrid.scrollToStart();
            close();
        });
        Button btnCancel = new Button("Batal", e -> {
            close();
        });
        horizontalLayout.add(btnSimpan, btnCancel);
        add(formLayout, horizontalLayout);
    }
}
