package com.project.course.managment.exceptions;

import static com.project.course.managment.constants.CommonsErrorConstants.DEFAULT_SERVICE_ERROR_CODE;

import org.springframework.http.HttpStatus;

public class StorageGeneralException extends GeneralApiException {

    public StorageGeneralException(String message) {
        super(message);
    }

    @Override
    public String getErrorCode() {
        return DEFAULT_SERVICE_ERROR_CODE;
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
