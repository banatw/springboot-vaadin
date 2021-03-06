package com.example.application.views.pegawai;

import java.util.List;

import com.example.application.entity.Customer;
import com.example.application.repo.CustomerRepo;
import com.example.application.views.Action;
import com.example.application.views.main.MainView;
import com.example.application.views.preview.PdfPreviewNoParameter;
import com.example.application.views.preview.PdfPreviewWithParameter;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
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
import lombok.Getter;
import lombok.Setter;

@Route(value = "pegawai", layout = MainView.class)
@PageTitle("Pegawai")
public class Pegawai extends Div {
    /**
     *
     */
    @Autowired
    private CustomerRepo customerRepo;
    private Grid<Customer> customerGrid = new Grid<>(Customer.class);
    private static Action act;
    private VerticalLayout vLayout = new VerticalLayout();

    /**
     * InnerPegawai
     */
    @Setter
    @Getter
    public class Param {
        private Long id;
        private Action action;
    }

    public Pegawai() {

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

        customerGrid.setDataProvider(dataProvider);
        customerGrid.setColumns("firstName", "lastName");

        customerGrid.addColumn(new ComponentRenderer<>(item -> {
            // Map<String, String> params = new HashMap<>();
            // params.put("id", String.valueOf(item.getIdCustomer()));
            // params.put("action", act.EDIT.toString());
            return new Button("Edit", e -> {
                UI.getCurrent().navigate(PegawaiForm.class, item.getIdCustomer() + "/" + Action.EDIT.toString());
            });

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
        // customerGrid.addColumn(new ComponentRenderer<>(item -> {
        // return new Button("Cetak", event -> {
        // HashMap<String, Object> params = new HashMap<String, Object>();
        // params.put("CustomerId", item.getIdCustomer());
        // PdfPreview pdfPreview = new PdfPreview(dataSource, params, "tes.jrxml");
        // pdfPreview.open();
        // });
        // }));
        customerGrid.addColumn(new ComponentRenderer<>(item -> {
            return new Button("Cetak", event -> {
                // UI.getCurrent().navigate(MyPdfBrowser.class, item.getIdCustomer());
                UI.getCurrent().navigate(PdfPreviewWithParameter.class, String.valueOf(item.getIdCustomer()) + "/tes");
            });
        }));

    }

    // StreamResource geStreamResource(HashMap<String, Object> parameters, String
    // namaFile) {
    // return new StreamResource(UUID.randomUUID().toString().replace("-", "") +
    // ".pdf", new InputStreamFactory() {

    // @Override
    // public InputStream createInputStream() {
    // // TODO Auto-generated method stub
    // ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    // try {
    // // FileInputStream fileInputStream = new
    // // FileInputStream(getClass().getResourceAsStream("resName"));
    // JasperReport jasperReport = JasperCompileManager
    // .compileReport(getClass().getResourceAsStream("/" + namaFile));
    // JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
    // parameters,
    // dataSource.getConnection());
    // JRPdfExporter pdfExporter = new JRPdfExporter();
    // SimplePdfReportConfiguration config = new SimplePdfReportConfiguration();
    // config.setSizePageToContent(true);
    // pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
    // pdfExporter.setConfiguration(config);
    // pdfExporter.setExporterOutput(new
    // SimpleOutputStreamExporterOutput(byteArrayOutputStream));
    // pdfExporter.exportReport();
    // byte[] buf = byteArrayOutputStream.toByteArray();
    // byteArrayOutputStream.close();
    // return new ByteArrayInputStream(buf);

    // } catch (JRException | SQLException | IOException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // return null;
    // }

    // });
    // }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        // TODO Auto-generated method stub
        if (attachEvent.isInitialAttach()) {
            TextField filterField = new TextField();
            filterField.setValue("");
            filterField.setPlaceholder("search");
            Button buttonAdd = new Button(new Icon(VaadinIcon.PLUS));
            buttonAdd.addClickListener(e -> {
                act = Action.ADD;
                // PegawaiForm pegawaiForm = new PegawaiForm(customerRepo, act,
                // Optional.empty(), customerGrid, nilaiRepo);
                // pegawaiForm.open();
            });
            HorizontalLayout bLayout = new HorizontalLayout();
            Button buttonPrintPreview = new Button("Print", event -> {
                // PdfPreview pdfPreview = new PdfPreview(dataSource, null, "all.jrxml");
                // pdfPreview.open();
                UI.getCurrent().navigate(PdfPreviewNoParameter.class, "all");
            });
            bLayout.add(filterField, buttonAdd, buttonPrintPreview);
            refreshGrid("");
            setSizeFull();
            vLayout.setSizeFull();
            vLayout.add(bLayout, customerGrid);
            add(vLayout);
        }
    }
}
