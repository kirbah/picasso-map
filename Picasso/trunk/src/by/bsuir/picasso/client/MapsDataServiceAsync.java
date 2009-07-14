package by.bsuir.picasso.client;

import by.bsuir.picasso.shared.MarkerStorage;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface MapsDataServiceAsync {
	void getMarkerStorageList(AsyncCallback<MarkerStorage[]> callback);

	void persistMarkerStorage(MarkerStorage markerStorage, AsyncCallback<Long> callback);
	
}
