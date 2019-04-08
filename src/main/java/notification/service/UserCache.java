package notification.service;

import notification.entity.User;
import notification.exception.DuplicateUserException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;

@Component(value = "UserCache")
public class UserCache {

    private HashMap<String, User> users = new HashMap<>();

    public User addNewUser(User user) {
        User existingUser = users.get(user.getUsername());
        if (existingUser == null) {
            synchronized (this) {
                existingUser = users.get(user.getUsername());
                if (existingUser == null) {
                    users.put(user.getUsername(), user);
                }
            }
            return user;
        }
        throw new DuplicateUserException("The username [" + user.getUsername() + "] is already in use");
    }

    public Collection<User> getAllUsers() {
        return users.values();
    }

    public User getUser(String username) {
        return users.get(username);
    }

    //Visible for testing
    void clear() {
        users.clear();

    }
}
