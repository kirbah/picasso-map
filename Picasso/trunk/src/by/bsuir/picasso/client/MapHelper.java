package by.bsuir.picasso.client;

import by.bsuir.picasso.client.data.ClientDataStorage;
import by.bsuir.picasso.client.data.MarkerModel;
import by.bsuir.picasso.client.service.MapsDataServiceAsync;
import by.bsuir.picasso.client.service.MarkersDataServiceAsync;
import by.bsuir.picasso.shared.MapInfo;
import by.bsuir.picasso.shared.MarkerStorage;

import com.extjs.gxt.ui.client.store.ListStore;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.user.client.rpc.AsyncCallback;


public class MapHelper {

  public static void openMap(final ClientDataStorage cds, final Long mapId) {
    MapsDataServiceAsync mapsDataService = cds.getService().getMapsDataService();
    mapsDataService.openMap(mapId, new AsyncCallback<MapInfo>() {
      public void onFailure(Throwable caught) {
      }

      public void onSuccess(MapInfo mapInfo) {
        if (mapInfo != null) {
          showMap(cds, mapInfo);
        }
      }
    });
  }

  public static void showMap(final ClientDataStorage cds, MapInfo mapInfo) {
    if (mapInfo != null) {
      cds.getMapContentPanel().setHeading(mapInfo.getName());
      final MapWidget map = cds.getMap();
      map.setVisible(true);
      map.setCenter(LatLng.newInstance(mapInfo.getLatitude(), mapInfo.getLongitude()));
      map.setZoomLevel(mapInfo.getZoomLevel());

      // Cleanup markers on the Map
      for (MarkerModel markerModel : cds.getMarkersStore().getModels()) {
        map.removeOverlay(markerModel.getMarker());
      }
      cds.getMarkersStore().removeAll();

      // Load markers for current map
      MarkersDataServiceAsync markersDataService = cds.getService().getMarkersDataService();
      markersDataService.getMarkerStorageList(new AsyncCallback<MarkerStorage[]>() {
        public void onFailure(Throwable caught) {
        }

        public void onSuccess(MarkerStorage[] result) {
          ListStore<MarkerModel> ms = cds.getMarkersStore();
          for (int i = 0; i < result.length; i++) {
            MarkerStorage markerStorage = result[i];
            ms.add(new MarkerModel(cds, markerStorage, null));
          }
        }
      });
    } else {
      MapWidget map = cds.getMap();
      map.setVisible(false);
    }
  }

  public static Marker createMarker(MarkerStorage markerStore) {
    MarkerOptions options = MarkerOptions.newInstance();
    options.setDraggable(true);
    options.setTitle(markerStore.getName());
    LatLng latLng = LatLng.newInstance(markerStore.getLatitude(), markerStore.getLongitude());
    final Marker marker = new Marker(latLng, options);
    return marker;
  }

}
