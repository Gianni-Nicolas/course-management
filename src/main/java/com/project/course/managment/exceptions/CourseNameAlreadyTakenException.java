package com.project.course.managment.exceptions;

import com.project.course.managment.constants.CustomExceptionConstants;
import org.springframework.http.HttpStatus;

public class CourseNameAlreadyTakenException extends GeneralApiException {

    public CourseNameAlreadyTakenException(String message) {
        super(message);
    }

    @Override
    public String getErrorCode() {
        return CustomExceptionConstants.COURSE_NAME_ALREADY_TAKEN_ERROR_CODE;
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
