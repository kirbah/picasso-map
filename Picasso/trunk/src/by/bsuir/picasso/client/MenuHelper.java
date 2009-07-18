package by.bsuir.picasso.client;

import by.bsuir.picasso.client.service.MapsDataServiceAsync;
import by.bsuir.picasso.shared.MapInfo;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormButtonBinding;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuBar;
import com.extjs.gxt.ui.client.widget.menu.MenuBarItem;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.menu.SeparatorMenuItem;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class MenuHelper {
  public static MenuBar buildMenuBar(final ClientDataStorage cds) {
    Menu menu = new Menu();

    MenuItem item1 = new MenuItem("New");
    menu.add(item1);

    final Window window = createMapWindow(cds);

    item1.addSelectionListener(new SelectionListener<MenuEvent>() {
      public void componentSelected(MenuEvent ce) {
        window.show();
      }
    });

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

  private static Window createMapWindow(final ClientDataStorage cds) {
    final Window window = new Window();
    window.setSize(300, 200);
    window.setPlain(true);
    window.setModal(true);
    window.setBlinkModal(true);
    window.setHeading("New Map");
    window.setLayout(new FitLayout());

    FormPanel form = new FormPanel();
    form.setHeaderVisible(false);
    form.setWidth(150);

    final TextField<String> firstName = new TextField<String>();
    firstName.setFieldLabel("Name");
    firstName.setAllowBlank(false);
    FormData formData = new FormData("-20");
    form.add(firstName, formData);

    window.add(form);

    Button createBtn = new Button("Create");
    window.addButton(createBtn);
    createBtn.addSelectionListener(new SelectionListener<ButtonEvent>() {
      public void componentSelected(ButtonEvent ce) {
        MapsDataServiceAsync mapsDataService = cds.getService().getMapsDataService();
        final MapInfo mapInfo = new MapInfo();
        mapInfo.setName(firstName.getName());
        mapsDataService.save(mapInfo, new AsyncCallback<Long>() {
          public void onFailure(Throwable caught) {
          }

          public void onSuccess(Long result) {
            mapInfo.setMapId(result);
            
          }
        });        
        firstName.setValue("");
        window.hide();
      }
    });

    Button closeBtn = new Button("Close");
    closeBtn.addSelectionListener(new SelectionListener<ButtonEvent>() {
      public void componentSelected(ButtonEvent ce) {
        firstName.setValue("");
        window.hide();
      }
    });
    window.addButton(closeBtn);

    FormButtonBinding binding = new FormButtonBinding(form);
    binding.addButton(createBtn);

    return window;
  }
}
