package by.bsuir.picasso.client.util;

import java.util.List;

import com.google.gwt.maps.client.geom.LatLng;

public class PolylineEncoder {
  public static PolylineEncoderResult createEncodings(List<LatLng> vertexs, int level, int step) {
    StringBuffer encodedPoints = new StringBuffer();
    StringBuffer encodedLevels = new StringBuffer();

    int plat = 0;
    int plng = 0;
    LatLng point;

    for (int i = 0; i < vertexs.size(); i += step) {
      point = vertexs.get(i);

      int late5 = floor1e5(point.getLatitude());
      int lnge5 = floor1e5(point.getLongitude());

      int dlat = late5 - plat;
      int dlng = lnge5 - plng;

      plat = late5;
      plng = lnge5;

      encodedPoints.append(encodeSignedNumber(dlat)).append(encodeSignedNumber(dlng));
      encodedLevels.append(encodeNumber(level));
    }

    PolylineEncoderResult result = new PolylineEncoderResult(encodedPoints.toString(), encodedLevels.toString());
    return result;
  }

  private static int floor1e5(double coordinate) {
    return (int) Math.floor(coordinate * 1e5);
  }

  private static String encodeSignedNumber(int num) {
    int sgn_num = num << 1;
    if (num < 0) {
      sgn_num = ~(sgn_num);
    }
    return (encodeNumber(sgn_num));
  }

  private static String encodeNumber(int num) {
    StringBuffer encodeString = new StringBuffer();

    while (num >= 0x20) {
      int nextValue = (0x20 | (num & 0x1f)) + 63;
      encodeString.append((char) (nextValue));
      num >>= 5;
    }

    num += 63;
    encodeString.append((char) (num));

    return encodeString.toString();
  }

}
