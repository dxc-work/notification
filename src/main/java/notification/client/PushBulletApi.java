package notification.client;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import notification.entity.Message;
import notification.entity.PushBulletRequest;
import notification.entity.PushBulletResponse;
import notification.exception.NotificationFailureException;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Component
public class PushBulletApi {
    private static final String PUSH_ENDPOINT = "https://api.pushbullet.com/v2/pushes";
    private RestTemplate restTemplate;

    public PushBulletApi(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        restTemplate.setMessageConverters(Collections.singletonList(new MappingJackson2HttpMessageConverter(mapper)));
    }

    public void push(final String accessToken, final Message message) {
        PushBulletRequest request = new PushBulletRequest();
        request.setTitle("push");
        request.setType("note");
        request.setBody(message.getText());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Access-Token", accessToken);
        ResponseEntity<PushBulletResponse> response;
        try {
            response = restTemplate
                    .exchange(PUSH_ENDPOINT, HttpMethod.POST, new HttpEntity<>(request, headers), PushBulletResponse.class);
        } catch (Exception e) {
            throw new NotificationFailureException(e.getMessage());
        }

        if (response == null || !response.getStatusCode().equals(HttpStatus.OK)) {
            throw new NotificationFailureException("Falied to send notification");
        }
    }
}
