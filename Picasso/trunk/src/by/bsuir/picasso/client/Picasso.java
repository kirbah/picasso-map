package by.bsuir.picasso.client;

import by.bsuir.picasso.client.service.LoginServiceAsync;
import by.bsuir.picasso.shared.LoginInfo;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.menu.MenuBar;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Picasso implements EntryPoint {
  private final ClientDataStorage cds = new ClientDataStorage();

  public void onModuleLoad() {
    if (!Maps.isLoaded()) {
     com.google.gwt.user.client.Window.alert("The Maps API is not installed."
     + " The <script> tag that loads the Maps API may be missing or your Maps key may be wrong."); return; }

    if (!Maps.isBrowserCompatible()) {
      com.google.gwt.user.client.Window.alert("The Maps API is not compatible with this browser."); return;
    }

    // Check login status using login service.
    LoginServiceAsync loginService = cds.getService().getLoginService();
    loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
      public void onFailure(Throwable error) {
      }

      public void onSuccess(LoginInfo result) {
        cds.setLoginInfo(result);
        if(result.isLoggedIn()) {
          loadApplicationUI();
        } else {
          loadLoginUI();
        }
      }
    });
  }  

  private void loadLoginUI() {
    // Assemble login panel.
    VerticalPanel loginPanel = new VerticalPanel();
    Label loginLabel = new Label("Please sign in to your Google Account to access the application.");
    Anchor signInLink = new Anchor("Sign In");
    signInLink.setHref(cds.getLoginInfo().getLoginUrl());
    loginPanel.add(loginLabel);
    loginPanel.add(signInLink);
    RootPanel.get().add(loginPanel);
  }

  public void loadApplicationUI() {
    ContentPanel west = new ContentPanel();
    west.setHeading("Markers");
    ContentPanel center = buildMapPanel();
    ContentPanel south = new ContentPanel();
    south.setHeading("Properties");

    BorderLayoutData westData = new BorderLayoutData(LayoutRegion.WEST, 150);
    westData.setSplit(true);
    westData.setCollapsible(true);
    westData.setMargins(new Margins(5));

    BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);
    centerData.setMargins(new Margins(5, 5, 5, 0));

    BorderLayoutData southData = new BorderLayoutData(LayoutRegion.SOUTH, 100);
    southData.setSplit(true);
    southData.setCollapsible(true);
    southData.setFloatable(true);
    southData.setMargins(new Margins(0, 5, 5, 5));

    ContentPanel panel = new ContentPanel();
    panel.setHeading("Picasso the world!");
    MenuBar bar = MenuHelper.buildMenuBar(cds);
    panel.setTopComponent(bar);
    panel.setLayout(new BorderLayout());
    panel.add(west, westData);
    panel.add(center, centerData);
    panel.add(south, southData);

    Viewport viewport = new Viewport();
    viewport.setLayout(new BorderLayout());
    viewport.add(panel, new BorderLayoutData(LayoutRegion.CENTER));
    RootPanel.get().add(viewport);
  }

  private ContentPanel buildMapPanel() {
    ContentPanel center = new ContentPanel();
    center.setHeading("Map");

    MapWidget map = new MapWidget(LatLng.newInstance(37.4419, -122.1419), 13);
    cds.setMap(map);

    map.setSize("250px", "150px");
    map.addMapType(MapType.getHybridMap());
    map.setCurrentMapType(MapType.getHybridMap());
    center.add(map);
     //center.addListener(eventType, listener);
     //center.setMonitorWindowResize(true);

    return center;
  }

}
