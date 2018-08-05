package com.github.krystiankowalik.splitme.api.usersservice.exception;

public class InvitationAlreadyExistsException extends Exception {

    public InvitationAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvitationAlreadyExistsException(String message) {
        super(message);
    }
}
