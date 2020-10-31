package com.example.application.views.pegawai.form;

import com.example.application.entity.Customer;
import com.example.application.repo.CustomerRepo;
import com.example.application.views.Action;
import com.example.application.views.pegawai.Pegawai;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class PegawaiForm extends Dialog {
    Customer customer;

    public PegawaiForm(CustomerRepo customerRepo, Action act, Long customerId, Grid<Customer> customerGrid) {
        FormLayout formLayout = new FormLayout();

        TextField tFieldFirstName = new TextField("First Name");
        TextField tFieldLastName = new TextField("Last Name");
        if (act.equals(Action.EDIT)) {
            customer = customerRepo.findById(customerId).get();
            tFieldFirstName.setValue(customer.getFirstName());
            tFieldLastName.setValue(customer.getLastName());
        }

        formLayout.add(tFieldFirstName, tFieldLastName);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Button btnSimpan = new Button("Simpan", e -> {
            if (act.equals(Action.EDIT)) {
                // customer = customerRepo.findById(idCustomer).get();
                customer.setFirstName(tFieldFirstName.getValue());
                customer.setLastName(tFieldLastName.getValue());
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
            customerGrid.getDataProvider().refreshAll();
            customerGrid.scrollToStart();
            close();
        });
        horizontalLayout.add(btnSimpan, btnCancel);
        add(formLayout, horizontalLayout);
    }
}
