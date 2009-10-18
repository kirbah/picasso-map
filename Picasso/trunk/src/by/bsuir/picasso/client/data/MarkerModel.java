package by.bsuir.picasso.client.data;

import by.bsuir.picasso.client.MapHelper;
import by.bsuir.picasso.shared.MarkerStorage;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.ChangeEvent;
import com.extjs.gxt.ui.client.data.PropertyChangeEvent;
import com.google.gwt.maps.client.overlay.Marker;

public class MarkerModel extends BaseModel {
  private MarkerStorage _markerStorage;
  private Marker _marker;
  public Marker getMarker() {
    return _marker;
  }

  private ClientDataStorage _cds;

  public MarkerStorage getMarkerStorage() {
    if (_marker != null) {
      _markerStorage.setLatitude(_marker.getLatLng().getLatitude());
      _markerStorage.setLongitude(_marker.getLatLng().getLongitude());
    }
    return _markerStorage;
  }

  public MarkerModel(final ClientDataStorage cds, MarkerStorage markerStorage, Marker marker) {
    _markerStorage = markerStorage;
    _marker = marker;
    _cds = cds;
    setName(markerStorage.getName());
  }

  @Override
  public void notify(ChangeEvent evt) {
    super.notify(evt);

    PropertyChangeEvent e = (PropertyChangeEvent) evt;
    if (e.getName().equals("name")) {
      String name = (String) e.getNewValue();
      _markerStorage.setName(name);

      // Update marker on the Map
      Marker marker = MapHelper.createMarker(getMarkerStorage());
      if (_marker != null) {
        _cds.getMap().removeOverlay(_marker);
      }
      _marker = marker;
      _cds.getMap().addOverlay(_marker);
    }
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
