package by.bsuir.picasso.client;

import by.bsuir.picasso.client.data.ClientDataStorage;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormButtonBinding;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;

public class ElevationExportWindow {
  public static Window showElevationWindow(final ClientDataStorage cds) {
    FormData formData = new FormData("100%");
    final Window window = new Window();
    window.setSize(400, 300);
    window.setPlain(true);
    window.setModal(true);
    window.setBlinkModal(true);
    window.setHeading(Picasso.CONSTANTS.elevationExport());
    window.setLayout(new FitLayout());

    FormPanel panel = new FormPanel();
    panel.setPadding(5);
    panel.setFrame(false);
    panel.setHeaderVisible(false);
    panel.setBodyBorder(false);
    panel.setButtonAlign(HorizontalAlignment.CENTER);

    LayoutContainer main = new LayoutContainer();
    main.setLayout(new ColumnLayout());

    // Latitude
    FieldSet left = new FieldSet();
    left.setHeading("Latitude");
    left.setStyleAttribute("padding", "5px");
    FormLayout layout = new FormLayout();
    layout.setLabelWidth(75);
    left.setLayout(layout);

    NumberField latMin = new NumberField();
    latMin.setFieldLabel("Min");
    left.add(latMin, formData);

    NumberField latMax = new NumberField();
    latMax.setFieldLabel("Max");
    left.add(latMax, formData);

    NumberField latSteps = new NumberField();
    latSteps.setFieldLabel("Pieces");
    latSteps.setValue(20);
    left.add(latSteps, formData);

    LabelField latDelta = new LabelField();
    latDelta.setFieldLabel("Delta");
    left.add(latDelta, formData);

    // Longitude
    FieldSet right = new FieldSet();
    right.setHeading("Longitude");
    right.setStyleAttribute("padding", "5px");
    layout = new FormLayout();
    layout.setLabelWidth(75);
    right.setLayout(layout);

    NumberField lngMin = new NumberField();
    lngMin.setFieldLabel("Min");
    right.add(lngMin, formData);

    NumberField lngMax = new NumberField();
    lngMax.setFieldLabel("Max");
    right.add(lngMax, formData);

    NumberField lngSteps = new NumberField();
    lngSteps.setFieldLabel("Pieces");
    lngSteps.setValue(20);
    right.add(lngSteps, formData);

    LabelField lngDelta = new LabelField();
    lngDelta.setFieldLabel("Delta");
    right.add(lngDelta, formData);

    main.add(left, new ColumnData(.5));
    main.add(right, new ColumnData(.5));
    panel.add(main, new FormData("100%"));

    final TextArea description = new TextArea();
    // description.setPreventScrollbars(true);
    description.setFieldLabel("Description");
    panel.add(description, formData);

    Button submit = new Button("Submit");

    submit.addSelectionListener(new SelectionListener<ButtonEvent>() {
      public void componentSelected(ButtonEvent ce) {
        description.setRawValue("LAT;LNG;METERS\r\n");
        ElevationExport.begin(description, 53.93, 27.69, 53.95, 27.75, 0.01, 0.01);
      }
    });

    panel.addButton(submit);
    panel.addButton(new Button("Cancel"));

    FormButtonBinding binding = new FormButtonBinding(panel);
    binding.addButton(submit);

    window.add(panel);
    window.show();

    return window;
  }
}
