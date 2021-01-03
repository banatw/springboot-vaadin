package com.example.application.views.barcodescanner;

import com.example.application.views.main.MainView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import sk.smartbase.component.barcodescanner.BarcodeScanner;
import sk.smartbase.component.barcodescanner.DecoderEnum;

@Route(value = "barcodescanner", layout = MainView.class)
@PageTitle("Barcode Scanner")
public class MyBarcodeScanner extends Div {
    private final VerticalLayout layout = new VerticalLayout();

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        // TODO Auto-generated method stub
        if (attachEvent.isInitialAttach()) {
            BarcodeScanner barcodeScanner = new BarcodeScanner();
            Label lastScannedLabel = new Label("Last scanned value: unknown");
            barcodeScanner.setConsumer(event -> {
                System.out.println("data: " + event);
                lastScannedLabel.setText("Last scanned value: " + event.getCodeResult().getCode());
            });
            layout.add(barcodeScanner);
            layout.add(lastScannedLabel);
            barcodeScanner.setReadersEnums(DecoderEnum.CODE_39_READER);
            barcodeScanner.setStopAfterScan(false);
            layout.add(new Button("stop", e -> {
                barcodeScanner.stop();
            }));
            layout.add(new Button("start", e -> {
                barcodeScanner.initAndStart();
            }));
            add(layout);
        }
    }

}
