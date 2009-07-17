package by.bsuir.picasso.client.service;

import by.bsuir.picasso.shared.PolyStorage;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("poly")
public interface PolyService extends RemoteService {
  PolyStorage[] getPolyList();

  Long save(PolyStorage polySrorage);

  Boolean delete(Long id);
}
