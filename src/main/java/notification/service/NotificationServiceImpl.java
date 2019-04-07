package notification.service;

import notification.client.PushBulletApi;
import notification.entity.Message;
import notification.entity.User;
import notification.exception.DuplicateUserException;
import notification.exception.NotificationFailureException;
import notification.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private PushBulletApi pushBulletApi;
    @Autowired
    private LocalDateTimeProvider localDateTImeProvider;
    private Map<String, User> users = new HashMap<>();

    //Visible for testing
    void removeAllUsers() {
        users.clear();

    }

    @Override
    public User registerUser(final User user) {
        User newUser = User.createNewUser(user, localDateTImeProvider.now());
        User existingUser = users.get(newUser.getUsername());
        if (existingUser == null) {
            synchronized (this) {
                existingUser = users.get(newUser.getUsername());
                if (existingUser == null) {
                    users.put(newUser.getUsername(), newUser);
                }
            }
            return newUser;
        }
        throw new DuplicateUserException("The username [" + newUser.getUsername() + "] is already in use");
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public void pushMessage(final Message message) {
        User user = users.get(message.getUsername());
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
