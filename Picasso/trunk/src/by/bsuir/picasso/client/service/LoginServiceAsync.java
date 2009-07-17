package by.bsuir.picasso.client.service;

import by.bsuir.picasso.shared.LoginInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync {
  public void login(String requestUri, AsyncCallback<LoginInfo> async);
}
