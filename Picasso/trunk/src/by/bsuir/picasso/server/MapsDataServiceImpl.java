package by.bsuir.picasso.server;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import by.bsuir.picasso.client.MapsDataService;
import by.bsuir.picasso.shared.MarkerStorage;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class MapsDataServiceImpl extends RemoteServiceServlet implements
		MapsDataService {

	@SuppressWarnings("unchecked")
	public MarkerStorage[] getMarkerStorageList() {
        PersistenceManager pm = PMF.get().getPersistenceManager();

        List<MarkerStorage> detachedMarkers = null;
        Query q = pm.newQuery(MarkerStorage.class);
        List<MarkerStorage> persistedMarkers = (List<MarkerStorage>) q.execute();
        detachedMarkers = (List<MarkerStorage>) pm.detachCopyAll(persistedMarkers);
        pm.close();

		return detachedMarkers.toArray(new MarkerStorage[0]);
	}

	public Long persistMarkerStorage(MarkerStorage markerStorage) {
        PersistenceManager pm = PMF.get().getPersistenceManager();

        try {
            pm.makePersistent(markerStorage);
        } finally {
            pm.close();
        }
        
        return markerStorage.getId();
	}

}
