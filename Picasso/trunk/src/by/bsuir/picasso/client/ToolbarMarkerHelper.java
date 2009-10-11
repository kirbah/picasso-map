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
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.event.MarkerDragEndHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;

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
              addedMarker.setLatLng(point);

              final Marker marker = createMarker(addedMarker);
              sender.addOverlay(marker);
              cds.getMarkersStore().add(new MarkerModel(addedMarker, marker));
            }
          });
        } else {

        }
        //Info.display("MessageBox", "You entered '{0}'", new Params(btn.getItemId()));
      }
    });
  }

  private static Marker createMarker(MarkerStorage markerStore) {
    MarkerOptions options = MarkerOptions.newInstance();
    options.setDraggable(true);
    options.setTitle(markerStore.getName());
    final Marker marker = new Marker(markerStore.getLatLng(), options);
    marker.addMarkerDragEndHandler(new MyMarkerDragEndHandler());
    marker.addMarkerClickHandler(new MyMarkerClickHandler());
    return marker;
  }

  static class MyMarkerDragEndHandler implements MarkerDragEndHandler {
    public void onDragEnd(MarkerDragEndEvent event) {
      Marker marker = event.getSender();
      /*
       * MarkerStorage markerStore = markersHash.get(marker);
       * markerStore.setLatLng(marker.getLatLng());
       * saveMarkerStorage(markerStore);
       */
    }
  }

  static class MyMarkerClickHandler implements MarkerClickHandler {
    public void onClick(MarkerClickEvent event) {
      final Marker marker = event.getSender();
      /*
       * MarkerStorage markerStorage = markersHash.get(marker);
       * 
       * markersDataService.delete(markerStorage.getId(), new
       * AsyncCallback<Boolean>() { public void onFailure(Throwable caught) {
       * Window.alert(SERVER_ERROR); }
       * 
       * public void onSuccess(Boolean result) { if (result) {
       * markersHash.remove(marker); map.removeOverlay(marker);
       * showMarkersTable(); } } });
       */
    }
  }
}
