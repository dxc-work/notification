package notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import notification.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class NotificationApiTest {

    @Mock
    NotificationService notificationService;
    @InjectMocks
    NotificationApi notificationApi;

    private MockMvc mvc;

    private static String toJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(notificationApi).build();
    }

    @Test
    public void registerUser() throws Exception {
        String urlTemplate = "/user";
        User user = new User("username", "accessToken");
        User returnedUser = User.createNewUser(user, LocalDateTime.now());

        when(notificationService.registerUser(any())).thenReturn(returnedUser);

        mvc.perform(MockMvcRequestBuilders.post(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(toJson(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", equalTo(user.getUsername())))
                .andExpect(jsonPath("$.accessToken", equalTo(user.getAccessToken())))
                .andExpect(jsonPath("$.creationTime", notNullValue()))
                .andExpect(jsonPath("$.numOfNotificationsPushed", equalTo(0)));
    }

    @Test
    public void getAllUsers_noneExist() throws Exception {
        String urlTemplate = "/users";
        String emptyJsonArray = "[]";

        mvc.perform(get(urlTemplate))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(emptyJsonArray)));
    }

    @Test
    public void getAllUsers() throws Exception {
        String urlTemplate = "/users";
        User user1 = new User("username1", "accessToken1");
        User user2 = new User("username2", "accessToken2");
        User returnedUser1 = User.createNewUser(user1, LocalDateTime.now());
        User returnedUser2 = User.createNewUser(user2, LocalDateTime.now());

        when(notificationService.getAllUsers()).thenReturn(Arrays.asList(returnedUser1, returnedUser2));

        mvc.perform(get(urlTemplate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}