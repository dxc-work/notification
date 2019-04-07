package notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import notification.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class NotificationApiTest {

    @Autowired
    private MockMvc mvc;

    private static String toJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }

    @Test
    public void registerUser() throws Exception {
        String urlTemplate = "/user";
        User user = new User("username", "accessToken");

        mvc.perform(MockMvcRequestBuilders.post(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(toJson(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", equalTo(user.getUsername())))
                .andExpect(jsonPath("$.accessToken", equalTo(user.getAccessToken())))
                .andExpect(jsonPath("$.creationTime", notNullValue()))
                .andExpect(jsonPath("$.numOfNotificationsPushed", equalTo(0)));
    }
}