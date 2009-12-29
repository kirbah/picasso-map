package by.bsuir.picasso.client;

import java.util.ArrayList;
import java.util.List;

import by.bsuir.picasso.client.data.ClientDataStorage;
import by.bsuir.picasso.client.data.MarkerModel;
import by.bsuir.picasso.client.data.PolyModel;

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
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class MarkersPanelHelper {
  public static ContentPanel buildMarkersPanel(final ClientDataStorage cds) {
    ContentPanel west = new ContentPanel();
    west.setHeading("Markers");
    west.setLayout(new AccordionLayout());

    ToolBar toolBar = new ToolBar();
    final Button item = new Button();
    item.setToolTip("New Marker");
    item.setIcon(AbstractImagePrototype.create(Picasso.IMAGES.markerAdd()));
    item.addSelectionListener(new SelectionListener<ButtonEvent>() {
      public void componentSelected(ButtonEvent ce) {
        ToolbarHelper.addMarkerButton(cds);
      }
    });
    toolBar.add(item);

    Button itemPolyline = new Button();
    itemPolyline.setToolTip("New Polygon");
    itemPolyline.setIcon(AbstractImagePrototype.create(Picasso.IMAGES.polilyneAdd()));
    itemPolyline.addSelectionListener(new SelectionListener<ButtonEvent>() {
      public void componentSelected(ButtonEvent ce) {
        ToolbarHelper.addPolygonButton(cds);
      }
    });
    toolBar.add(itemPolyline);

    toolBar.add(new SeparatorToolItem());

    Button itemDelete = new Button();
    itemDelete.setToolTip("Delete");
    itemDelete.setIcon(AbstractImagePrototype.create(Picasso.IMAGES.delete()));
    toolBar.add(itemDelete);

    west.setTopComponent(toolBar);

    // Markers
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

    ListStore<MarkerModel> markersStore = cds.getMarkersStore();
    final EditorGrid<MarkerModel> grid = new EditorGrid<MarkerModel>(markersStore, cm);
    grid.setAutoExpandColumn("name");
    grid.setBorders(true);
    grid.setClicksToEdit(ClicksToEdit.TWO);
    cp.add(grid);

    west.add(cp);

    // Polygons
    ContentPanel contentPoly = new ContentPanel();
    contentPoly.setHeading("Polygons");
    contentPoly.setLayout(new FitLayout());

    List<ColumnConfig> configsPoly = new ArrayList<ColumnConfig>();

    ColumnConfig columnPoly = new ColumnConfig();
    columnPoly.setId("name");
    columnPoly.setHeader("Name");
    columnPoly.setWidth(220);

    TextField<String> textPoly = new TextField<String>();
    text.setAllowBlank(false);
    columnPoly.setEditor(new CellEditor(textPoly));
    configsPoly.add(columnPoly);

    ColumnModel cmPoly = new ColumnModel(configsPoly);

    ListStore<PolyModel> polyStore = cds.getPolygonStore();
    final EditorGrid<PolyModel> gridPoly = new EditorGrid<PolyModel>(polyStore, cmPoly);
    gridPoly.setAutoExpandColumn("name");
    gridPoly.setBorders(true);
    gridPoly.setClicksToEdit(ClicksToEdit.TWO);
    contentPoly.add(gridPoly);
    
    west.add(contentPoly);

    return west;
  }
}
