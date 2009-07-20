package by.bsuir.picasso.client.data;

import java.util.Date;

import com.extjs.gxt.ui.client.data.BaseModel;

public class MapModel extends BaseModel {
  public MapModel() {

  }

  public MapModel(Long mapId, String name, Date updateDate) {
    setMapId(mapId);
    setName(name);
    setUpdateDate(updateDate);
  }

  public Long getMapId() {
    return get("mapId");
  }

  public void setMapId(Long mapId) {
    set("mapId", mapId);
  }

  public Date getUpdateDate() {
    return get("updateDate");
  }

  public void setUpdateDate(Date updateDate) {
    set("updateDate", updateDate);
  }

  public String getName() {
    return get("name");
  }

  public void setName(String name) {
    set("name", name);
  }
}
