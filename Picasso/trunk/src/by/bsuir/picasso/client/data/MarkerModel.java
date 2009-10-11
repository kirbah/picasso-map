package by.bsuir.picasso.client.data;

import by.bsuir.picasso.shared.MarkerStorage;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.maps.client.overlay.Marker;

public class MarkerModel extends BaseModel {
  MarkerStorage _markerStorage;
  Marker _marker;

  public MarkerStorage getMarkerStorage() {
    _markerStorage.setName(getName());
    _markerStorage.setLatLng(_marker.getLatLng());
    return _markerStorage;
  }

  public MarkerModel() {

  }

  public MarkerModel(MarkerStorage markerStorage, Marker marker) {
    _markerStorage = markerStorage;
    _marker = marker;
    setName(markerStorage.getName());
  }

  public MarkerModel(String name) {
    setName(name);
  }

  public String getName() {
    return get("name");
  }

  public void setName(String name) {
    set("name", name);
  }
}
