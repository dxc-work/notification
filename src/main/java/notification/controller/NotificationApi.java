package notification.controller;

import notification.service.NotificationService;
import notification.entity.Message;
import notification.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    @ResponseStatus(value = HttpStatus.CREATED)
    public User registerUser(@Valid @RequestBody final User user) {
        return notificationService.registerUser(user);
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return new ArrayList<>(notificationService.getAllUsers());
    }

    @PostMapping("/message")
    @ResponseStatus(value = HttpStatus.OK)
    public void sendNotification(@Valid @RequestBody final Message message) {
        notificationService.pushMessage(message);
    }

}