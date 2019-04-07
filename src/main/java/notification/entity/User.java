package notification.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicInteger;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    @NotEmpty
    private String username;
    @NotEmpty
    private String accessToken;
    private LocalDateTime creationTime;

    private AtomicInteger numOfNotificationsPushed;

    public static User createNewUser(final User user, final LocalDateTime creationTime) {
        return new User(user.getUsername(), user.getAccessToken(), creationTime, 0);
    }

    public User() {
    }

    public User(final String username, final String accessToken) {
        this.username = username;
        this.accessToken = accessToken;
    }

    private User(final String username, final String accessToken, final LocalDateTime creationTime,
                 final int numOfNotificationsPushed) {
        this.username = username;
        this.accessToken = accessToken;
        this.creationTime = creationTime;
        this.numOfNotificationsPushed = new AtomicInteger(numOfNotificationsPushed);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public LocalDateTime getCreationTime() {
        return creationTime == null ? null : creationTime.truncatedTo(ChronoUnit.SECONDS);
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public Integer getNumOfNotificationsPushed() {
        return numOfNotificationsPushed == null ? null : numOfNotificationsPushed.get();
    }

    public void setNumOfNotificationsPushed(AtomicInteger numOfNotificationsPushed) {
        this.numOfNotificationsPushed = numOfNotificationsPushed;
    }

    public void incrementNumOfNotificationsPushed() {
        if (numOfNotificationsPushed != null) {
            numOfNotificationsPushed.getAndIncrement();
        }
    }
}
