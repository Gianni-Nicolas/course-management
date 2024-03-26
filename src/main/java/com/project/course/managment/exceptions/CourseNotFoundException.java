package com.project.course.managment.exceptions;

import static com.project.course.managment.constants.CustomExceptionConstants.COURSE_NOT_FOUND_ERROR_CODE;

import org.springframework.http.HttpStatus;

public class CourseNotFoundException extends GeneralApiException {

    public CourseNotFoundException(String message) {
        super(message);
    }

    @Override
    public String getErrorCode() {
        return COURSE_NOT_FOUND_ERROR_CODE;
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
