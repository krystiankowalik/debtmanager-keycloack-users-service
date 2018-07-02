package com.krystiankowalik.debtmanager.usersservice.exception;

public class InvitationAlreadyExistsException extends Exception {

    public InvitationAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvitationAlreadyExistsException(String message) {
        super(message);
    }
}
