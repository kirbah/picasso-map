package by.bsuir.picasso.client;

import java.util.ArrayList;
import java.util.List;

import by.bsuir.picasso.client.data.ClientDataStorage;
import by.bsuir.picasso.client.data.MarkerModel;
import by.bsuir.picasso.client.data.PolyModel;
import by.bsuir.picasso.shared.MarkerStorage;
import by.bsuir.picasso.shared.PolyStorage;

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
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid.ClicksToEdit;
import com.extjs.gxt.ui.client.widget.layout.AccordionLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class MarkersPanelHelper {
  public static ContentPanel buildMarkersPanel(final ClientDataStorage cds) {
    final ContentPanel west = new ContentPanel();
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
    final EditorGrid<MarkerModel> gridMarkers = new EditorGrid<MarkerModel>(markersStore, cm);
    gridMarkers.setAutoExpandColumn("name");
    gridMarkers.setBorders(true);
    gridMarkers.setClicksToEdit(ClicksToEdit.TWO);

    final GridSelectionModel<MarkerModel> csm = new GridSelectionModel<MarkerModel>();
    gridMarkers.setSelectionModel(csm);
    cp.add(gridMarkers);

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

    final GridSelectionModel<PolyModel> csmPoly = new GridSelectionModel<PolyModel>();
    gridPoly.setSelectionModel(csmPoly);
    contentPoly.add(gridPoly);

    // Delete icon support
    itemDelete.addSelectionListener(new SelectionListener<ButtonEvent>() {
      public void componentSelected(ButtonEvent ce) {
        if (gridMarkers.isVisible()) {
          // Delete Marker
          List<MarkerModel> selected = gridMarkers.getSelectionModel().getSelectedItems();
          for (MarkerModel mm : selected) {
            MarkerStorage ms = mm.getMarkerStorage();
            cds.addDeletedMarkers(ms);
            cds.getMap().removeOverlay(mm.getMarker());
            cds.getMarkersStore().remove(mm);
          }
        } else if (gridPoly.isVisible()) {
          // Delete Polygon
          List<PolyModel> selected = gridPoly.getSelectionModel().getSelectedItems();
          for (PolyModel pm : selected) {
            PolyStorage ps = pm.getPolyStorage();
            cds.addDeletedPoly(ps);
            cds.getMap().removeOverlay(pm.getPolygon());
            cds.getPolygonStore().remove(pm);
          }
        }
      }
    });

    west.add(contentPoly);

    return west;
  }
}
