package com.example.cdi_workshop;

import com.vaadin.annotations.Theme;
import com.vaadin.cdi.CDIUI;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@Theme("cdi_workshop")
@CDIUI("")
public class Cdi_workshopUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        Navigator navigator = new Navigator(this, this);
        navigator.addView("", LoginView.class);
        navigator.addView("chat", ChatView.class);
    }

}