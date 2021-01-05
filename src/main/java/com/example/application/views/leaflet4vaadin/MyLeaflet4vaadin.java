package com.example.application.views.leaflet4vaadin;

import com.example.application.views.main.MainView;
import com.vaadin.addon.leaflet4vaadin.LeafletMap;
import com.vaadin.addon.leaflet4vaadin.layer.map.options.DefaultMapOptions;
import com.vaadin.addon.leaflet4vaadin.layer.map.options.MapOptions;
import com.vaadin.addon.leaflet4vaadin.types.LatLng;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "leaflet4vaadin", layout = MainView.class)
@PageTitle(value = "Leaflet 4 Vaadin")
public class MyLeaflet4vaadin extends Div {
    // private final VerticalLayout verticalLayout = new VerticalLayout();

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        // TODO Auto-generated method stub
        if (attachEvent.isInitialAttach()) {
            MapOptions options = new DefaultMapOptions();
            options.setCenter(new LatLng(47.070121823, 19.204101562500004));
            options.setZoom(7);
            LeafletMap leafletMap = new LeafletMap(options);
            leafletMap.setBaseUrl("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png");
            add(leafletMap);
        }
    }

}
