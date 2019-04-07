package notification;

import notification.entity.User;

import java.util.Collection;

public interface NotificationService {
    User registerUser(User user);

    Collection<User> getAllUsers();
}
