package by.bsuir.picasso.client;

import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuBar;
import com.extjs.gxt.ui.client.widget.menu.MenuBarItem;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.menu.SeparatorMenuItem;

public class MenuHelper {
  public static MenuBar buildMenuBar(final ClientDataStorage cds) {
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
        String logoutURL = cds.getLoginInfo().getLogoutUrl();
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
