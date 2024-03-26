package com.project.course.managment.exceptions;

import static com.project.course.managment.constants.CustomExceptionConstants.INVALID_FILE_TYPE_ERROR_CODE;

import org.springframework.http.HttpStatus;

public class InvalidFileTypePdfException extends GeneralApiException {

    public InvalidFileTypePdfException(String message) {
        super(message);
    }

    @Override
    public String getErrorCode() {
        return INVALID_FILE_TYPE_ERROR_CODE;
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
