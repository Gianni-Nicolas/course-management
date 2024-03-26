package com.project.course.managment.exceptions;

import org.springframework.http.HttpStatus;

public class DocumentsAlreadyOwnedException extends GeneralApiException {

    private final String code;

    public DocumentsAlreadyOwnedException(String code, String message) {
        super(message);
        this.code = code;
    }

    @Override
    public String getErrorCode() {
        return code;
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
