package by.bsuir.picasso.server;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import by.bsuir.picasso.client.service.PolyService;
import by.bsuir.picasso.server.util.CacheUtil;
import by.bsuir.picasso.server.util.PMF;
import by.bsuir.picasso.shared.PolyStorage;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class PolyServiceImpl extends RemoteServiceServlet implements PolyService {

  public Boolean delete(Long id) {
    Boolean isDeleted = false;
    PersistenceManager pm = PMF.get().getPersistenceManager();

    try {
      PolyStorage polyStorage = pm.getObjectById(PolyStorage.class, id);
      if (CacheUtil.getOpenMapId() == polyStorage.getMapId()) {
        pm.deletePersistent(polyStorage);
        isDeleted = true;
      }
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
  public Long save(PolyStorage polySrorage) {
    // TODO Auto-generated method stub
    return null;
  }

}
