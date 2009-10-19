package by.bsuir.picasso.client.data;

import java.util.ArrayList;
import java.util.List;

import by.bsuir.picasso.client.util.PolylineEncoder;
import by.bsuir.picasso.client.util.PolylineEncoderResult;
import by.bsuir.picasso.shared.PolyStorage;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.ChangeEvent;
import com.extjs.gxt.ui.client.data.PropertyChangeEvent;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Polygon;

public class PolyModel extends BaseModel {
  private Polygon _polygon;
  private PolyStorage _polyStorage;
  private ClientDataStorage _cds;
  private boolean _isChanged = false;

  public PolyModel(final ClientDataStorage cds, PolyStorage polyStorage, Polygon polygon) {
    _polygon = polygon;
    _cds = cds;
    _polyStorage = polyStorage;
    setName(polyStorage.getName());
  }

  @Override
  public void notify(ChangeEvent evt) {
    super.notify(evt);

    PropertyChangeEvent e = (PropertyChangeEvent) evt;
    if (e.getName().equals("name")) {
      String name = (String) e.getNewValue();
    }
  }

  public String getName() {
    return get("name");
  }

  public void setName(String name) {
    set("name", name);
  }

  public Polygon getPolygon() {
    return _polygon;
  }

  public PolyStorage getPolyStorage() {
    if (_polygon != null) {
      // Encode new or changed points in the String for storage
      if (_isChanged || _polyStorage.getPoints() == null || _polyStorage.getPoints().length() == 0) {
        List<LatLng> vertexs = new ArrayList<LatLng>(_polygon.getVertexCount());
        for (int i = 0; i < _polygon.getVertexCount(); i++) {
          LatLng point = _polygon.getVertex(i);
          vertexs.add(point);
        }
        PolylineEncoderResult result = PolylineEncoder.createEncodings(vertexs, _polyStorage.getZoomLevel(), 1);
        _polyStorage.setPoints(result.getPoints());
        _polyStorage.setLevels(result.getLevels());
      }
    }
    return _polyStorage;
  }

  public void markChanged() {
    _isChanged = true;
  }
}
