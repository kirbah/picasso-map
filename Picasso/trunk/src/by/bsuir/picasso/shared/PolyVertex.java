package by.bsuir.picasso.shared;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.gwt.user.client.rpc.IsSerializable;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class PolyVertex implements IsSerializable {
  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  private Long id;

  @Persistent
  private Long mapId;

  @Persistent
  private double latitude;

  @Persistent
  private double longitude;

  @Persistent
  private PolyStorage polyStorage;

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

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public PolyStorage getPolyStorage() {
    return polyStorage;
  }

  public void setPolyStorage(PolyStorage polyStorage) {
    this.polyStorage = polyStorage;
  }
  
}
