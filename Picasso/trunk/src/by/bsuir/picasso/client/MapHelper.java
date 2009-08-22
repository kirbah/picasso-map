package by.bsuir.picasso.client;

import by.bsuir.picasso.client.service.MapsDataServiceAsync;
import by.bsuir.picasso.shared.MapInfo;

import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.user.client.rpc.AsyncCallback;


public class MapHelper {

  public static void openMap(final ClientDataStorage cds, final Long mapId) {
    MapsDataServiceAsync mapsDataService = cds.getService().getMapsDataService();
    mapsDataService.openMap(mapId, new AsyncCallback<MapInfo>() {
      public void onFailure(Throwable caught) {
      }

      public void onSuccess(MapInfo mapInfo) {
        // TODO show map
        if (mapInfo != null) {
          MapWidget map = cds.getMap();
          map.setCenter(mapInfo.getCenter());
          map.setZoomLevel(mapInfo.getZoomLevel());

        }
      }
    });

  }
  
}
