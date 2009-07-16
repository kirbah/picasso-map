package by.bsuir.picasso.server;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import by.bsuir.picasso.client.MarkersDataService;
import by.bsuir.picasso.shared.MarkerStorage;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class MarkersDataServiceImpl extends RemoteServiceServlet implements MarkersDataService {

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
			if (markerStorage.getId() == null || markerStorage.getId() == 0) {
				// Save new marker
				pm.makePersistent(markerStorage);
			} else {
				// Update exist marker
				Transaction tx = pm.currentTransaction();
				try {
					tx.begin();
					MarkerStorage managedMarker = pm.getObjectById(MarkerStorage.class, markerStorage.getId());
					managedMarker.setLatitude(markerStorage.getLatitude());
					managedMarker.setLongitude(markerStorage.getLongitude());
					managedMarker.setName(managedMarker.getName());
					tx.commit();
				} catch (Exception e) {
					if (tx.isActive()) {
						tx.rollback();
					}
				}
			}
		} finally {
			pm.close();
		}

		return markerStorage.getId();
	}

	public Boolean deleteMarkerStorage(Long id) {
		Boolean isDeleted = false;
		PersistenceManager pm = PMF.get().getPersistenceManager();

		try {
			MarkerStorage managedMarker = pm.getObjectById(MarkerStorage.class, id);
			pm.deletePersistent(managedMarker);
			isDeleted = true;
		} finally {
			pm.close();
		}
		return isDeleted;
	}

	public MarkerStorage getMarkerStorage(Long id) {
		MarkerStorage managedMarker = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();

		try {
			managedMarker = pm.getObjectById(MarkerStorage.class, id);
		} finally {
			pm.close();
		}

		return managedMarker;
	}

}
