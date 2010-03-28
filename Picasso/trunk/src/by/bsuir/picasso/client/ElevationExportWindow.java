package by.bsuir.picasso.client;

import by.bsuir.picasso.client.data.ClientDataStorage;
import by.bsuir.picasso.client.data.MarkerModel;
import by.bsuir.picasso.client.data.PolyModel;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormButtonBinding;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.LatLngBounds;

public class ElevationExportWindow {
  private static double _latMin;
  private static double _latMax;
  private static double _lngMin;
  private static double _lngMax;

  private static double _latStep;
  private static double _lngStep;

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
    left.setHeading(Picasso.CONSTANTS.exportLatitude());
    left.setStyleAttribute("padding", "5px");
    FormLayout layout = new FormLayout();
    layout.setLabelWidth(85);
    left.setLayout(layout);

    final NumberField latMaxField = new NumberField();
    latMaxField.setFieldLabel(Picasso.CONSTANTS.exportMax());
    left.add(latMaxField, formData);

    final NumberField latMinField = new NumberField();
    latMinField.setFieldLabel(Picasso.CONSTANTS.exportMin());
    left.add(latMinField, formData);

    final NumberField latSteps = new NumberField();
    latSteps.setFieldLabel(Picasso.CONSTANTS.exportPieces());
    latSteps.setPropertyEditorType(Integer.class);
    latSteps.setValue(20);
    left.add(latSteps, formData);

    final LabelField latDelta = new LabelField();
    latDelta.setFieldLabel(Picasso.CONSTANTS.exportDelta());
    left.add(latDelta, formData);

    Listener<FieldEvent> latitudeCalcListener = new Listener<FieldEvent>() {
      public void handleEvent(FieldEvent be) {
        latitudeCalculate(latMaxField, latMinField, latSteps, latDelta);
      }
    };
    latMinField.addListener(Events.Change, latitudeCalcListener);
    latMaxField.addListener(Events.Change, latitudeCalcListener);
    latSteps.addListener(Events.Change, latitudeCalcListener);

    // Longitude
    FieldSet right = new FieldSet();
    right.setHeading(Picasso.CONSTANTS.exportLongitude());
    right.setStyleAttribute("padding", "5px");
    layout = new FormLayout();
    layout.setLabelWidth(85);
    right.setLayout(layout);

    final NumberField lngMaxField = new NumberField();
    lngMaxField.setFieldLabel(Picasso.CONSTANTS.exportMax());
    right.add(lngMaxField, formData);

    final NumberField lngMinField = new NumberField();
    lngMinField.setFieldLabel(Picasso.CONSTANTS.exportMin());
    right.add(lngMinField, formData);

    final NumberField lngSteps = new NumberField();
    lngSteps.setFieldLabel(Picasso.CONSTANTS.exportPieces());
    lngSteps.setValue(20);
    right.add(lngSteps, formData);

    final LabelField lngDelta = new LabelField();
    lngDelta.setFieldLabel(Picasso.CONSTANTS.exportDelta());
    right.add(lngDelta, formData);

    Listener<FieldEvent> longitudeCalcListener = new Listener<FieldEvent>() {
      public void handleEvent(FieldEvent be) {
        longitudeCalculate(lngMaxField, lngMinField, lngSteps, lngDelta);
      }
    };
    lngMinField.addListener(Events.Change, longitudeCalcListener);
    lngMaxField.addListener(Events.Change, longitudeCalcListener);
    lngSteps.addListener(Events.Change, longitudeCalcListener);

    main.add(left, new ColumnData(.5));
    main.add(right, new ColumnData(.5));
    panel.add(main, new FormData("100%"));

    // SRTM/Aster selection
    final SimpleComboBox<String> serviceCombo = new SimpleComboBox<String>();
    serviceCombo.add(Picasso.CONSTANTS.exportAster());
    serviceCombo.add(Picasso.CONSTANTS.exportSRTM());
    serviceCombo.setSimpleValue(Picasso.CONSTANTS.exportAster());
    serviceCombo.setTriggerAction(TriggerAction.ALL);
    serviceCombo.setFieldLabel(Picasso.CONSTANTS.exportService());
    serviceCombo.setEditable(false);
    panel.add(serviceCombo, formData);

    // Export results area
    final TextArea dataArea = new TextArea();
    dataArea.setFieldLabel(Picasso.CONSTANTS.exportData());
    panel.add(dataArea, formData);

    Button submit = new Button(Picasso.CONSTANTS.exportSubmit());
    submit.addSelectionListener(new SelectionListener<ButtonEvent>() {
      public void componentSelected(ButtonEvent ce) {
        final MessageBox box = MessageBox.progress(Picasso.CONSTANTS.progressWait(), Picasso.CONSTANTS
            .progressMessage(), Picasso.CONSTANTS.progressInit());

        dataArea.setRawValue("LAT;LNG;METERS\r\n");
        String service = "astergdem";
        if (Picasso.CONSTANTS.exportSRTM().equals(serviceCombo.getSimpleValue())) {
          service = "srtm3";
        }
        double latMin = latMinField.getValue().doubleValue();
        double latMax = latMaxField.getValue().doubleValue();
        double lngMin = lngMinField.getValue().doubleValue();
        double lngMax = lngMaxField.getValue().doubleValue();
        ElevationExport.begin(box, dataArea, service, latMin, lngMin, latMax, lngMax, _latStep, _lngStep);
      }
    });
    panel.addButton(submit);

    Button closeBtn = new Button(Picasso.CONSTANTS.buttonClose());
    closeBtn.addSelectionListener(new SelectionListener<ButtonEvent>() {
      public void componentSelected(ButtonEvent ce) {
        window.hide();
      }
    });
    panel.addButton(closeBtn);

    FormButtonBinding binding = new FormButtonBinding(panel);
    binding.addButton(submit);

    window.add(panel);
    window.show();

    // Visible map
    LatLngBounds mapBounds = cds.getMap().getBounds();
    _latMax = mapBounds.getNorthEast().getLatitude();
    _lngMax = mapBounds.getNorthEast().getLongitude();
    _latMin = mapBounds.getSouthWest().getLatitude();
    _lngMin = mapBounds.getSouthWest().getLongitude();

    // Markers
    for (MarkerModel markerModel : cds.getMarkersStore().getModels()) {
      LatLng point = markerModel.getMarker().getLatLng();
      if (_latMin > point.getLatitude()) {
        _latMin = point.getLatitude();
      }
      if (_latMax < point.getLatitude()) {
        _latMax = point.getLatitude();
      }
      if (_lngMin > point.getLongitude()) {
        _lngMin = point.getLongitude();
      }
      if (_lngMax < point.getLongitude()) {
        _lngMax = point.getLongitude();
      }
    }

    // Polygons
    for (PolyModel polyModel : cds.getPolygonStore().getModels()) {
      LatLngBounds bounds = polyModel.getPolygon().getBounds();
      if (_latMax < bounds.getNorthEast().getLatitude()) {
        _latMax = bounds.getNorthEast().getLatitude();
      }
      if (_lngMax < bounds.getNorthEast().getLongitude()) {
        _lngMax = bounds.getNorthEast().getLongitude();
      }
      if (_latMin > bounds.getSouthWest().getLatitude()) {
        _latMin = bounds.getSouthWest().getLatitude();
      }
      if (_lngMin > bounds.getSouthWest().getLongitude()) {
        _lngMin = bounds.getSouthWest().getLongitude();
      }
    }

    // Set min/max fields values
    latMinField.setValue(round(_latMin - 0.01));
    latMaxField.setValue(round(_latMax + 0.01));
    lngMinField.setValue(round(_lngMin - 0.01));
    lngMaxField.setValue(round(_lngMax + 0.01));

    latitudeCalculate(latMaxField, latMinField, latSteps, latDelta);
    longitudeCalculate(lngMaxField, lngMinField, lngSteps, lngDelta);

    return window;
  }

  private static double round(double in) {
    int rounded = (int) (in * 100.0);
    return ((double) rounded) / 100.0;
  }

  private static void latitudeCalculate(final NumberField latMaxField, final NumberField latMinField,
      final NumberField latSteps, final LabelField latDelta) {
    double delta = 0.1;
    if (latSteps.getValue().intValue() > 0) {
      delta = (latMaxField.getValue().doubleValue() - latMinField.getValue().doubleValue())
          / latSteps.getValue().doubleValue();
    }
    _latStep = ElevationExport.round(delta);
    latDelta.setValue(_latStep);
  }

  private static void longitudeCalculate(final NumberField lngMaxField, final NumberField lngMinField,
      final NumberField lngSteps, final LabelField lngDelta) {
    double delta = 0.1;
    if (lngSteps.getValue().intValue() > 0) {
      delta = (lngMaxField.getValue().doubleValue() - lngMinField.getValue().doubleValue())
          / lngSteps.getValue().doubleValue();
    }
    _lngStep = ElevationExport.round(delta);
    lngDelta.setValue(_lngStep);
  }
}
