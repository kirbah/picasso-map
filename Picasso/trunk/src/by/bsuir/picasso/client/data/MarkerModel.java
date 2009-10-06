package by.bsuir.picasso.client.data;

import com.extjs.gxt.ui.client.data.BaseModel;

public class MarkerModel extends BaseModel {
  public MarkerModel() {

  }

  public MarkerModel(Long id, String name) {
    setId(id);
    setName(name);
  }

  public Long getId() {
    return get("id");
  }

  public void setId(Long id) {
    set("id", id);
  }

  public String getName() {
    return get("name");
  }

  public void setName(String name) {
    set("name", name);
  }
}
