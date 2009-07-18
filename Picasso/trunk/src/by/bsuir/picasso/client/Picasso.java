package by.bsuir.picasso.client;

import java.util.HashMap;

import by.bsuir.picasso.client.service.LoginService;
import by.bsuir.picasso.client.service.LoginServiceAsync;
import by.bsuir.picasso.shared.LoginInfo;
import by.bsuir.picasso.shared.MarkerStorage;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HtmlContainer;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuBar;
import com.extjs.gxt.ui.client.widget.menu.MenuBarItem;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.menu.SeparatorMenuItem;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Picasso implements EntryPoint {
  private LoginInfo loginInfo = null;

  private HashMap<Marker, MarkerStorage> markersHash = new HashMap<Marker, MarkerStorage>();
  private MarkerStorage addedMarker = null;
  MapWidget map = null;

  public void onModuleLoad() {
    /*
      if (!Maps.isLoaded()) { com.google.gwt.user.client.Window.alert("The Maps API is not installed."
       + " The <script> tag that loads the Maps API may be missing or your Maps key may be wrong."); return; }
      
      if (!Maps.isBrowserCompatible()) {
        com.google.gwt.user.client.Window.alert("The Maps API is not compatible with this browser."); return;
      }
     */

    // Check login status using login service.
    LoginServiceAsync loginService = GWT.create(LoginService.class);
    loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
      public void onFailure(Throwable error) {
      }

      public void onSuccess(LoginInfo result) {
        loginInfo = result;
        if(loginInfo.isLoggedIn()) {
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
    signInLink.setHref(loginInfo.getLoginUrl());
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
    MenuBar bar = buildMenuBar();
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
    /*
     * map = new MapWidget(LatLng.newInstance(37.4419, -122.1419), 13);
     * map.setSize("250px", "150px"); map.addMapType(MapType.getHybridMap());
     * map.setCurrentMapType(MapType.getHybridMap()); center.add(map);
     * //center.addListener(eventType, listener);
     * //center.setMonitorWindowResize(true);
     */
    return center;
  }

  private MenuBar buildMenuBar() {
    Menu menu = new Menu();

    MenuItem item1 = new MenuItem("New");
    menu.add(item1);

    MenuItem item2 = new MenuItem("Open");
    menu.add(item2);

    menu.add(new SeparatorMenuItem());

    MenuItem item3 = new MenuItem("Log out");
    menu.add(item3);
    item3.addSelectionListener(new SelectionListener<MenuEvent>() {
      public void componentSelected(MenuEvent ce) {
        String logoutURL = loginInfo.getLogoutUrl();
        com.google.gwt.user.client.Window.Location.replace(logoutURL);
      }
    });

    MenuBar bar = new MenuBar();
    bar.setBorders(true);
    bar.setStyleAttribute("borderTop", "none");
    bar.add(new MenuBarItem("File", menu));

    Menu sub2 = new Menu();
    sub2.add(new MenuItem("Cut"));
    sub2.add(new MenuItem("Copy"));

    MenuBarItem edit = new MenuBarItem("Edit", sub2);
    bar.add(edit);
    return bar;
  }
}
