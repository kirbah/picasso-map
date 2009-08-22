package by.bsuir.picasso.client.service;

import by.bsuir.picasso.shared.MapInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MapsDataServiceAsync {
  void getMapsList(AsyncCallback<MapInfo[]> callback);

  void save(MapInfo mapInfo, AsyncCallback<Long> callback);

  void delete(Long id, AsyncCallback<Boolean> callback);

  void delete(Long[] ids, AsyncCallback<Boolean> callback);

  void findOpenMap(AsyncCallback<MapInfo> callback);

  void openMap(Long mapId, AsyncCallback<MapInfo> callback);
}
