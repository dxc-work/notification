package notification.service;

import notification.client.PushBulletApi;
import notification.entity.Message;
import notification.entity.User;
import notification.exception.DuplicateUserException;
import notification.exception.NotificationFailureException;
import notification.exception.UserNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NotificationServiceImplTest {

    private static final LocalDateTime FIXED_TIME = LocalDateTime.now();

    @MockBean
    private PushBulletApi pushBulletApi;
    @MockBean
    private LocalDateTimeProvider localDateTimeProvider;
    @Autowired
    private NotificationServiceImpl notificationService;

    @Before
    public void setUp() throws Exception {
        when(localDateTimeProvider.now()).thenReturn(FIXED_TIME);
    }

    @After
    public void tearDown() throws Exception {
        notificationService.removeAllUsers();
    }

    @Test
    public void registerUser() {
        User user = new User("username", "accessToken");

        User returnedUser = notificationService.registerUser(user);

        assertThat(returnedUser.getUsername(), equalTo(user.getUsername()));
        assertThat(returnedUser.getAccessToken(), equalTo(user.getAccessToken()));
        assertThat(returnedUser.getCreationTime(), equalTo(FIXED_TIME.truncatedTo(ChronoUnit.SECONDS)));
        assertThat(returnedUser.getNumOfNotificationsPushed(), equalTo(0));
    }

    @Test(expected = DuplicateUserException.class)
    public void registerUser_duplicate() {
        User user = new User("username", "accessToken");

        assertThat(notificationService.registerUser(user), notNullValue());

        notificationService.registerUser(user);
    }

    @Test
    public void getAllUsers() {
        User user1 = new User("username1", "accessToken");
        User user2 = new User("username2", "accessToken");

        assertThat(notificationService.registerUser(user1), notNullValue());
        assertThat(notificationService.registerUser(user2), notNullValue());

        Collection<User> users = notificationService.getAllUsers();

        assertThat(users, iterableWithSize(2));
    }

    @Test
    public void getAllUsers_empty() {
        assertThat(notificationService.getAllUsers(), emptyIterable());
    }

    @Test
    public void pushMessage() {
        User user = new User("username", "accessToken");
        assertThat(notificationService.registerUser(user), notNullValue());
        Message message = new Message();
        message.setUsername(user.getUsername());
        message.setText("text");

        notificationService.pushMessage(message);

        List<User> users = new ArrayList<>(notificationService.getAllUsers());

        assertThat(users, iterableWithSize(1));
        assertThat(users.get(0).getNumOfNotificationsPushed(), equalTo(1));
    }

    @Test(expected = UserNotFoundException.class)
    public void pushMessage_noUser() {
        Message message = new Message();
        message.setUsername("username");
        message.setText("text");

        notificationService.pushMessage(message);
    }

    @Test(expected = NotificationFailureException.class)
    public void pushMessage_unexpectedError() {
        User user = new User("username", "accessToken");
        assertThat(notificationService.registerUser(user), notNullValue());
        Message message = new Message();
        message.setUsername(user.getUsername());
        message.setText("text");

        doThrow(new RuntimeException()).when(pushBulletApi).push(Mockito.any(), Mockito.any());

        notificationService.pushMessage(message);
    }
}