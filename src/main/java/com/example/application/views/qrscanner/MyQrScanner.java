package com.example.application.views.qrscanner;

import com.example.application.views.main.MainView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import sk.smartbase.component.qrscanner.QrScanner;

@Route(value = "qrscanner", layout = MainView.class)
@PageTitle(value = "QRScanner")
public class MyQrScanner extends Div {
    private final VerticalLayout verticalLayout = new VerticalLayout();

    public MyQrScanner() {

    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        if (attachEvent.isInitialAttach()) {
            QrScanner qrScanner = new QrScanner();
            // qrScanner.setShowChangeCamera(true);
            qrScanner.setSwitchCameraAfterStart(true);
            qrScanner.setConsumer(event -> {
                if (!event.equalsIgnoreCase(QrScanner.ERROR_DECODING_QR_CODE))
                    qrScanner.setActive(false);
                System.out.println("consumer event value: " + event);
            });
            verticalLayout.add(qrScanner);

            add(verticalLayout);
        }

    }

}
