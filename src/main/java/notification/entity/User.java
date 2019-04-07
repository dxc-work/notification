package notification.entity;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class User {
    @NotEmpty
    private String username;
    @NotEmpty
    private String accessToken;
    private LocalDateTime creationTime;
    private Long numOfNotificationsPushed;

    public User() {
    }

    public User(final String username, final String accessToken) {
        this.username = username;
        this.accessToken = accessToken;
    }

    private User(final String username, final String accessToken, final LocalDateTime creationTime,
                 final long numOfNotificationsPushed) {
        this.username = username;
        this.accessToken = accessToken;
        this.creationTime = creationTime;
        this.numOfNotificationsPushed = numOfNotificationsPushed;
    }

    public static User createNewUser(final User user, final LocalDateTime creationTime) {
        return new User(user.getUsername(), user.getAccessToken(), creationTime, 0L);
    }

    public String getUsername() {
        return username;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public LocalDateTime getCreationTime() {
        return creationTime == null ? null : creationTime.truncatedTo(ChronoUnit.SECONDS);
    }

    public Long getNumOfNotificationsPushed() {
        return numOfNotificationsPushed;
    }
}
