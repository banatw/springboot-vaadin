package com.example.application.views.pegawai;

import com.example.application.entity.Customer;
import com.example.application.repo.CustomerRepo;
import com.example.application.views.Action;
import com.example.application.views.main.MainView;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
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

import java.util.List;

@Route(value = "pegawai", layout = MainView.class)
@PageTitle("Pegawai")
public class Pegawai extends VerticalLayout {
    /**
     *
     */
    @Autowired
    private CustomerRepo customerRepo;
    private Grid<Customer> customerGrid;
    private static final long serialVersionUID = 1L;
    private Action act;


    private class InnerDialog extends Dialog {

        /**
         *
         */
        private static final long serialVersionUID = 1L;

        /**
         *
         */
        private void initiate(Customer customer) {
            FormLayout formLayout = new FormLayout();

            TextField tFieldFirstName = new TextField("First Name");
            TextField tFieldLastName = new TextField("Last Name");
            if (act.equals(Action.EDIT)) {
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
                close();
                customerGrid.getDataProvider().refreshAll();
                customerGrid.scrollToStart();
            });
            Button btnCancel = new Button("Batal", e -> {
                close();
            });
            horizontalLayout.add(btnSimpan, btnCancel);
            add(formLayout, horizontalLayout);
        }

        public InnerDialog(Customer customer) {
            addAttachListener(event -> {
                if(event.isInitialAttach()) initiate(customer);
            });
        }

    }

    public Pegawai() {
        addAttachListener(event -> {
            if(event.isInitialAttach()) initiate();
        });
    }

    private void initiate() {
        final TextField filterField = new TextField();
        filterField.setValue("");
        filterField.setPlaceholder("search");
        final Button buttonAdd = new Button(new Icon(VaadinIcon.PLUS));
        buttonAdd.addClickListener(e -> {
            act = Action.ADD;
            InnerDialog innerDialog = new InnerDialog(null);
            innerDialog.open();
        });
        final HorizontalLayout bLayout = new HorizontalLayout();
        bLayout.add(filterField, buttonAdd);
        listCustomer("");
        add(bLayout, customerGrid);
    }
    void listCustomer(final String filterText) {
        // customerGrid.setDataProvider(null);
        DataProvider<Customer, Void> dataProvider = DataProvider.fromCallbacks(query -> {
            final int offset = query.getOffset();
            final int limit = query.getLimit();
            final int page = offset / limit;
            List<Customer> customers = customerRepo
                    .findAll(PageRequest.of(page, limit, Sort.Direction.DESC, "auditDate")).toList();

            return customers.stream();
        }, query -> (int) customerRepo.count());
        customerGrid = new Grid<>(Customer.class);
        customerGrid.setDataProvider(dataProvider);
        customerGrid.setColumns("firstName", "lastName");

        // customerGrid.setItems(customerRepo.findAll(Sort.by(Sort.Direction.DESC,
        // "auditDate")));

        customerGrid.addColumn(new ComponentRenderer<>(item -> {
            final Button buttonEdit = new Button("Edit", e -> {
                act = Action.EDIT;
                InnerDialog innerDialog = new InnerDialog(item);
                // System.out.println(item.getFirstName());
                innerDialog.open();
            });

            return buttonEdit;
        }));
        customerGrid.addColumn(new ComponentRenderer<>(item -> {
            final Button buttonDelete = new Button("Delete",
                    e -> Notification.show(String.valueOf(item.getIdCustomer())));
            return buttonDelete;
        }));
        // customerGrid.addColumn(new ComponentRenderer<>(item ->{
        // final Button
        // }));

    }
}
