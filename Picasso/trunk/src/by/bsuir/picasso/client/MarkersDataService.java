package by.bsuir.picasso.client;

import by.bsuir.picasso.shared.MarkerStorage;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("markers")
public interface MarkersDataService extends RemoteService {
	MarkerStorage[] getMarkerStorageList();

	Long persistMarkerStorage(MarkerStorage markerStorage);

	MarkerStorage getMarkerStorage(Long id);

	Boolean deleteMarkerStorage(Long id);
}
