package by.bsuir.picasso.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import by.bsuir.picasso.server.MapsDataServiceImpl;
import by.bsuir.picasso.server.MarkersDataServiceImpl;
import by.bsuir.picasso.server.PolyDataServiceImpl;
import by.bsuir.picasso.shared.MapInfo;
import by.bsuir.picasso.shared.MarkerStorage;
import by.bsuir.picasso.shared.PolyStorage;

@SuppressWarnings("serial")
public class DataExportServlet extends HttpServlet {
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    MapInfo mapInfo = (new MapsDataServiceImpl()).findOpenMap();
    String filename = mapInfo.getMapId() + mapInfo.getName().replaceAll("\\P{ASCII}+", "");

    resp.setCharacterEncoding("UTF-8");

    String format = req.getParameter("format");
    if ("csv".equals(format)) {
      exportToCSV(resp, mapInfo, filename);
    } else {
      exportToXML(resp, mapInfo, filename);
    }
  }

  private void exportToXML(HttpServletResponse resp, MapInfo mapInfo, String filename) throws IOException {
    PrintWriter pw = resp.getWriter();
    resp.setContentType("text/xml; charset=UTF-8");
    resp.setHeader("Content-disposition", "attachment; filename=" + filename + ".xml");

    pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    pw.println("<Map>");
    
    StringBuilder sb = new StringBuilder();
    sb.append(" <MapInfo name=\"");
    sb.append(mapInfo.getName());
    sb.append("\" create=\"");
    sb.append(mapInfo.getCreateDate());
    sb.append("\" update=\"");
    sb.append(mapInfo.getUpdateDate());
    sb.append("\" latitude=\"");
    sb.append(mapInfo.getLatitude());
    sb.append("\" longitude=\"");
    sb.append(mapInfo.getLongitude());
    sb.append("\" />");
    pw.println(sb.toString());

    pw.println(" <Markers>");
    MarkersDataServiceImpl mservice = new MarkersDataServiceImpl();
    for (MarkerStorage ms : mservice.getMarkerStorageList()) {
      StringBuilder sbm = new StringBuilder();
      sbm.append("  <Marker name=\"");
      sbm.append(ms.getName());
      sbm.append("\" latitude=\"");
      sbm.append(ms.getLatitude());
      sbm.append("\" longitude=\"");
      sbm.append(ms.getLongitude());
      sbm.append("\" />");
      pw.println(sbm.toString());
    }
    pw.println(" </Markers>");

    pw.println("</Map>");
  }

  private void exportToCSV(HttpServletResponse resp, MapInfo mapInfo, String filename) throws IOException {
    PrintWriter pw = resp.getWriter();
    resp.setContentType("application/csv; charset=UTF-8");
    resp.setHeader("Content-disposition", "attachment; filename=" + filename + ".csv");

    StringBuilder sb = new StringBuilder();
    sb.append("MapInfo;");
    sb.append(mapInfo.getName());
    sb.append(";");
    sb.append(mapInfo.getCreateDate());
    sb.append(";");
    sb.append(mapInfo.getUpdateDate());
    sb.append(";");
    sb.append(mapInfo.getLatitude());
    sb.append(";");
    sb.append(mapInfo.getLongitude());
    pw.println(sb.toString());

    MarkersDataServiceImpl mservice = new MarkersDataServiceImpl();
    for (MarkerStorage ms : mservice.getMarkerStorageList()) {
      StringBuilder sbm = new StringBuilder();
      sbm.append("Marker;");
      sbm.append(ms.getName());
      sbm.append(";");
      sbm.append(ms.getLatitude());
      sbm.append(";");
      sbm.append(ms.getLongitude());
      pw.println(sbm.toString());
    }

    PolyDataServiceImpl pservice = new PolyDataServiceImpl();
    for (PolyStorage ps : pservice.getPolyList()) {
      StringBuilder sbp = new StringBuilder();
      sbp.append(ps.getType());
      sbp.append(";");
      sbp.append(ps.getName());
      sbp.append(";");
      sbp.append(ps.getZoomLevel());
      sbp.append(";");
      pw.println(sbp.toString());
    }
  }
}
