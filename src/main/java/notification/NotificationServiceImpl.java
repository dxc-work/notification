package notification;

import notification.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Map<String, User> users = new HashMap<>();

    @Override
    public User registerUser(final User user) {
        User newUser = User.createNewUser(user, LocalDateTime.now());
        users.put(newUser.getUsername(), newUser);
        return newUser;
    }
}
