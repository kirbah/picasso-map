package by.bsuir.picasso.client;

import by.bsuir.picasso.shared.MarkerStorage;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Picasso implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Maps service.
	 */
	private final MapsDataServiceAsync mapsDataService = GWT
			.create(MapsDataService.class);

	private MarkerStorage[] markerStorage = null;

	private MarkerStorage addedMarker = null; 
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setSpacing(10);

		final Button addMarkerButton = addMarkerButton();
		vPanel.add(addMarkerButton);

		final Button saveMarkerButton = new Button("save");
		
		class saveMarkerHandler implements ClickHandler {
			public void onClick(ClickEvent event) {
				if (addedMarker != null) {
					mapsDataService.persistMarkerStorage(addedMarker, new AsyncCallback<Long>() {
						public void onFailure(Throwable caught) {
							Window.alert(SERVER_ERROR);
						}

						public void onSuccess(Long result) {
							loadMarkers();
						}
					});
				}
			}
		}		
		saveMarkerButton.addClickHandler(new saveMarkerHandler());

		vPanel.add(saveMarkerButton);

		DecoratorPanel decorator = new DecoratorPanel();
		decorator.add(vPanel);

		RootPanel.get("hm-map").add(decorator);

		loadMarkers();
	}

	private void listMarkers() {
		final FlexTable flexTable = new FlexTable();
		flexTable.setCellSpacing(3);
		flexTable.setCellPadding(2);
		flexTable.setWidget(0,0, new HTML("<b>ID</b>"));
		flexTable.setWidget(0,1, new HTML("<b>Name</b>"));
		flexTable.setWidget(0,2, new HTML("<b>Latitude</b>"));
		flexTable.setWidget(0,3, new HTML("<b>Longitude</b>"));

		for (int j = 0; j < markerStorage.length; j++) {
			MarkerStorage marker = markerStorage[j];
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
		mapsDataService
				.getMarkerStorageList(new AsyncCallback<MarkerStorage[]>() {
					public void onFailure(Throwable caught) {
						Window.alert(SERVER_ERROR);
					}

					public void onSuccess(MarkerStorage[] result) {
						markerStorage = result;
						listMarkers();
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
}
