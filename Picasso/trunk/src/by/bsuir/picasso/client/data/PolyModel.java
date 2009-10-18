package by.bsuir.picasso.client.data;

import by.bsuir.picasso.shared.PolyStorage;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.ChangeEvent;
import com.extjs.gxt.ui.client.data.PropertyChangeEvent;
import com.google.gwt.maps.client.overlay.Polygon;

public class PolyModel extends BaseModel {
  private Polygon _polygon;
  private PolyStorage _polyStorage;
  private ClientDataStorage _cds;

  public PolyModel(final ClientDataStorage cds, Polygon polygon, PolyStorage polyStorage) {
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
}
