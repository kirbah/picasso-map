package by.bsuir.picasso.client.service;

import by.bsuir.picasso.shared.MarkerStorage;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("markers")
public interface MarkersDataService extends RemoteService {
	MarkerStorage[] getMarkerStorageList();

	Long[] save(MarkerStorage[] markerStorage);

	MarkerStorage getMarkerStorage(Long id);

	Boolean delete(Long[] id);
}
