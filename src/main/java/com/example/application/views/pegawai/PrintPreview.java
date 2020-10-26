package com.example.application.views.pegawai;

import com.example.application.entity.Customer;
import com.example.application.repo.CustomerRepo;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.IFrame;

import org.vaadin.reports.PrintPreviewReport;

public class PrintPreview extends Dialog {

    public PrintPreview(CustomerRepo customerRepo) {
        PrintPreviewReport<Customer> cPreviewReport = new PrintPreviewReport<>(Customer.class);
        cPreviewReport.setItems(customerRepo.findAll());
        add(cPreviewReport);
        IFrame iFrame = new IFrame();
        // iFrame.set
    }

}
