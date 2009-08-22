package by.bsuir.picasso.client;

import java.util.ArrayList;
import java.util.List;

import by.bsuir.picasso.client.data.MapModel;
import by.bsuir.picasso.client.service.MapsDataServiceAsync;
import by.bsuir.picasso.shared.MapInfo;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormButtonBinding;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuBar;
import com.extjs.gxt.ui.client.widget.menu.MenuBarItem;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.menu.SeparatorMenuItem;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class MenuHelper {
  public static MenuBar buildMenuBar(final ClientDataStorage cds) {
    Menu menu = new Menu();

    MenuItem item1 = new MenuItem("New");
    menu.add(item1);

    final Window createMapWindow = createMapWindow(cds);
    item1.addSelectionListener(new SelectionListener<MenuEvent>() {
      public void componentSelected(MenuEvent ce) {
        createMapWindow.show();
      }
    });

    MenuItem item2 = new MenuItem("Open");
    menu.add(item2);

    item2.addSelectionListener(new SelectionListener<MenuEvent>() {
      public void componentSelected(MenuEvent ce) {
        openMapWindow(cds);
      }
    });

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

  private static void openMapWindow(final ClientDataStorage cds) {
    MapsDataServiceAsync mapsDataService = cds.getService().getMapsDataService();
    mapsDataService.getMapsList(new AsyncCallback<MapInfo[]>() {
      public void onFailure(Throwable caught) {
      }

      public void onSuccess(MapInfo[] mapInfo) {
        showOpenMapWindow(cds, mapInfo);
      }
    });
  }

  private static Window showOpenMapWindow(final ClientDataStorage cds, final MapInfo[] mapInfo) {
    final Window window = new Window();
    window.setSize(300, 200);
    window.setPlain(true);
    window.setModal(true);
    window.setBlinkModal(true);
    window.setHeading("Open Map");
    window.setLayout(new FitLayout());

    List<MapModel> mapModel = new ArrayList<MapModel>();
    for (MapInfo mi : mapInfo) {
      mapModel.add(new MapModel(mi.getMapId(), mi.getName(), mi.getUpdateDate()));
    }

    List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

    final CheckBoxSelectionModel<MapModel> sm = new CheckBoxSelectionModel<MapModel>();
    configs.add(sm.getColumn());

    ColumnConfig column = new ColumnConfig("name", "Map Name", 200);
    configs.add(column);

    column = new ColumnConfig("updateDate", "Last Updated", 100);
    column.setAlignment(HorizontalAlignment.RIGHT);
    column.setDateTimeFormat(DateTimeFormat.getShortDateTimeFormat());
    configs.add(column);

    ColumnModel cm = new ColumnModel(configs);

    ContentPanel cp = new ContentPanel();
    cp.setBodyBorder(false);
    // cp.setIcon(Examples.ICONS.table());
    // cp.setHeading("Basic Grid");
    cp.setHeaderVisible(false);
    // cp.setButtonAlign(HorizontalAlignment.CENTER);
    cp.setLayout(new FitLayout());
    // cp.setSize(600, 300);

    ListStore<MapModel> mapStore = new ListStore<MapModel>();
    mapStore.add(mapModel);

    final Grid<MapModel> grid = new Grid<MapModel>(mapStore, cm);
    grid.setSelectionModel(sm);
    grid.setStyleAttribute("borderTop", "none");
    grid.setAutoExpandColumn("name");
    grid.setBorders(true);
    grid.setStripeRows(true);
    grid.addPlugin(sm);

    window.add(grid);

    Button openBtn = new Button("Open");
    window.addButton(openBtn);
    openBtn.addSelectionListener(new SelectionListener<ButtonEvent>() {
      public void componentSelected(ButtonEvent ce) {
        List<MapModel> selected = sm.getSelectedItems();
        if (selected.size() > 0) {
          Long mapId = selected.get(0).getMapId();
          window.hide();
          MapHelper.openMap(cds, mapId);
        }
      }
    });

    Button deleteBtn = new Button("Delete");
    window.addButton(deleteBtn);
    deleteBtn.addSelectionListener(new SelectionListener<ButtonEvent>() {
      public void componentSelected(ButtonEvent ce) {
        final List<MapModel> selected = sm.getSelectedItems();
        if (selected.size() > 0) {
          MapsDataServiceAsync mapsDataService = cds.getService().getMapsDataService();
          List<Long> selectedIds = new ArrayList<Long>();
          for (MapModel selectedModel : selected) {
            selectedIds.add(selectedModel.getMapId());
          }
          mapsDataService.delete(selectedIds.toArray(new Long[0]), new AsyncCallback<Boolean>() {
            public void onFailure(Throwable caught) {
            }

            public void onSuccess(Boolean result) {
              for (MapModel selectedModel : selected) {
                grid.getStore().remove(selectedModel);
              }
            }
          });
        }
      }
    });

    Button closeBtn = new Button("Close");
    closeBtn.addSelectionListener(new SelectionListener<ButtonEvent>() {
      public void componentSelected(ButtonEvent ce) {
        window.hide();
      }
    });
    window.addButton(closeBtn);

    window.show();

    return window;
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
        mapInfo.setName(firstName.getValue());
        mapInfo.setLatitude(37);
        mapInfo.setLongitude(-122);
        mapInfo.setZoomLevel(10);
        mapsDataService.save(mapInfo, new AsyncCallback<Long>() {
          public void onFailure(Throwable caught) {
          }

          public void onSuccess(Long result) {
            window.hide();
            MapHelper.openMap(cds, result);
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
