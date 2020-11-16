package com.example.application.views.preview;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.server.StreamResource;

@Tag("object")
public class EmbeddedPdf extends Component implements HasSize {

    public EmbeddedPdf(StreamResource resource) {
        this();
        getElement().setAttribute("data", resource);
    }

    public EmbeddedPdf(String url) {
        this();
        getElement().setAttribute("data", url);
    }

    protected EmbeddedPdf() {
        getElement().setAttribute("type", "application/pdf");
        setSizeFull();
    }
}
