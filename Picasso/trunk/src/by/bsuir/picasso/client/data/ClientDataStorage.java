package by.bsuir.picasso.client.data;

import java.util.ArrayList;
import java.util.List;

import by.bsuir.picasso.client.service.ServiceHelper;
import by.bsuir.picasso.shared.LoginInfo;
import by.bsuir.picasso.shared.MarkerStorage;
import by.bsuir.picasso.shared.PolyStorage;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.google.gwt.maps.client.MapWidget;

public class ClientDataStorage {
  private LoginInfo loginInfo = null;
  private ServiceHelper service = new ServiceHelper();

  ListStore<MarkerModel> markersStore = new ListStore<MarkerModel>();
  ListStore<PolyModel> polygonStore = new ListStore<PolyModel>();
  MapWidget map = null;
  ContentPanel mapContentPanel = null;
  List<Long> deletedMarkersId = new ArrayList<Long>();
  List<Long> deletedPolyId = new ArrayList<Long>();

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

  public ListStore<PolyModel> getPolygonStore() {
    return polygonStore;
  }

  public void addDeletedMarkers(MarkerStorage markerStorage) {
    if (markerStorage.getId() > 0) {
      deletedMarkersId.add(markerStorage.getId());
    }
  }

  public void addDeletedPoly(PolyStorage polyStorage) {
    if (polyStorage.getId() > 0) {
      deletedPolyId.add(polyStorage.getId());
    }
  }

  public List<Long> getDeletedMarkersId() {
    return deletedMarkersId;
  }

  public List<Long> getDeletedPolyId() {
    return deletedPolyId;
  }

}
