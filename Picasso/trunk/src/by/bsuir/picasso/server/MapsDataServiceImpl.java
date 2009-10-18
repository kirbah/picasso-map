package by.bsuir.picasso.server;

import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import by.bsuir.picasso.client.service.MapsDataService;
import by.bsuir.picasso.server.util.CacheUtil;
import by.bsuir.picasso.server.util.PMF;
import by.bsuir.picasso.server.util.UserUtil;
import by.bsuir.picasso.shared.MapInfo;
import by.bsuir.picasso.shared.MapTypes;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class MapsDataServiceImpl extends RemoteServiceServlet implements MapsDataService {

  public Boolean delete(Long id) {
    Boolean isDeleted = false;
    PersistenceManager pm = PMF.get().getPersistenceManager();

    try {
      MapInfo mapInfo = pm.getObjectById(MapInfo.class, id);
      if (UserUtil.getCurrentUserEmail().equals(mapInfo.getUserEmailAddress())) {
        pm.deletePersistent(mapInfo);
        isDeleted = true;
      }
    } finally {
      pm.close();
    }
    return isDeleted;
  }

  public Boolean delete(Long[] ids) {
    Boolean isDeleted = false;
    PersistenceManager pm = PMF.get().getPersistenceManager();

    try {
      for (Long id : ids) {
        MapInfo mapInfo = pm.getObjectById(MapInfo.class, id);
        if (UserUtil.getCurrentUserEmail().equals(mapInfo.getUserEmailAddress())) {
          pm.deletePersistent(mapInfo);
          isDeleted = true;
        }
      }
    } finally {
      pm.close();
    }
    return isDeleted;
  }

  public MapInfo findOpenMap() {
    MapInfo openMap = null;
    PersistenceManager pm = PMF.get().getPersistenceManager();

    Query query = pm.newQuery(MapInfo.class);
    query.setFilter("userEmailAddress == userEmail && status == '" + MapTypes.MAP_OPEN + "'");
    query.setOrdering("updateDate desc");
    query.declareParameters("String userEmail");
    String userEmail = UserUtil.getCurrentUserEmail();

    List<MapInfo> mapInfos = (List<MapInfo>) query.execute(userEmail);
    if (mapInfos.size() > 0) {
      openMap = pm.detachCopy(mapInfos.get(0));
    }
    pm.close();

    return openMap;
  }

  public MapInfo[] getMapsList() {
    PersistenceManager pm = PMF.get().getPersistenceManager();

    List<MapInfo> detachedMaps = null;
    Query query = pm.newQuery(MapInfo.class);
    query.setFilter("userEmailAddress == userEmail");
    query.setOrdering("updateDate desc");
    query.declareParameters("String userEmail");
    String userEmail = UserUtil.getCurrentUserEmail();

    List<MapInfo> mapInfos = (List<MapInfo>) query.execute(userEmail);
    detachedMaps = (List<MapInfo>) pm.detachCopyAll(mapInfos);
    pm.close();

    return detachedMaps.toArray(new MapInfo[0]);
  }

  public Long save(MapInfo mapInfo) {
    PersistenceManager pm = PMF.get().getPersistenceManager();

    try {
      if (mapInfo.getMapId() == null || mapInfo.getMapId() == 0) {
        // Save new map
        mapInfo.setCreateDate(new Date());
        mapInfo.setUpdateDate(new Date());
        mapInfo.setUserEmailAddress(UserUtil.getCurrentUserEmail());
        pm.makePersistent(mapInfo);
      } else {
        // Update exist map
        Transaction tx = pm.currentTransaction();
        try {
          tx.begin();
          MapInfo managedMapInfo = pm.getObjectById(MapInfo.class, mapInfo.getMapId());
          if (UserUtil.getCurrentUserEmail().equals(managedMapInfo.getUserEmailAddress())) {
            // Update maps only for current user
            managedMapInfo.setUpdateDate(new Date());
            managedMapInfo.setName(mapInfo.getName());
            managedMapInfo.setLatitude(mapInfo.getLatitude());
            managedMapInfo.setLongitude(mapInfo.getLongitude());
            managedMapInfo.setZoomLevel(mapInfo.getZoomLevel());
          }
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

    return mapInfo.getMapId();
  }

  public MapInfo openMap(Long mapId) {
    MapInfo openMap = null;
    PersistenceManager pm = PMF.get().getPersistenceManager();

    try {
      Transaction tx = pm.currentTransaction();
      try {
        // Close open Maps
        tx.begin();
        Query query = pm.newQuery(MapInfo.class, "status == '" + MapTypes.MAP_OPEN + "'");
        List<MapInfo> mapInfos = (List<MapInfo>) query.execute();
        for (MapInfo mapInfo : mapInfos) {
          mapInfo.setStatus(MapTypes.MAP_CLOSED);
        }
        tx.commit();

        // Open selected Map
        tx.begin();
        MapInfo managedMapInfo = pm.getObjectById(MapInfo.class, mapId);
        if (UserUtil.getCurrentUserEmail().equals(managedMapInfo.getUserEmailAddress())) {
          // Update maps only for current user
          managedMapInfo.setStatus(MapTypes.MAP_OPEN);
          openMap = pm.detachCopy(managedMapInfo);
        }
        tx.commit();
      } catch (Exception e) {
        if (tx.isActive()) {
          tx.rollback();
        }
      }
    } finally {
      pm.close();
    }

    CacheUtil.setOpenMapId(openMap.getMapId());

    return openMap;
  }

}
