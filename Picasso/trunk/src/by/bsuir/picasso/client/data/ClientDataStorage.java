package by.bsuir.picasso.client.data;

import by.bsuir.picasso.client.service.ServiceHelper;
import by.bsuir.picasso.shared.LoginInfo;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.google.gwt.maps.client.MapWidget;

public class ClientDataStorage {
  private LoginInfo loginInfo = null;
  private ServiceHelper service = new ServiceHelper();

  ListStore<MarkerModel> markersStore = null;   
  MapWidget map = null;
  ContentPanel mapContentPanel = null;

  public ContentPanel getMapContentPanel() {
    return mapContentPanel;
  }

  public void setMapContentPanel(ContentPanel mapContentPanel) {
    this.mapContentPanel = mapContentPanel;
  }

  public LoginInfo getLoginInfo() {
    return loginInfo;
  }

  public void setLoginInfo(LoginInfo loginInfo) {
    this.loginInfo = loginInfo;
  }

  public MapWidget getMap() {
    return map;
  }

  public void setMap(MapWidget map) {
    this.map = map;
  }

  public ServiceHelper getService() {
    return service;
  }

  public ListStore<MarkerModel> getMarkersStore() {
    return markersStore;
  }

  public void setMarkersStore(ListStore<MarkerModel> markersStore) {
    this.markersStore = markersStore;
  }

}
