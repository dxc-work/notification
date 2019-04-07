package notification.service;

import notification.entity.Message;
import notification.entity.User;
import notification.exception.DuplicateUserException;

import java.util.Collection;

public interface NotificationService {

    User registerUser(User user) throws DuplicateUserException;

    Collection<User> getAllUsers();

    void pushMessage(Message message);

}
