package by.bsuir.picasso.client;

import by.bsuir.picasso.client.data.ClientDataStorage;
import by.bsuir.picasso.client.service.MapsDataServiceAsync;
import by.bsuir.picasso.shared.MapInfo;

import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.geom.LatLng;
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
    cds.getMapContentPanel().setHeading(mapInfo.getName());
    MapWidget map = cds.getMap();
    map.setCenter(LatLng.newInstance(mapInfo.getLatitude(), mapInfo.getLongitude()));
    map.setZoomLevel(mapInfo.getZoomLevel());
  }

}
