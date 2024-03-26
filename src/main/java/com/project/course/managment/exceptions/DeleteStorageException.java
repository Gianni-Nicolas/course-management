package com.project.course.managment.exceptions;

import com.project.course.managment.constants.CustomExceptionConstants;
import org.springframework.http.HttpStatus;

public class DeleteStorageException extends GeneralApiException {

    public DeleteStorageException(String message) {
        super(message);
    }

    @Override
    public String getErrorCode() {
        return CustomExceptionConstants.DELETE_STORAGE_EXCEPTION_CODE;
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
