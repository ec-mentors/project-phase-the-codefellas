package io.everyonecodes.anber.pwrecovery;

public class UserNotFoundException extends java.lang.Exception {
    private String username;

    public UserNotFoundException(String username) {
        super("The user with username" + username + "was not found");

    }

    public String getUsername() {
        return username;
    }
}
