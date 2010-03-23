package by.bsuir.picasso.client;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;

public class ElevationExport {
  private static final int POINTS_PER_REQUEST = 3;
  private static final String WS_URL = "http://ws.geonames.org/";

  private static TextArea theDesription;
  private static double theLat = 0.0;
  private static double theLng = 0.0;
  private static double theLngStart = 0.0;
  private static double theLatEnd = 0.0;
  private static double theLngEnd = 0.0;
  private static double theLatStep = 0.0;
  private static double theLngStep = 0.0;
  private static List<Double> _pointsLat;
  private static List<Double> _pointsLng;

  public static void begin(TextArea description, double latStart, double lngStart, double latEnd, double lngEnd,
      double latStep, double lngStep) {
    theDesription = description;
    theLat = latStart;
    theLng = lngStart;
    theLngStart = lngStart;
    theLatEnd = latEnd;
    theLngEnd = lngEnd;
    theLatStep = latStep;
    theLngStep = lngStep;

    nextStep();
  }

  private static void nextStep() {
    _pointsLat = new ArrayList<Double>();
    _pointsLng = new ArrayList<Double>();
    int i = 0;
    for (; theLat <= theLatEnd; theLat = round(theLat + theLatStep)) {
      for (; theLng <= theLngEnd; theLng = round(theLng + theLngStep)) {
        _pointsLat.add(theLat);
        _pointsLng.add(theLng);
        i++;
        if (i == POINTS_PER_REQUEST) {
          theLng = round(theLng + theLngStep);
          break;
        }
      }
      if (i == POINTS_PER_REQUEST) {
        break;
      }
      theLng = theLngStart;
    }
    if (_pointsLat.size() > 0) {
      fetchData();
    }
  }

  public static double round(double in) {
    int rounded = (int) (in * 10000.0);
    return ((double) rounded) / 10000.0;
  }

  private static void fetchData() {
    String url = WS_URL + "astergdem?lats";
    boolean isFirst = true;
    for (Double lat : _pointsLat) {
      if (isFirst) {
        url += "=";
        isFirst = false;
      } else {
        url += ",";
      }
      url += lat;
    }
    url += "&lngs";
    isFirst = true;
    for (Double lng : _pointsLng) {
      if (isFirst) {
        url += "=";
        isFirst = false;
      } else {
        url += ",";
      }
      url += lng;
    }
    RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(url));

    try {
      Request request = builder.sendRequest(null, new RequestCallback() {
        public void onError(Request request, Throwable exception) {
          // Couldn't connect to server (could be timeout, SOP violation, etc.)
          com.google.gwt.user.client.Window.alert("Couldn't connect to WebService: " + exception.getMessage());
        }

        public void onResponseReceived(Request request, Response response) {
          if (200 == response.getStatusCode()) {
            // Process the response in response.getText()
            String result = response.getText();
            // com.google.gwt.user.client.Window.alert(result);
            String[] resMeters = result.split("\r\n");
            String old = theDesription.getRawValue();
            String nvalue = "";
            int i = 0;
            for (String pointMeters : resMeters) {
              nvalue += _pointsLat.get(i) + ";" + _pointsLng.get(i) + ";" + pointMeters + "\r\n";
              i++;
            }
            theDesription.setRawValue(old + nvalue);

            nextStep();
          } else {
            // Handle the error. Can get the status text from
            // response.getStatusText()
            com.google.gwt.user.client.Window.alert("Error: " + response.getStatusText());
          }
        }
      });
    } catch (RequestException e) {
      // Couldn't connect to server
      com.google.gwt.user.client.Window.alert("WebService connection error: " + e.getMessage());
    }
  }

}
