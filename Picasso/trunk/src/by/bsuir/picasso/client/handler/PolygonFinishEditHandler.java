package by.bsuir.picasso.client.handler;

import com.google.gwt.maps.client.event.PolygonClickHandler;
import com.google.gwt.maps.client.overlay.Polygon;

public class PolygonFinishEditHandler implements PolygonClickHandler {
  private PolygonEditHandler _polygonEditHandler;

  public PolygonFinishEditHandler(PolygonEditHandler polygonEditHandler) {
    _polygonEditHandler = polygonEditHandler;
  }

  @Override
  public void onClick(PolygonClickEvent e) {
    Polygon polygon = e.getSender();
    polygon.setEditingEnabled(false);
    polygon.removePolygonClickHandler(this);
    polygon.addPolygonClickHandler(_polygonEditHandler);
  }
}
