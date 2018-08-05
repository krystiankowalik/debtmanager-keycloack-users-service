package com.github.krystiankowalik.splitme.api.usersservice.exception;

public class UserNotFoundException extends Exception{

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
