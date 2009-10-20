package by.bsuir.picasso.client.handler;

import by.bsuir.picasso.client.data.PolyModel;

import com.google.gwt.maps.client.event.PolygonClickHandler;
import com.google.gwt.maps.client.overlay.PolyEditingOptions;
import com.google.gwt.maps.client.overlay.Polygon;

public class PolygonEditHandler implements PolygonClickHandler {
  private PolyModel _polyModel;
  private PolygonClickHandler _cancelEditHandler = null;

  public PolygonEditHandler(PolyModel polyModel) {
    _polyModel = polyModel;
  }

  @Override
  public void onClick(PolygonClickEvent e) {
    Polygon polygon = e.getSender();
    polygon.setEditingEnabled(PolyEditingOptions.newInstance(20));
    _polyModel.markChanged();
    if (_cancelEditHandler == null) {
      _cancelEditHandler = new PolygonFinishEditHandler(this);
    }
    polygon.removePolygonClickHandler(this);
    polygon.addPolygonClickHandler(_cancelEditHandler);
  }
}
