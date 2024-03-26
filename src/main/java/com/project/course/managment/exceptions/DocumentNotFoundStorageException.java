package com.project.course.managment.exceptions;

import static com.project.course.managment.constants.CustomExceptionConstants.PDF_NOT_FOUND_STORAGE_ERROR_CODE;

import org.springframework.http.HttpStatus;

public class DocumentNotFoundStorageException extends GeneralApiException {

    public DocumentNotFoundStorageException(String message) {
        super(message);
    }

    @Override
    public String getErrorCode() {
        return PDF_NOT_FOUND_STORAGE_ERROR_CODE;
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
