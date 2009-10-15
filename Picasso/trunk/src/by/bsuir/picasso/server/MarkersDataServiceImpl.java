package by.bsuir.picasso.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import by.bsuir.picasso.client.service.MarkersDataService;
import by.bsuir.picasso.server.util.CacheUtil;
import by.bsuir.picasso.server.util.PMF;
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
    Query query = pm.newQuery(MarkerStorage.class);
    query.setFilter("mapId == openMapId");
    query.declareParameters("Long openMapId");
    List<MarkerStorage> persistedMarkers = (List<MarkerStorage>) query.execute(CacheUtil.getOpenMapId());
    detachedMarkers = (List<MarkerStorage>) pm.detachCopyAll(persistedMarkers);
    pm.close();

    return detachedMarkers.toArray(new MarkerStorage[0]);
  }

  public Long[] save(MarkerStorage[] markerStorages) {
    List<Long> result = new ArrayList<Long>();
    PersistenceManager pm = PMF.get().getPersistenceManager();

    try {
      for (int i = 0; i < markerStorages.length; i++) {
        MarkerStorage markerStorage = markerStorages[i];
        markerStorage.setMapId(CacheUtil.getOpenMapId());
        if (markerStorage.getId() == null || markerStorage.getId() == 0) {
          // Save new marker
          pm.makePersistent(markerStorage);
        } else {
          // Update exist marker
          Transaction tx = pm.currentTransaction();
          try {
            tx.begin();
            MarkerStorage managedMarker = pm.getObjectById(MarkerStorage.class, markerStorage.getId());
            if (CacheUtil.getOpenMapId() == managedMarker.getMapId()) {
              managedMarker.setLatitude(markerStorage.getLatitude());
              managedMarker.setLongitude(markerStorage.getLongitude());
              managedMarker.setName(managedMarker.getName());
            }
            tx.commit();
          } catch (Exception e) {
            if (tx.isActive()) {
              tx.rollback();
            }
          }
        }
        result.add(markerStorage.getId());
      }
    } finally {
      pm.close();
    }

    return result.toArray(new Long[0]);
  }

  public Boolean delete(Long id) {
    Boolean isDeleted = false;
    PersistenceManager pm = PMF.get().getPersistenceManager();

    try {
      MarkerStorage managedMarker = pm.getObjectById(MarkerStorage.class, id);
      if (CacheUtil.getOpenMapId() == managedMarker.getMapId()) {
        pm.deletePersistent(managedMarker);
        isDeleted = true;
      }
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
      if (CacheUtil.getOpenMapId() != managedMarker.getMapId()) {
        managedMarker = null;
      }
    } finally {
      pm.close();
    }

    return managedMarker;
  }

}
