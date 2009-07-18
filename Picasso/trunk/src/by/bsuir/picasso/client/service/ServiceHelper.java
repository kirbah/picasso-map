package by.bsuir.picasso.client.service;

import com.google.gwt.core.client.GWT;

public class ServiceHelper {
  private final MapsDataServiceAsync mapsDataService = GWT.create(MapsDataService.class);
  private final LoginServiceAsync loginService = GWT.create(LoginService.class);

  public MapsDataServiceAsync getMapsDataService() {
    return mapsDataService;
  }

  public LoginServiceAsync getLoginService() {
    return loginService;
  }
}
