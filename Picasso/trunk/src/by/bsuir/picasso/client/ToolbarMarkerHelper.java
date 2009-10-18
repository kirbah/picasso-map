package by.bsuir.picasso.client;

import by.bsuir.picasso.client.data.ClientDataStorage;
import by.bsuir.picasso.client.data.MarkerModel;
import by.bsuir.picasso.shared.MarkerStorage;

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;

public class ToolbarMarkerHelper {
  public static void addMarkerButton(final ClientDataStorage cds) {
    final MessageBox box = MessageBox.prompt("Name", "Please enter new marker name:");
    box.addCallback(new Listener<MessageBoxEvent>() {
      public void handleEvent(MessageBoxEvent be) {
        Button btn = be.getButtonClicked();
        if (Dialog.OK.equals(btn.getItemId())) {
          final MarkerStorage addedMarker = new MarkerStorage();
          addedMarker.setName(be.getValue());

          final MapWidget map = cds.getMap();
          map.addMapClickHandler(new MapClickHandler() {
            public void onClick(MapClickEvent e) {
              map.removeMapClickHandler(this);
              MapWidget sender = e.getSender();
              // Overlay overlay = e.getOverlay();
              LatLng point = e.getLatLng();
              addedMarker.setLatitude(point.getLatitude());
              addedMarker.setLongitude(point.getLongitude());

              final Marker marker = MapHelper.createMarker(addedMarker);
              sender.addOverlay(marker);
              cds.getMarkersStore().add(new MarkerModel(cds, addedMarker, marker));
            }
          });
        } else {

        }
      }
    });
  }

}