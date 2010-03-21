package by.bsuir.picasso.client;

import by.bsuir.picasso.client.data.ClientDataStorage;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormButtonBinding;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;

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
    panel.setPadding(0);
    panel.setFrame(false);
    panel.setHeaderVisible(false);
    panel.setBodyBorder(false);
    panel.setButtonAlign(HorizontalAlignment.CENTER);
    // panel.setLayout(new FitLayout());

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
