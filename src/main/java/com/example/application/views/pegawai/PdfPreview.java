package com.example.application.views.pegawai;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.example.application.views.EmbeddedPdfDocument;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.StreamResourceWriter;
import com.vaadin.flow.server.VaadinSession;

import org.vaadin.alejandro.PdfBrowserViewer;

public class PdfPreview extends Dialog {

    /**
     *
     */

    private static final long serialVersionUID = 1L;

    public PdfPreview() {
        StreamResource streamResource = new StreamResource("download.pdf",
                () -> getClass().getResourceAsStream("/download.pdf")); // file in src/main/resources/

        StreamResource streamResource2 = new StreamResource("download.pdf", new InputStreamFactory() {

            @Override
            public InputStream createInputStream() {
                // TODO Auto-generated method stub
                try {
                    FileInputStream fis = new FileInputStream(new File("d:/opt/download2.pdf"));
                    return fis;
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            }

        });

        EmbeddedPdfDocument viewer = new EmbeddedPdfDocument(streamResource2);
        viewer.setWidth("100%");
        viewer.setHeight("100%");
        setWidthFull();
        setHeightFull();
        add(new Button("close", event -> close()), viewer);

    }

}
