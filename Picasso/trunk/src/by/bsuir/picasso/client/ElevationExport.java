package by.bsuir.picasso.client;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;

public class ElevationExport {
  private static final int POINTS_PER_REQUEST = 20;
  private static final String WS_URL = "http://ws.geonames.org/";

  private static MessageBox theBox;
  private static TextArea theData;
  private static String theService;
  private static double theLat = 0.0;
  private static double theLng = 0.0;
  private static double theLngStart = 0.0;
  private static double theLatEnd = 0.0;
  private static double theLngEnd = 0.0;
  private static double theLatStep = 0.0;
  private static double theLngStep = 0.0;
  private static List<Double> thePointsLat;
  private static List<Double> thePointsLng;
  private static int thePointsToLoad;
  private static int theCurrentPoint;

  public static void begin(MessageBox box, TextArea dataArea, String service, double latStart, double lngStart, double latEnd, double lngEnd,
      double latStep, double lngStep) {
    theBox = box;
    theData = dataArea;
    theService = service;
    theLat = latStart;
    theLng = lngStart;
    theLngStart = lngStart;
    theLatEnd = latEnd;
    theLngEnd = lngEnd;
    theLatStep = latStep;
    theLngStep = lngStep;

    thePointsToLoad = ((int)((theLatEnd - theLat) / theLatStep) + 1) * ((int)((theLngEnd - theLng) / theLngStep) + 1);
    theCurrentPoint = 0;

    nextStep();
  }

  private static void nextStep() {
    double progress = ((double)theCurrentPoint) / ((double)thePointsToLoad);
    theBox.getProgressBar().updateProgress(progress, (int) (progress * 100) + Picasso.CONSTANTS.progressComplete());

    thePointsLat = new ArrayList<Double>();
    thePointsLng = new ArrayList<Double>();
    int i = 0;
    for (; theLat <= theLatEnd; theLat = round(theLat + theLatStep)) {
      for (; theLng <= theLngEnd; theLng = round(theLng + theLngStep)) {
        thePointsLat.add(theLat);
        thePointsLng.add(theLng);
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
    if (thePointsLat.size() > 0) {
      theCurrentPoint += thePointsLat.size();
      fetchData();
    } else {
      theBox.getProgressBar().updateProgress(1, 100 + Picasso.CONSTANTS.progressComplete());
      theBox.close();
    }
  }

  public static double round(double in) {
    int rounded = (int) (in * 10000.0);
    return ((double) rounded) / 10000.0;
  }

  private static void fetchData() {
    String url = WS_URL + theService + "?lats";
    boolean isFirst = true;
    for (Double lat : thePointsLat) {
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
    for (Double lng : thePointsLng) {
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
      builder.sendRequest(null, new RequestCallback() {
        public void onError(Request request, Throwable exception) {
          // Couldn't connect to server (could be timeout, SOP violation, etc.)
          com.google.gwt.user.client.Window.alert("Couldn't connect to WebService: " + exception.getMessage());
        }

        public void onResponseReceived(Request request, Response response) {
          if (200 == response.getStatusCode()) {
            // Process the response in response.getText()
            String result = response.getText();
            String[] resMeters = result.split("\r\n");
            String old = theData.getRawValue();
            String nvalue = "";
            int i = 0;
            for (String pointMeters : resMeters) {
              nvalue += thePointsLat.get(i) + ";" + thePointsLng.get(i) + ";" + pointMeters + "\r\n";
              i++;
            }
            theData.setRawValue(old + nvalue);

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
