package by.bsuir.picasso.client;

import by.bsuir.picasso.shared.MarkerStorage;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("maps")
public interface MapsDataService extends RemoteService {
	/**
	 * Gets all tasks that have been persisted.
	 * 
	 * @return
	 */
	MarkerStorage[] getMarkerStorageList();

	Long persistMarkerStorage(MarkerStorage markerStorage);

}
