package by.bsuir.picasso.client.service;

import by.bsuir.picasso.shared.MarkerStorage;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MarkersDataServiceAsync {
	void getMarkerStorageList(AsyncCallback<MarkerStorage[]> callback);

	void save(MarkerStorage[] markerStorage, AsyncCallback<Long[]> callback);

	void getMarkerStorage(Long id, AsyncCallback<MarkerStorage> callback);

	void delete(Long id, AsyncCallback<Boolean> callback);
}
