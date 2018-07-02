package com.krystiankowalik.debtmanager.usersservice.exception;

public class UserNotFoundException extends Exception{

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
