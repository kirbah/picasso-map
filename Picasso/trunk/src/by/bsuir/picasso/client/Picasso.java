package by.bsuir.picasso.client;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuBar;
import com.extjs.gxt.ui.client.widget.menu.MenuBarItem;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

public class Picasso implements EntryPoint {

  public void onModuleLoad() {
    ContentPanel west = new ContentPanel();
    west.setHeading("Markers");
    ContentPanel center = new ContentPanel();
    center.setHeading("Map");
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

  private MenuBar buildMenuBar() {
    Menu menu = new Menu();

    MenuItem item1 = new MenuItem("New");
    menu.add(item1);

    MenuItem item2 = new MenuItem("Open File");
    menu.add(item2);

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
