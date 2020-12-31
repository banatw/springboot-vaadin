package com.example.application.views.pegawai;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.example.application.entity.Customer;
import com.example.application.entity.Nilai;
import com.example.application.repo.CustomerRepo;
import com.example.application.repo.NilaiRepo;
import com.example.application.views.Action;
import com.example.application.views.main.MainView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;
import com.vaadin.flow.router.WildcardParameter;

import org.springframework.beans.factory.annotation.Autowired;

@RoutePrefix(value = "pegawai")
@Route(value = "form", layout = MainView.class)
@PageTitle(value = "Pegawai Form")
public class PegawaiForm extends Div implements HasUrlParameter<String> {
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private NilaiRepo nilaiRepo;
    private Action act;
    private Optional<Long> customerId;

    private VerticalLayout vLayout = new VerticalLayout();
    // private HorizontalLayout hLayout = new HorizontalLayout();
    private Customer customer;
    private TextField firstName = new TextField("First Name");
    private TextField lastName = new TextField("Last Name");
    private VerticalLayout vLayoutNilai = new VerticalLayout();

    public PegawaiForm() {

    }

    @Override
    public void setParameter(BeforeEvent event, @WildcardParameter String parameter) {
        // TODO Auto-generated method stub
        // QueryParameters queryParameters = event.getLocation().getQueryParameters();
        // Map<String, List<String>> params = queryParameters.getParameters();
        // System.out.println(parameter);
        this.customerId = Optional.ofNullable(Long.valueOf(event.getLocation().getSegments().get(2)));
        this.act = Action.valueOf(event.getLocation().getSegments().get(3));
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        // TODO Auto-generated method stub
        if (attachEvent.isInitialAttach()) {
            initComponents();
        }
    }

    private void simpan() {
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
        customer.setLNilais(liNilais);
        customerRepo.save(customer);
    }

    private void initComponents() {
        customer = customerRepo.findById(customerId.get()).get();
        firstName.setValue(customer.getFirstName());
        lastName.setValue(customer.getLastName());
        for (Nilai nilai : customer.getLNilais()) {
            NumberField numberField = new NumberField("Nilai " + nilai.getMataKuliah().getNamaMatakuliah());
            numberField.setValue(nilai.getNilai());
            numberField.setId(String.valueOf(nilai.getId()));
            vLayoutNilai.add(numberField);
        }
        HorizontalLayout layoutButton = new HorizontalLayout();
        layoutButton.add(new Button("Batal", e -> UI.getCurrent().navigate(Pegawai.class)), new Button("Simpan", e -> {
            simpan();
            UI.getCurrent().navigate(Pegawai.class);
        }));
        vLayout.add(firstName, lastName, vLayoutNilai, layoutButton);
        add(vLayout);
    }
}
