package by.bsuir.picasso.server.util;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class UserUtil {
  public static User getCurrentUser() {
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    return user;
  }

  public static String getCurrentUserEmail() {
    return getCurrentUser().getEmail();
  }

}
