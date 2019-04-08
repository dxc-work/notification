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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
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
    @MockBean
    private UserCache userCache;
    @Autowired
    private NotificationServiceImpl notificationService;

    @Before
    public void setUp() {
        when(localDateTimeProvider.now()).thenReturn(FIXED_TIME);
    }

    @After
    public void tearDown() {
        userCache.clear();
    }

    @Test
    public void registerUser() {
        User user = new User("username", "accessToken");
        when(userCache.addNewUser(any())).thenReturn(User.createNewUser(user, FIXED_TIME));

        User returnedUser = notificationService.registerUser(user);

        assertThat(returnedUser.getUsername(), equalTo(user.getUsername()));
        assertThat(returnedUser.getAccessToken(), equalTo(user.getAccessToken()));
        assertThat(returnedUser.getCreationTime(), equalTo(FIXED_TIME.truncatedTo(ChronoUnit.SECONDS)));
        assertThat(returnedUser.getNumOfNotificationsPushed(), equalTo(0));
    }

    @Test(expected = DuplicateUserException.class)
    public void registerUser_duplicate() {
        doThrow(new DuplicateUserException("")).when(userCache).addNewUser(any());

        notificationService.registerUser(new User("username", "accessToken"));
    }

    @Test
    public void getAllUsers() {
        User user1 = new User("username1", "accessToken");
        User user2 = new User("username2", "accessToken");

        when(userCache.getAllUsers()).thenReturn(Arrays.asList(User.createNewUser(user1, FIXED_TIME),
                User.createNewUser(user2, FIXED_TIME)));

        Collection<User> users = notificationService.getAllUsers();

        assertThat(users, iterableWithSize(2));
    }

    @Test
    public void getAllUsers_empty() {
        assertThat(notificationService.getAllUsers(), emptyIterable());
    }

    @Test
    public void pushMessage() {
        User user = User.createNewUser(new User("username", "accessToken"), FIXED_TIME);
        when(userCache.getUser(any())).thenReturn(user);
        Message message = new Message();
        message.setUsername(user.getUsername());
        message.setText("text");

        notificationService.pushMessage(message);

        assertThat(user.getNumOfNotificationsPushed(), equalTo(1));
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
        User user = User.createNewUser(new User("username", "accessToken"), FIXED_TIME);
        when(userCache.getUser(any())).thenReturn(user);
        Message message = new Message();
        message.setUsername(user.getUsername());
        message.setText("text");

        doThrow(new RuntimeException()).when(pushBulletApi).push(any(), any());

        notificationService.pushMessage(message);
    }
}