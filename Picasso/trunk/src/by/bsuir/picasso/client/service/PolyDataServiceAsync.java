package by.bsuir.picasso.client.service;

import by.bsuir.picasso.shared.PolyStorage;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PolyDataServiceAsync {
  void getPolyList(AsyncCallback<PolyStorage[]> callback);

  void save(PolyStorage[] polyStorage, AsyncCallback<Long[]> callback);

  void delete(Long id, AsyncCallback<Boolean> callback);
}
