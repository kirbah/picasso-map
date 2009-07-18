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

  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    
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

    final Window complex = new Window();   
    complex.setMaximizable(true);   
    complex.setHeading("Map Window");   
    complex.setWidth(200);   
    complex.setHeight(350);
    complex.setTopComponent(bar);

    HTML html = new HTML(
        "This is HTML.  It will be interpreted as such if you specify "
          + "the asHTML flag.", true);    
    
    complex.add(html);

/*
    ContentPanel panel = new ContentPanel();   
    panel.setHeading("MenuBar Example");   
    panel.setTopComponent(bar);
    //panel.setSize("auto", "auto");
    //panel.setAutoHeight(true);
    //panel.setDeferHeight(true);
    //panel.setBodyBorder(false);
*/
    
//    RootPanel.get().add(complex);
/*
    final Window points = new Window();   
    points.setMaximizable(true);   
    points.setHeading("Points Window");   
    points.setWidth(100);   
    points.setHeight(500);
//    RootPanel.get().add(points);
  */  
    
    ContentPanel west = new ContentPanel();   
    ContentPanel center = new ContentPanel();   
    ContentPanel south = new ContentPanel();   
    center.setHeading("BorderLayout Example");     
    
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
    //panel.setHeight(200);
    //panel.setAutoHeight(true);
    //panel.setDeferHeight(true);

    
    panel.setHeading("MenuBar Example");   
    panel.setTopComponent(bar);
    panel.setLayout(new BorderLayout());
    panel.add(west, westData);   
    panel.add(center, centerData);   
    panel.add(south, southData);

    Viewport viewport = new Viewport();
    viewport.setLayout(new BorderLayout());    
    viewport.add(panel, new BorderLayoutData(LayoutRegion.CENTER));
   // panel.layout();
    
    /*    
    //viewport.setAutoHeight(true);
    viewport.setLayout(new BorderLayout());

    viewport.add(west, westData);   
    viewport.add(center, centerData);   
    viewport.add(south, southData);
*/    
    RootPanel.get().add(viewport);
//    viewport.show();

//    complex.show();
//    points.show();
    
//    RootPanel.get().add(panel);
    
/*
    Window w = new Window();   
    w.setLayout(new FlowLayout(10)); 
    w.setHeading("Product Information");
    w.setModal(true);
    w.setSize(500, 300);
    w.setActive(true);
    w.setMaximizable(true);
    w.setResizable(true);
    //w.setEnabled(true);
    w.setToolTip("The ExtGWT product page...");
    //w.setUrl("http://www.extjs.com/products/gxt");
    Button b = new Button("Нажми меня...");
    w.add(b);
    panel.add(w);
    w.show();
*/
    
    /*
    Button b = new Button("Нажми меня...");
    SelectionListener<ButtonEvent> sl;

    sl = new SelectionListener<ButtonEvent>() {

      @Override
      public void componentSelected(ButtonEvent ce) {
        MessageBox.alert("Нажата кнопка", "Вы нажали кнопку", null);
      }

    };
    b.addSelectionListener(sl);
    RootPanel.get().add(b);
    */
  }

}
