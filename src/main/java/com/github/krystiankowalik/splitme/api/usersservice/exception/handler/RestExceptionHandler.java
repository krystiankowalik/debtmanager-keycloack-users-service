package com.github.krystiankowalik.splitme.api.usersservice.exception.handler;

import com.github.krystiankowalik.splitme.api.usersservice.exception.*;
import org.keycloak.exceptions.TokenNotActiveException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Malformed JSON request";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleOtherExceptions(Exception e) {
        return buildStandardResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AlreadyGroupMemberException.class)
    protected ResponseEntity<Object> handleAlreadyGroupMemberException(AlreadyGroupMemberException e) {
        return buildStandardResponseEntity(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GroupNotFoundException.class)
    protected ResponseEntity<Object> handleGroupNotFoundException(GroupNotFoundException e) {
        return buildStandardResponseEntity(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GroupAlreadyExistsException.class)
    protected ResponseEntity<Object> handleGroupAlreadyExistsException(GroupAlreadyExistsException e) {
        return buildStandardResponseEntity(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotGroupMemberException.class)
    protected ResponseEntity<Object> handleNotGroupMembersException(NotGroupMemberException e) {
        return buildStandardResponseEntity(e, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvitationAlreadyExistsException.class)
    protected ResponseEntity<Object> handleInvitationAlreadExistsException(InvitationAlreadyExistsException e) {
        return buildStandardResponseEntity(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TokenNotActiveException.class)
    protected ResponseEntity<Object> handleTokenNotActiveException(TokenNotActiveException e) {
        return buildStandardResponseEntity(e, HttpStatus.UNAUTHORIZED);

    }


    private ResponseEntity<Object> buildStandardResponseEntity(Exception e, HttpStatus httpStatus) {
        ApiError apiError = new ApiError(httpStatus);
        apiError.setMessage(e.getMessage());
        return buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }


}