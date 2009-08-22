package by.bsuir.picasso.client.data;

import java.util.HashMap;

import by.bsuir.picasso.client.service.ServiceHelper;
import by.bsuir.picasso.shared.LoginInfo;
import by.bsuir.picasso.shared.MarkerStorage;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.overlay.Marker;

public class ClientDataStorage {
  private LoginInfo loginInfo = null;
  private ServiceHelper service = new ServiceHelper();

  private HashMap<Marker, MarkerStorage> markersHash = new HashMap<Marker, MarkerStorage>();
  private MarkerStorage addedMarker = null;
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

}
