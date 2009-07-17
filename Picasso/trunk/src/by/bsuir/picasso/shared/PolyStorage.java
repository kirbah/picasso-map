package by.bsuir.picasso.shared;

import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.gwt.user.client.rpc.IsSerializable;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class PolyStorage implements IsSerializable {
  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  private Long id;

  @Persistent
  private Long mapId;

  @Persistent
  private String name;

  /**
   * @see by.bsuir.picasso.shared.MapTypes
   */
  @Persistent
  private String type;

  @Persistent(mappedBy = "polyStorage")
  private List<PolyVertex> vertex;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getMapId() {
    return mapId;
  }

  public void setMapId(Long mapId) {
    this.mapId = mapId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public List<PolyVertex> getVertex() {
    return vertex;
  }

  public void setVertex(List<PolyVertex> vertex) {
    this.vertex = vertex;
  }

}
