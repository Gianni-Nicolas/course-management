package com.project.course.managment.exceptions.handler;

import com.project.course.managment.constants.CommonsErrorConstants;
import com.project.course.managment.exceptions.CourseNameAlreadyTakenException;
import com.project.course.managment.exceptions.CourseNotFoundException;
import com.project.course.managment.exceptions.DeleteStorageException;
import com.project.course.managment.exceptions.DocumentNotFoundStorageException;
import com.project.course.managment.exceptions.DocumentNotOwnedException;
import com.project.course.managment.exceptions.DocumentsAlreadyOwnedException;
import com.project.course.managment.exceptions.GeneralApiException;
import com.project.course.managment.exceptions.InvalidFileTypePdfException;
import com.project.course.managment.exceptions.StorageGeneralException;
import com.project.course.managment.models.response.ApplicationResponse;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@NoArgsConstructor
public class CustomRestExceptionHandler extends RestExceptionHandler {


    @ExceptionHandler({
            CourseNameAlreadyTakenException.class,
            CourseNotFoundException.class,
            DeleteStorageException.class,
            DocumentNotOwnedException.class,
            DocumentNotFoundStorageException.class,
            DocumentsAlreadyOwnedException.class,
            InvalidFileTypePdfException.class,
            StorageGeneralException.class
    })
    protected ResponseEntity<ApplicationResponse<Object>> handleCustomExceptions(
            GeneralApiException generalApiException) {

        log.error(CommonsErrorConstants.LOG_ERROR_MESSAGE, generalApiException.getMessage());

        ApplicationResponse<Object> applicationResponse = this
                .getApplicationResponse(generalApiException.getErrorCode(),
                        generalApiException.getMessage(), null);
        return ResponseEntity.status(generalApiException.getStatus())
                .body(applicationResponse);
    }

}
