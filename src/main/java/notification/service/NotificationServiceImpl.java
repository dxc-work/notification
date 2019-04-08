package notification.service;

import notification.client.PushBulletApi;
import notification.entity.Message;
import notification.entity.User;
import notification.exception.NotificationFailureException;
import notification.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;


@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private PushBulletApi pushBulletApi;
    @Autowired
    private LocalDateTimeProvider localDateTImeProvider;
    @Autowired
    private UserCache users;

    @Override
    public User registerUser(final User user) {
        return users.addNewUser(User.createNewUser(user, localDateTImeProvider.now()));
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.getAllUsers();
    }

    @Override
    public void pushMessage(final Message message) {
        User user = users.getUser(message.getUsername());
        if (user == null) {
            throw new UserNotFoundException("No user was found with the username [" + message.getUsername() + "]");
        }
        try {
            pushBulletApi.push(user.getAccessToken(), message);
            user.incrementNumOfNotificationsPushed();
        } catch (Exception e) {
            throw new NotificationFailureException("Failed to send message");
        }

    }
}
