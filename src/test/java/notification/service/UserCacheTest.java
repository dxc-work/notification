package notification.service;

import notification.entity.User;
import notification.exception.DuplicateUserException;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserCacheTest {

    private static final LocalDateTime FIXED_TIME = LocalDateTime.now();

    @Autowired
    private UserCache userCache;

    @After
    public void tearDown() {
        userCache.clear();
    }

    @Test
    public void addUser() {
        User user = (User.createNewUser(new User("username", "accessToken"), FIXED_TIME));

        User returnedUser = userCache.addNewUser(user);

        assertThat(returnedUser.getUsername(), equalTo(user.getUsername()));
        assertThat(returnedUser.getAccessToken(), equalTo(user.getAccessToken()));
        assertThat(returnedUser.getCreationTime(), equalTo(FIXED_TIME.truncatedTo(ChronoUnit.SECONDS)));
        assertThat(returnedUser.getNumOfNotificationsPushed(), equalTo(0));
    }

    @Test(expected = DuplicateUserException.class)
    public void addUser_duplicate() {
        User user = (User.createNewUser(new User("username", "accessToken"), LocalDateTime.now()));

        assertThat(userCache.addNewUser(user), notNullValue());

        userCache.addNewUser(user);
    }

    @Test
    public void getUser() {
        User user = (User.createNewUser(new User("username", "accessToken"), FIXED_TIME));
        assertThat(userCache.addNewUser(user), notNullValue());

        User returnedUser = userCache.getUser(user.getUsername());

        assertThat(returnedUser.getUsername(), equalTo(user.getUsername()));
        assertThat(returnedUser.getAccessToken(), equalTo(user.getAccessToken()));
        assertThat(returnedUser.getCreationTime(), equalTo(FIXED_TIME.truncatedTo(ChronoUnit.SECONDS)));
        assertThat(returnedUser.getNumOfNotificationsPushed(), equalTo(0));
    }

    @Test
    public void getAllUsers() {
        User user1 = (User.createNewUser(new User("username1", "accessToken1"), FIXED_TIME));
        User user2 = (User.createNewUser(new User("username2", "accessToken2"), FIXED_TIME));
        assertThat(userCache.addNewUser(user1), notNullValue());
        assertThat(userCache.addNewUser(user2), notNullValue());

        Collection<User> users = userCache.getAllUsers();

        assertThat(users, iterableWithSize(2));
    }
}