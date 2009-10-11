package by.bsuir.picasso.client;

import java.util.ArrayList;
import java.util.List;

import by.bsuir.picasso.client.data.ClientDataStorage;
import by.bsuir.picasso.client.data.MarkerModel;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid.ClicksToEdit;
import com.extjs.gxt.ui.client.widget.layout.AccordionLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;

public class MarkersPanelHelper {
  public static ContentPanel buildMarkersPanel(final ClientDataStorage cds) {
    ContentPanel west = new ContentPanel();
    west.setHeading("Markers");
    west.setLayout(new AccordionLayout());

    ToolBar toolBar = new ToolBar();
    final Button item = new Button();
    item.setToolTip("New Marker");
    item.setIcon(Picasso.IMAGES.markerAdd());
    item.addSelectionListener(new SelectionListener<ButtonEvent>() {
      public void componentSelected(ButtonEvent ce) {
        ToolbarMarkerHelper.addMarkerButton(cds);
        //cds.getMarkersStore().add(new MarkerModel(8L, "name 8"));
      }
    });
    toolBar.add(item);

    Button itemPolyline = new Button();
    itemPolyline.setToolTip("New Polyline");
    itemPolyline.setIcon(Picasso.IMAGES.polilyneAdd());
    toolBar.add(itemPolyline);

    toolBar.add(new SeparatorToolItem());

    Button itemDelete = new Button();
    itemDelete.setToolTip("Delete");
    itemDelete.setIcon(Picasso.IMAGES.delete());
    toolBar.add(itemDelete);

    west.setTopComponent(toolBar);

    ContentPanel cp = new ContentPanel();
    cp.setHeading("Markers");
    cp.setLayout(new FitLayout());

    List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

    ColumnConfig column = new ColumnConfig();
    column.setId("name");
    column.setHeader("Name");
    column.setWidth(220);

    TextField<String> text = new TextField<String>();
    text.setAllowBlank(false);
    column.setEditor(new CellEditor(text));
    configs.add(column);

    ColumnModel cm = new ColumnModel(configs);

    ListStore<MarkerModel> markersStore = new ListStore<MarkerModel>();
    cds.setMarkersStore(markersStore);
    markersStore.add(new MarkerModel("name 1"));
    markersStore.add(new MarkerModel("Name 2"));
    markersStore.add(new MarkerModel("test 3"));

    final EditorGrid<MarkerModel> grid = new EditorGrid<MarkerModel>(markersStore, cm);
    grid.setAutoExpandColumn("name");
    grid.setBorders(true);
    grid.setClicksToEdit(ClicksToEdit.TWO);
    cp.add(grid);

    west.add(cp);

    ContentPanel cp2 = new ContentPanel();
    cp2.setHeading("Polylines");
    cp2.setLayout(new FitLayout());
    west.add(cp2);

    return west;
  }
}
