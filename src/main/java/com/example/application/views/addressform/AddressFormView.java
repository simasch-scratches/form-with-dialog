package com.example.application.views.addressform;

import com.example.application.data.entity.SampleAddress;
import com.example.application.data.service.SampleAddressService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.shared.Registration;

import java.util.concurrent.atomic.AtomicReference;

@PageTitle("Address Form")
@Route(value = "address-form", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class AddressFormView extends Div {

    private TextField street = new TextField("Street address");
    private TextField postalCode = new TextField("Postal code");
    private TextField city = new TextField("City");
    private ComboBox<String> state = new ComboBox<>("State");
    private ComboBox<String> country = new ComboBox<>("Country");

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private Binder<SampleAddress> binder = new Binder<>(SampleAddress.class);

    public AddressFormView(SampleAddressService addressService) {
        addClassName("address-form-view");

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());

        // Doesn't work!
//        Shortcuts.addShortcutListener(street,
//                event -> {
//                    new SomeDialog().open();
//                },
//                Key.F9).listenOn(street);

        AtomicReference<Registration> blur = new AtomicReference<>();
        Shortcuts.addShortcutListener(street,
                () -> {
                    blur.set(street.addBlurListener(blurEvent -> {
                        new SomeDialog().open();
                        blur.get().remove();
                    }));
                    street.blur();
                },
                Key.F9).listenOn(street);

        binder.bindInstanceFields(this);

        clearForm();

        cancel.addClickListener(e -> clearForm());
        save.addClickListener(e -> {
            addressService.update(binder.getBean());
            Notification.show(binder.getBean().getClass().getSimpleName() + " stored. street=" + binder.getBean().getStreet());
            clearForm();
        });
    }

    private Component createTitle() {
        return new H3("Address");
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(street, 2);
        postalCode.setPattern("\\d*");
        postalCode.setPreventInvalidInput(true);
        country.setItems("Country 1", "Country 2", "Country 3");
        state.setItems("State A", "State B", "State C", "State D");
        formLayout.add(postalCode, city, state, country);
        return formLayout;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save);
        buttonLayout.add(cancel);
        return buttonLayout;
    }

    private void clearForm() {
        this.binder.setBean(new SampleAddress());
    }

}
