package by.bsuir.picasso.server.util;

public class Vertex {
  double _latitude;
  double _longitude;

  public Vertex(double latitude, double longitude) {
    _latitude = latitude;
    _longitude = longitude;
  }

  public double getLatitude() {
    return _latitude;
  }

  public void setLatitude(double latitude) {
    _latitude = latitude;
  }

  public double getLongitude() {
    return _longitude;
  }

  public void setLongitude(double longitude) {
    _longitude = longitude;
  }
}
