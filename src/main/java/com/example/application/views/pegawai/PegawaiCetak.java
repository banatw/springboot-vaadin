package com.example.application.views.pegawai;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.sql.DataSource;

import com.example.application.views.main.MainView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.alejandro.PdfBrowserViewer;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;

@Route(value = "pegawai/cetak", layout = MainView.class)
@PageTitle(value = "Cetak")
public class PegawaiCetak extends Div implements HasUrlParameter<String> {

    private Long params;
    @Autowired
    private DataSource dataSource;
    private String namaFile = "tes.jrxml";

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        // TODO Auto-generated method stub
        if (attachEvent.isInitialAttach()) {
            initComponent();
        }
    }

    private void initComponent() {
        StreamResource streamResource = new StreamResource(UUID.randomUUID().toString().replace("-", "") + ".pdf",
                new InputStreamFactory() {

                    @Override
                    public InputStream createInputStream() {
                        // TODO Auto-generated method stub
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        try {
                            Map<String, Object> parameters = new HashMap<>();
                            parameters.put("CustomerId", params);
                            JasperReport jasperReport = JasperCompileManager
                                    .compileReport(getClass().getResourceAsStream("/" + namaFile));
                            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
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

        PdfBrowserViewer viewer = new PdfBrowserViewer(streamResource);
        viewer.setHeight("100%");
        setSizeFull();
        add(viewer);
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        // TODO Auto-generated method stub
        this.params = Long.valueOf(event.getLocation().getSegments().get(2));
    }

}
