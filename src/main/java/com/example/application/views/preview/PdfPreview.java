package com.example.application.views.preview;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

import javax.sql.DataSource;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.StreamResourceWriter;
import com.vaadin.flow.server.VaadinSession;

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

public class PdfPreview extends Dialog {

    /**
     *
     */
    private VerticalLayout vLayout = new VerticalLayout();

    private static final long serialVersionUID = 1L;

    public PdfPreview(DataSource dataSource, HashMap<String, Object> parameters, String namaFile) {
        StreamResource streamResource = new StreamResource(UUID.randomUUID().toString().replace("-", "") + ".pdf",
                new InputStreamFactory() {

                    @Override
                    public InputStream createInputStream() {
                        // TODO Auto-generated method stub
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        try {
                            // FileInputStream fileInputStream = new
                            // FileInputStream(getClass().getResourceAsStream("resName"));
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
        EmbeddedPdf viewer = new EmbeddedPdf(streamResource);
        viewer.setHeight("100%");
        Anchor download = new Anchor(streamResource, "Download");
        download.setTarget("_blank");
        vLayout.add(new Button("Close", e -> close()), download, viewer);
        vLayout.add();
        vLayout.setSizeFull();
        setSizeFull();
        add(vLayout);
        setCloseOnEsc(true);
        setWidth("100%");
    }

}
