package by.bsuir.picasso.server.util;

import java.util.HashMap;
import java.util.Map;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;

import by.bsuir.picasso.server.MapsDataServiceImpl;
import by.bsuir.picasso.shared.MapInfo;

import com.google.appengine.api.memcache.stdimpl.GCacheFactory;

public class CacheUtil {
  public static final String CACHE_KEY_DELIMETER = "||";
  public static final String CURRENT_MAP_KEY = "MAP";

  public static Cache getCache() {
    Cache cache = null;

    Map props = new HashMap();
    props.put(GCacheFactory.EXPIRATION_DELTA, 3600);

    try {
      CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
      cache = cacheFactory.createCache(props);
    } catch (CacheException e) {
      // TODO logging ...
    }
    return cache;
  }

  public static Long getOpenMapId() {
    Long mapId = null;
    String mapKey = UserUtil.getCurrentUserEmail() + CACHE_KEY_DELIMETER + CURRENT_MAP_KEY;
    Cache cache = getCache();
    if (cache.get(mapKey) != null) {
      mapId = (Long) cache.get(mapKey);
    } else {
      MapInfo mapInfo = (new MapsDataServiceImpl()).findOpenMap();
      mapId = mapInfo.getMapId();
      cache.put(mapKey, mapId);
    }
    return mapId;
  }

  public static void setOpenMapId(Long mapId) {
    String mapKey = UserUtil.getCurrentUserEmail() + CACHE_KEY_DELIMETER + CURRENT_MAP_KEY;
    Cache cache = getCache();
    cache.put(mapKey, mapId);
  }

}
