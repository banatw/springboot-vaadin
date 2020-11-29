package com.example.application.views.pegawai.form;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.application.entity.Customer;
import com.example.application.entity.Nilai;
import com.example.application.repo.CustomerRepo;
import com.example.application.repo.NilaiRepo;
import com.example.application.views.Action;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.renderer.ComponentRenderer;

public class PegawaiForm extends Dialog {

    private VerticalLayout vLayout = new VerticalLayout();
    private HorizontalLayout hLayout = new HorizontalLayout();
    private Customer customer;
    private TextField firstName = new TextField("First Name");
    private TextField lastName = new TextField("Last Name");
    private VerticalLayout vLayoutNilai = new VerticalLayout();

    public PegawaiForm(CustomerRepo customerRepo, Action act, Optional<Long> customerId, Grid<Customer> customerGrid,
            NilaiRepo nilaiRepo) {
        Binder<Customer> binder = new Binder<>(Customer.class);
        binder.forField(firstName).bind(Customer::getFirstName, Customer::setFirstName);
        binder.forField(lastName).bind(Customer::getLastName, Customer::setLastName);
        if (act.equals(Action.ADD))
            customer = new Customer();

        if (act.equals(Action.EDIT)) {
            customer = customerRepo.findById(customerId.get()).get();
            for (Nilai nilai : customer.getLNilais()) {
                NumberField numberField = new NumberField("Nilai " + nilai.getMataKuliah().getNamaMatakuliah());
                numberField.setValue(nilai.getNilai());
                numberField.setId(String.valueOf(nilai.getId()));
                vLayoutNilai.add(numberField);
            }
        }
        binder.readBean(customer);
        final String pesan = null;
        Button btnSimpan = new Button("Simpan", e -> {
            customer.setFirstName(firstName.getValue());
            customer.setLastName(lastName.getValue());
            customer.setAuditDate(new Date());
            List<Nilai> liNilais = new ArrayList<>();

            Iterator<Component> iterator = vLayoutNilai.getChildren().iterator();
            while (iterator.hasNext()) {
                Component component = iterator.next();
                if (component.getClass().getSimpleName().equalsIgnoreCase("numberfield")) {
                    NumberField numberField = (NumberField) component;
                    Nilai nilai = nilaiRepo.findById(Long.valueOf(numberField.getId().get())).get();
                    if (numberField.getValue() == null) {
                        numberField.setInvalid(true);
                        numberField.setErrorMessage("tidak boleh kosong");
                        return;
                    }
                    nilai.setNilai(numberField.getValue());
                    nilaiRepo.save(nilai);
                    liNilais.add(nilai);
                }
            }

            // vLayoutNilai.getChildren().forEach(component -> {
            // NumberField numberField = (NumberField) component;
            // if (numberField.getValue() == null) {
            // pesan = numberField.getLabel() + " tidak boleh kosong";

            // }
            // Nilai nilai =
            // nilaiRepo.findById(Long.valueOf(numberField.getId().get())).get();
            // nilai.setNilai(numberField.getValue());
            // nilaiRepo.save(nilai);
            // liNilais.add(nilai);
            // });
            customer.setLNilais(liNilais);
            customerRepo.save(customer);
            customerGrid.getDataProvider().refreshAll();
            customerGrid.scrollToStart();
            close();
        });
        Button btnCancel = new Button("Batal", e -> {
            close();
        });
        hLayout.add(btnSimpan, btnCancel);
        vLayout.add(firstName, lastName, vLayoutNilai, hLayout);
        setWidth("80%");
        add(vLayout);
        setCloseOnEsc(true);
    }
}
