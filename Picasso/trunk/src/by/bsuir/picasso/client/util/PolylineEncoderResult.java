package by.bsuir.picasso.client.util;

public class PolylineEncoderResult {
  public String _points;
  public String _levels;

  public PolylineEncoderResult(String points, String levels) {
    _points = points;
    _levels = levels;
  }

  public String getPoints() {
    return _points;
  }

  public String getLevels() {
    return _levels;
  }
}