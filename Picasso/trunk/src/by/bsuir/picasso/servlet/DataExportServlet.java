package by.bsuir.picasso.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import by.bsuir.picasso.server.MapsDataServiceImpl;
import by.bsuir.picasso.shared.MapInfo;

@SuppressWarnings("serial")
public class DataExportServlet extends HttpServlet {
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    MapInfo mapInfo = (new MapsDataServiceImpl()).findOpenMap();
    String filename = mapInfo.getMapId() + mapInfo.getName().replaceAll("\\P{ASCII}+", "");

    String format = req.getParameter("format");
    if ("cvs".equals(format)) {
      resp.setContentType("application/csv");

      resp.setHeader("Content-disposition", "attachment; filename=" + filename + ".cvs");

      PrintWriter pw = resp.getWriter();
      pw.println("Hello, world");
    } else {
      resp.setContentType("text/xml");

      resp.setHeader("Content-disposition", "attachment; filename=" + filename + ".xml");

      PrintWriter pw = resp.getWriter();
      pw.println("Hello, world");
    }
  }
}
