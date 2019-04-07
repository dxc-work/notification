package notification;

import notification.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class NotificationApi {

    private NotificationService notificationService;

    public NotificationApi(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/user")
    public User registerUser(@Valid @RequestBody final User user) {
        return notificationService.registerUser(user);
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return new ArrayList<>(notificationService.getAllUsers());
    }


}