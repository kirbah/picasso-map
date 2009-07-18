package by.bsuir.picasso.client;

import java.util.Collection;
import java.util.HashMap;

import by.bsuir.picasso.client.service.LoginService;
import by.bsuir.picasso.client.service.LoginServiceAsync;
import by.bsuir.picasso.client.service.MarkersDataService;
import by.bsuir.picasso.client.service.MarkersDataServiceAsync;
import by.bsuir.picasso.shared.LoginInfo;
import by.bsuir.picasso.shared.MarkerStorage;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.event.MarkerDragEndHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class CopyOfPicasso implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network connection and try again.";
	private LoginInfo loginInfo = null;
	/**
	 * Create a remote service proxy to talk to the server-side Maps service.
	 */
	private final MarkersDataServiceAsync markersDataService = GWT.create(MarkersDataService.class);

	private HashMap<Marker, MarkerStorage> markersHash = new HashMap<Marker, MarkerStorage>();
	private MarkerStorage addedMarker = null;
	MapWidget map = null;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
    // Check login status using login service.
    LoginServiceAsync loginService = GWT.create(LoginService.class);
    loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
      public void onFailure(Throwable error) {
      }

      public void onSuccess(LoginInfo result) {
        loginInfo = result;
        if(loginInfo.isLoggedIn()) {
          loadMapsList();
        } else {
          loadLogin();
        }
      }
    });
  }

  private void loadLogin() {
    // Assemble login panel.
    VerticalPanel loginPanel = new VerticalPanel();
    Label loginLabel = new Label("Please sign in to your Google Account to access the application.");
    Anchor signInLink = new Anchor("Sign In");
    signInLink.setHref(loginInfo.getLoginUrl());
    loginPanel.add(loginLabel);
    loginPanel.add(signInLink);
    RootPanel.get("top-line").add(loginPanel);
  }

  private void loadMapsList() {
    Anchor signOutLink = new Anchor("Sign Out");
    signOutLink.setHref(loginInfo.getLogoutUrl());
    VerticalPanel loginPanel = new VerticalPanel();
    loginPanel.add(signOutLink);
    RootPanel.get("top-line").add(loginPanel);
  }

  private void loadMapsListOLD() {
    if (!Maps.isLoaded()) {
			Window.alert("The Maps API is not installed."
					+ "  The <script> tag that loads the Maps API may be missing or your Maps key may be wrong.");
			return;
		}

		if (!Maps.isBrowserCompatible()) {
			Window.alert("The Maps API is not compatible with this browser.");
			return;
		}

		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setSpacing(10);

		final Button addMarkerButton = addMarkerButton();
		vPanel.add(addMarkerButton);

		map = new MapWidget(LatLng.newInstance(37.4419, -122.1419), 13);
		//map.setSize("500px", "300px");
		map.setSize("250px", "150px");
		//map.setUIToDefault();
		vPanel.add(map);
	    map.addMapType(MapType.getHybridMap());
	    map.setCurrentMapType(MapType.getHybridMap());

		DecoratorPanel decorator = new DecoratorPanel();
		decorator.add(vPanel);

		RootPanel.get("hm-map").add(decorator);

		loadMarkers();
	}

	class MyMarkerDragEndHandler implements MarkerDragEndHandler {
		public void onDragEnd(MarkerDragEndEvent event) {
			Marker marker = event.getSender();
			MarkerStorage markerStore = markersHash.get(marker);
			markerStore.setLatLng(marker.getLatLng());
			saveMarkerStorage(markerStore);
		}
	}
	class MyMarkerClickHandler implements MarkerClickHandler {
		public void onClick(MarkerClickEvent event) {
			final Marker marker = event.getSender();
			MarkerStorage markerStorage = markersHash.get(marker);

			markersDataService.delete(markerStorage.getId(), new AsyncCallback<Boolean>() {
				public void onFailure(Throwable caught) {
					Window.alert(SERVER_ERROR);
				}

				public void onSuccess(Boolean result) {
					if (result) {
						markersHash.remove(marker);
						map.removeOverlay(marker);
						showMarkersTable();
					}
				}
			});
		}
	}

	private void showMarkers(MarkerStorage[] markerStorage) {
		for (int j = 0; j < markerStorage.length; j++) {
			MarkerStorage markerStore = markerStorage[j];

			final Marker marker = createMarker(markerStore);

			markersHash.put(marker, markerStore);
			map.addOverlay(marker);
		}
	}

	private Marker createMarker(MarkerStorage markerStore) {
		MarkerOptions options = MarkerOptions.newInstance();
		options.setDraggable(true);
		options.setTitle(markerStore.getName());
		final Marker marker = new Marker(markerStore.getLatLng(), options);
		marker.addMarkerDragEndHandler(new MyMarkerDragEndHandler());
		marker.addMarkerClickHandler(new MyMarkerClickHandler());
		return marker;
	}

	private void showMarkersTable() {
		final FlexTable flexTable = new FlexTable();
		flexTable.setCellSpacing(3);
		flexTable.setCellPadding(2);
		flexTable.setWidget(0, 0, new HTML("<b>ID</b>"));
		flexTable.setWidget(0, 1, new HTML("<b>Name</b>"));
		flexTable.setWidget(0, 2, new HTML("<b>Latitude</b>"));
		flexTable.setWidget(0, 3, new HTML("<b>Longitude</b>"));

		Collection<MarkerStorage> markerStorage = markersHash.values();
		for (MarkerStorage marker : markerStorage) {
			int numRows = flexTable.getRowCount();
			flexTable.setWidget(numRows, 0, new HTML(marker.getId().toString()));
			flexTable.setWidget(numRows, 1, new HTML(marker.getName()));
			flexTable.setWidget(numRows, 2, new HTML(Double.toString(marker.getLatitude())));
			flexTable.setWidget(numRows, 3, new HTML(Double.toString(marker.getLongitude())));
		}

		RootPanel.get("test-markers").clear();
		RootPanel.get("test-markers").add(flexTable);
	}

	private void loadMarkers() {
		markersDataService.getMarkerStorageList(new AsyncCallback<MarkerStorage[]>() {
			public void onFailure(Throwable caught) {
				Window.alert(SERVER_ERROR);
			}

			public void onSuccess(MarkerStorage[] result) {
				showMarkers(result);
				showMarkersTable();
			}
		});
	}

	private Button addMarkerButton() {
		final Button addMarkerButton = new Button("Add Marker");

		// Create the popup dialog box
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("New marker creation");
		dialogBox.setAnimationEnabled(true);

		final VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");

		final HorizontalPanel dialogHPanelInput = new HorizontalPanel();
		dialogHPanelInput.setSpacing(3);
		dialogHPanelInput.add(new HTML("<b>Marker name:</b> "));
		final TextBox nameField = new TextBox();
		dialogHPanelInput.add(nameField);
		dialogVPanel.add(dialogHPanelInput);

		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);

		final HorizontalPanel dialogHPanelButtons = new HorizontalPanel();
		dialogHPanelButtons.setSpacing(3);
		final Button addButton = new Button("Add");
		dialogHPanelButtons.add(addButton);
		final Button closeButton = new Button("Close");
		dialogHPanelButtons.add(closeButton);
		dialogVPanel.add(dialogHPanelButtons);

		dialogBox.setWidget(dialogVPanel);

		addButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				addMarkerButton.setEnabled(true);
				addedMarker = new MarkerStorage();
				addedMarker.setName(nameField.getValue());
				nameField.setValue("");

				map.addMapClickHandler(new MapClickHandler() {
					public void onClick(MapClickEvent e) {
						map.removeMapClickHandler(this);
						MapWidget sender = e.getSender();
						// Overlay overlay = e.getOverlay();
						LatLng point = e.getLatLng();
						addedMarker.setLatLng(point);

						final Marker marker = createMarker(addedMarker);
						sender.addOverlay(marker);

						markersHash.put(marker, addedMarker);
						saveMarkerStorage(addedMarker);
						showMarkersTable();
					}
				});
			}
		});

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				addMarkerButton.setEnabled(true);
			}
		});

		class OpenAddMarkerDialogHandler implements ClickHandler {
			public void onClick(ClickEvent event) {
				addMarkerButton.setEnabled(false);
				dialogBox.center();
				nameField.setFocus(true);
			}
		}

		// Add a handler to send the name to the server
		OpenAddMarkerDialogHandler handler = new OpenAddMarkerDialogHandler();
		addMarkerButton.addClickHandler(handler);
		return addMarkerButton;
	}

	private void saveMarkerStorage(MarkerStorage marker) {
		final MarkerStorage markerTmp = marker;
		markersDataService.save(marker, new AsyncCallback<Long>() {
			public void onFailure(Throwable caught) {
				Window.alert(SERVER_ERROR);
			}

			public void onSuccess(Long result) {
				markerTmp.setId(result);
				showMarkersTable();
			}
		});
	}
}
