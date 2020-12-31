package com.example.application.views.preview;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.UUID;

import javax.sql.DataSource;

import com.example.application.views.main.MainView;
import com.example.application.views.pegawai.Pegawai;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;
import com.vaadin.flow.router.WildcardParameter;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;

import org.springframework.beans.factory.annotation.Autowired;

// import org.vaadin.alejandro.PdfBrowserViewer;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;

@RoutePrefix(value = "pegawai")
@Route(value = "preview_no_params", layout = MainView.class)
@PageTitle(value = "Preview")
public class PdfPreviewNoParameter extends Div implements HasUrlParameter<String> {
    @Autowired
    private DataSource dataSource;

    private String namaFile;

    // private Long parameterLong;

    // private Map<String, Object> parameters = new HashMap<>();

    /**
     *
     */
    private VerticalLayout vLayout = new VerticalLayout();

    private static final long serialVersionUID = 1L;

    private void initComponents() {
        StreamResource streamResource = new StreamResource(UUID.randomUUID().toString().replace("-", "") + ".pdf",
                new InputStreamFactory() {

                    @Override
                    public InputStream createInputStream() {
                        // TODO Auto-generated method stub
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        try {
                            // parameters.put("CustomerId", parameterLong);
                            JasperReport jasperReport = JasperCompileManager
                                    .compileReport(getClass().getResourceAsStream("/" + namaFile));
                            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null,
                                    dataSource.getConnection());
                            JRPdfExporter pdfExporter = new JRPdfExporter();
                            SimplePdfReportConfiguration config = new SimplePdfReportConfiguration();
                            config.setSizePageToContent(true);
                            pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                            pdfExporter.setConfiguration(config);
                            pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
                            pdfExporter.exportReport();
                            byte[] buf = byteArrayOutputStream.toByteArray();
                            byteArrayOutputStream.close();
                            return new ByteArrayInputStream(buf);

                        } catch (JRException | SQLException | IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        return null;
                    }

                });
        EmbeddedPdf viewer = new EmbeddedPdf(streamResource);
        viewer.setSizeFull();
        Anchor download = new Anchor(streamResource, "Download");
        download.setTarget("_blank");
        vLayout.add(download, viewer, new Button("Kembali", e -> UI.getCurrent().navigate(Pegawai.class)));
        vLayout.add();
        vLayout.setSizeFull();
        setSizeFull();
        add(vLayout);
    }

    @Override
    public void setParameter(BeforeEvent event, @WildcardParameter String parameter) {
        // this.parameterLong = Long.valueOf(event.getLocation().getSegments().get(2));
        this.namaFile = event.getLocation().getSegments().get(2) + ".jrxml";
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        if (attachEvent.isInitialAttach()) {
            initComponents();
        }
    }

}
