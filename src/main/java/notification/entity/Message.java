package notification.entity;

import javax.validation.constraints.NotEmpty;

public class Message {
    @NotEmpty
    private String username;
    @NotEmpty
    private String text;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
