package com.example.application.views.pegawai;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import com.example.application.entity.Customer;
import com.example.application.repo.CustomerRepo;
import com.example.application.views.Action;
import com.example.application.views.main.MainView;
import com.example.application.views.pegawai.form.PegawaiForm;
import com.example.application.views.preview.PdfPreview;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import de.codecamp.vaadin.components.messagedialog.MessageDialog;

@Route(value = "pegawai", layout = MainView.class)
@PageTitle("Pegawai")
public class Pegawai extends Div {
    /**
     *
     */
    @Autowired
    private CustomerRepo customerRepo;
    private Grid<Customer> customerGrid;
    private Action act;
    private VerticalLayout vLayout = new VerticalLayout();

    @Autowired
    private DataSource dataSource;

    public Pegawai() {
        TextField filterField = new TextField();
        filterField.setValue("");
        filterField.setPlaceholder("search");
        Button buttonAdd = new Button(new Icon(VaadinIcon.PLUS));
        buttonAdd.addClickListener(e -> {
            act = Action.ADD;
            PegawaiForm pegawaiForm = new PegawaiForm(customerRepo, act, Optional.empty(), customerGrid);
            pegawaiForm.open();
        });
        HorizontalLayout bLayout = new HorizontalLayout();
        Button buttonPrintPreview = new Button("Print", event -> {
            PdfPreview pdfPreview = new PdfPreview(dataSource, null, "all.jrxml");
            pdfPreview.open();
        });
        bLayout.add(filterField, buttonAdd, buttonPrintPreview);
        refreshGrid("");
        setSizeFull();
        vLayout.setSizeFull();
        vLayout.add(bLayout, customerGrid);
        add(vLayout);

    }

    public void refreshGrid(String filterText) {
        // customerGrid.setDataProvider(null);
        DataProvider<Customer, Void> dataProvider = DataProvider.fromCallbacks(query -> {
            int offset = query.getOffset();
            int limit = query.getLimit();
            int page = offset / limit;
            List<Customer> customers = customerRepo
                    .findAll(PageRequest.of(page, limit, Sort.Direction.DESC, "auditDate")).toList();

            return customers.stream();
        }, query -> (int) customerRepo.count());
        customerGrid = new Grid<>(Customer.class);
        customerGrid.setDataProvider(dataProvider);
        customerGrid.setColumns("firstName", "lastName");

        customerGrid.addColumn(new ComponentRenderer<>(item -> {
            Button buttonEdit = new Button("Edit", e -> {
                act = Action.EDIT;
                PegawaiForm pegawaiForm = new PegawaiForm(customerRepo, act, Optional.of(item.getIdCustomer()),
                        customerGrid);
                // System.out.println(item.getFirstName());
                pegawaiForm.open();
            });

            return buttonEdit;
        }));
        customerGrid.addColumn(new ComponentRenderer<>(item -> {
            Button buttonDelete = new Button("Delete", e -> {
                MessageDialog messageDialog = new MessageDialog().setTitle("Perhatian", VaadinIcon.WARNING.create())
                        .setMessage("Apakah anda yakin?");
                messageDialog.addButton().text("Tidak").icon(VaadinIcon.WARNING).error()
                        .onClick(ev -> Notification.show("Discarded.")).closeOnClick();
                messageDialog.addButton().text("Ya").primary().onClick(ev -> {
                    customerRepo.deleteById(item.getIdCustomer());
                    customerGrid.getDataProvider().refreshAll();
                }).closeOnClick();
                messageDialog.open();
            });
            return buttonDelete;
        }));
        customerGrid.addColumn(new ComponentRenderer<>(item -> {
            return new Button("Cetak", event -> {
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("CustomerId", item.getIdCustomer());
                PdfPreview pdfPreview = new PdfPreview(dataSource, params, "tes.jrxml");
                pdfPreview.open();
            });
        }));
    }

}
