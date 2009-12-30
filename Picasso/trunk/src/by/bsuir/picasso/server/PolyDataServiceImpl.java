package by.bsuir.picasso.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import by.bsuir.picasso.client.service.PolyDataService;
import by.bsuir.picasso.server.util.CacheUtil;
import by.bsuir.picasso.server.util.PMF;
import by.bsuir.picasso.shared.PolyStorage;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class PolyDataServiceImpl extends RemoteServiceServlet implements PolyDataService {

  public Boolean delete(Long[] ids) {
    Boolean isDeleted = false;
    PersistenceManager pm = PMF.get().getPersistenceManager();

    try {
      for (Long id : ids) {
        PolyStorage polyStorage = pm.getObjectById(PolyStorage.class, id);
        if (CacheUtil.getOpenMapId() == polyStorage.getMapId()) {
          pm.deletePersistent(polyStorage);
        }
      }
      isDeleted = true;
    } finally {
      pm.close();
    }
    return isDeleted;
  }

  public PolyStorage[] getPolyList() {
    PersistenceManager pm = PMF.get().getPersistenceManager();

    List<PolyStorage> detachedPoly = null;
    Query query = pm.newQuery(PolyStorage.class);
    query.setFilter("mapId == openMapId");
    query.declareParameters("Long openMapId");
    List<PolyStorage> persistedPoly = (List<PolyStorage>) query.execute(CacheUtil.getOpenMapId());
    detachedPoly = (List<PolyStorage>) pm.detachCopyAll(persistedPoly);
    pm.close();

    return detachedPoly.toArray(new PolyStorage[0]);
  }

  @Override
  public Long[] save(PolyStorage[] polyStorage) {
    List<Long> result = new ArrayList<Long>();
    PersistenceManager pm = PMF.get().getPersistenceManager();

    try {
      for (int i = 0; i < polyStorage.length; i++) {
        PolyStorage polyStore = polyStorage[i];
        polyStore.setMapId(CacheUtil.getOpenMapId());
        if (polyStore.getId() == null || polyStore.getId() == 0) {
          // Save new marker
          pm.makePersistent(polyStore);
        } else {
          // Update exist marker
          Transaction tx = pm.currentTransaction();
          try {
            tx.begin();
            PolyStorage managedPoly = pm.getObjectById(PolyStorage.class, polyStore.getId());
            if (CacheUtil.getOpenMapId() == managedPoly.getMapId()) {
              managedPoly.setPoints(polyStore.getPoints());
              managedPoly.setLevels(polyStore.getLevels());
              managedPoly.setZoomLevel(polyStore.getZoomLevel());
              managedPoly.setName(polyStore.getName());
            }
            tx.commit();
          } catch (Exception e) {
            if (tx.isActive()) {
              tx.rollback();
            }
          }
        }
        result.add(polyStore.getId());
      }
    } finally {
      pm.close();
    }

    return result.toArray(new Long[0]);
  }

}
