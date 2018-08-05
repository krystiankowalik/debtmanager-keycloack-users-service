package com.github.krystiankowalik.splitme.api.usersservice.exception;

public class GroupAlreadyExistsException extends Exception {

    public GroupAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public GroupAlreadyExistsException(String message) {
        super(message);
    }
}
