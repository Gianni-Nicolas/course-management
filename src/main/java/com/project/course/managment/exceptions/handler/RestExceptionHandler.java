package com.project.course.managment.exceptions.handler;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.project.course.managment.constants.CommonsErrorConstants;
import com.project.course.managment.models.response.ApplicationResponse;
import com.project.course.managment.models.response.ErrorDetail;
import com.project.course.managment.models.response.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

public abstract class RestExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static final String TIME_FORMAT = "HH:mm";

    protected static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

    protected static final String JSON_FIELD_SEPARATOR = ".";

    @Value("${spring.application.name}")
    protected String applicationName;

    protected RestExceptionHandler() {
    }

    @ExceptionHandler({ConstraintViolationException.class})
    protected ResponseEntity<ApplicationResponse<Object>> handleConstraintViolationException(
            ConstraintViolationException ex) {
        List<ErrorDetail> details = new ArrayList<>();

        List<String> fields =
                ex.getConstraintViolations().stream().map(e -> e.getPropertyPath().toString())
                        .distinct().collect(Collectors.toList());

        for (String field : fields) {
            List<ConstraintViolation<?>> exceptions =
                    ex.getConstraintViolations().stream()
                            .filter(e -> e.getPropertyPath().toString().equals(field))
                            .collect(Collectors.toList());

            String property;

            if (field.contains(".")) {
                property = field.split("\\.")[1];
            } else {
                property = field;
            }

            List<String> messages =
                    exceptions.stream().map(constraint -> {
                        String msg = constraint.getMessage();
                        if (msg.contains("%s")) {
                            return String.format(msg, property);
                        }
                        return msg;
                    }).collect(Collectors.toList());

            ErrorDetail errorDetail = new ErrorDetail(property, messages);
            details.add(errorDetail);
        }

        log.info(CommonsErrorConstants.LOG_VALIDATION_MESSAGE, ex.getClass(), details);
        ApplicationResponse<Object> applicationResponse =
                this.getApplicationResponse(CommonsErrorConstants.REQUEST_VALIDATION_ERROR_CODE,
                        CommonsErrorConstants.REQUEST_VALIDATION_ERROR_MESSAGE, details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(applicationResponse);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    protected ResponseEntity<ApplicationResponse<Object>> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex) {
        String message = String.format(CommonsErrorConstants.TYPE_MISMATCH_PARAM_ERROR_MESSAGE,
                ex.getName());
        log.info(message);
        ApplicationResponse<Object> applicationResponse =
                this.getApplicationResponse(CommonsErrorConstants.TYPE_MISMATCH_PARAM_ERROR_CODE,
                        message, null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(applicationResponse);
    }

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<ApplicationResponse<Object>> handleException(Exception ex) {
        log.error(CommonsErrorConstants.LOG_ERROR_MESSAGE, ex.getMessage(), ex);
        ApplicationResponse<Object> applicationResponse =
                this.getApplicationResponse(CommonsErrorConstants.DEFAULT_SERVICE_ERROR_CODE,
                        CommonsErrorConstants.DEFAULT_SERVICE_ERROR_MESSAGE, null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(applicationResponse);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status,
            WebRequest request) {
        ApplicationResponse<Object> applicationResponse =
                this.getApplicationResponse(CommonsErrorConstants.METHOD_NOT_ALLOWED_ERROR_CODE,
                        String.format(
                                CommonsErrorConstants.METHOD_NOT_ALLOWED_ERROR_MESSAGES,
                                Arrays.toString(ex.getSupportedMethods())), null);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(applicationResponse);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status,
            WebRequest request) {
        String message = String.format(CommonsErrorConstants.REQUIRED_PARAM_ERROR_MESSAGE,
                ex.getParameterName());
        log.info(message);
        ApplicationResponse<Object> applicationResponse =
                this.getApplicationResponse(CommonsErrorConstants.REQUEST_VALIDATION_ERROR_CODE,
                        message, null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(applicationResponse);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String message =
                String.format(CommonsErrorConstants.TYPE_MISMATCH_PARAM_ERROR_MESSAGE,
                        ex.getPropertyName());
        log.info(message);
        ApplicationResponse<Object> applicationResponse =
                this.getApplicationResponse(CommonsErrorConstants.TYPE_MISMATCH_PARAM_ERROR_CODE,
                        message, null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(applicationResponse);
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(
            ServletRequestBindingException ex, HttpHeaders headers, HttpStatusCode status,
            WebRequest request) {
        return this.getGenericBadRequestApplicationResponse(Strings.EMPTY);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status,
            WebRequest request) {

        String message = this.getMessageForJacksonCause(ex.getCause());
        ApplicationResponse<Object> applicationResponse =
                this.getApplicationResponse(CommonsErrorConstants.REQUEST_GENERIC_ERROR_CODE,
                        message, null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(applicationResponse);
    }

    private String getMessageForJacksonCause(Throwable cause) {
        String message = CommonsErrorConstants.REQUEST_GENERIC_ERROR_MESSAGE;
        if (cause instanceof JsonProcessingException) {
            if (cause instanceof JsonParseException) {
                message = this.getMessageFromJsonParseException((JsonParseException) cause);
            } else if (cause instanceof JsonMappingException) {
                message = this.getMessageFromJsonMappingException(cause);
            }
        }

        return message;
    }

    private String getMessageFromJsonParseException(JsonParseException cause) {
        String message = CommonsErrorConstants.REQUEST_GENERIC_ERROR_MESSAGE;
        String fieldName = Optional.ofNullable(cause.getProcessor())
                .map(JsonParser::getParsingContext).map(
                        JsonStreamContext::getCurrentName).orElse("");
        if (!fieldName.isEmpty()) {
            message = String.format("Parameter %s contains invalid information", fieldName);
        }

        return message;
    }

    private String getMessageFromJsonMappingException(Throwable cause) {
        String message = CommonsErrorConstants.REQUEST_GENERIC_ERROR_MESSAGE;
        String fieldName = Strings.EMPTY;

        for (JsonMappingException.Reference path : ((JsonMappingException) cause).getPath()) {
            if (path.getFieldName() != null) {
                fieldName = fieldName.concat(path.getFieldName() + ".");
            } else {
                fieldName = fieldName.concat("[" + path.getIndex() + "].");
            }
        }

        fieldName = fieldName.substring(0, fieldName.length() - 1);

        if (fieldName.contains(".[")) {
            fieldName = fieldName.replace(".[", "[");
        }

        Throwable secondCause = Optional.of(cause).map(Throwable::getCause).orElse(cause);
        if (secondCause instanceof DateTimeParseException) {
            String format = (fieldName.contains("Time")) ? TIME_FORMAT : DATE_FORMAT;
            message = String.format("Parameter %s has a different format than expected '%s'",
                    fieldName, format);
        } else if (secondCause instanceof InvalidFormatException) {

            InvalidFormatException ifx = (InvalidFormatException) secondCause;
            if (ifx.getTargetType() != null && ifx.getTargetType().isEnum()) {
                message = String.format(
                        "Invalid value: '%s' for the field: '%s'. The value must be one of: %s.",
                        ifx.getValue(), ifx.getPath().get(ifx.getPath().size() - 1).getFieldName(),
                        Arrays.toString(ifx.getTargetType().getEnumConstants()));
            } else {
                message = String.format("The parameter %s has an invalid format", fieldName);
            }
        } else if (!fieldName.isEmpty()) {
            message = (!secondCause.getMessage().isEmpty()) ? secondCause.getMessage()
                    : String.format("Parameter %s contains invalid information", fieldName);
        }

        return message;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status,
            WebRequest request) {

        List<Object> filds =
                ex.getBindingResult().getAllErrors().stream().map(e -> e.getArguments()[0])
                        .distinct().toList();
        String field = "";

        List<ErrorDetail> details = new ArrayList<>();

        for (int i = 0; i < filds.size(); i++) {

            int finalI = i;
            List<ObjectError> objectErrors = ex.getBindingResult().getAllErrors().stream()
                    .filter(e -> e.getArguments()[0].equals(filds.get(finalI))).toList();

            if (!objectErrors.isEmpty() && objectErrors.get(0).getArguments() != null
                    && objectErrors.get(0).getArguments().length > 0) {

                field = ((DefaultMessageSourceResolvable) objectErrors.get(0).getArguments()[0])
                        .getDefaultMessage();
            }

            List<String> errorsDetails =
                    objectErrors.stream()
                            .map(this::generateArgumentNotValidErrorMessage)
                            .collect(Collectors.toList());

            if (Strings.isEmpty(field)) {
                field = "Request";
            }

            ErrorDetail errorDetail = new ErrorDetail(field, errorsDetails);
            details.add(errorDetail);
        }

        ApplicationResponse<Object> applicationResponse =
                this.getApplicationResponse(CommonsErrorConstants.REQUEST_VALIDATION_ERROR_CODE,
                        CommonsErrorConstants.REQUEST_VALIDATION_ERROR_MESSAGE, details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(applicationResponse);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(
            MissingServletRequestPartException ex, HttpHeaders headers, HttpStatusCode status,
            WebRequest request) {

        String message = String.format(CommonsErrorConstants.REQUIRED_PARAM_ERROR_MESSAGE,
                ex.getRequestPartName());
        log.info(message);
        ApplicationResponse<Object> applicationResponse =
                this.getApplicationResponse(CommonsErrorConstants.REQUEST_VALIDATION_ERROR_CODE,
                        message, null);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(applicationResponse);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatusCode status,
            WebRequest request) {
        ApplicationResponse<Object> applicationResponse =
                this.getApplicationResponse(CommonsErrorConstants.UNSUPPORTED_MEDIA_TYPE_ERROR_CODE,
                        CommonsErrorConstants.UNSUPPORTED_MEDIA_TYPE_ERROR_MESSAGE, null);
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(applicationResponse);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
            HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatusCode status,
            WebRequest request) {

        ApplicationResponse<Object> applicationResponse =
                this.getApplicationResponse(CommonsErrorConstants.UNSUPPORTED_MEDIA_TYPE_ERROR_CODE,
                        CommonsErrorConstants.UNSUPPORTED_MEDIA_TYPE_ERROR_MESSAGE, null);
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(applicationResponse);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return this.getGenericBadRequestApplicationResponse(Strings.EMPTY);
    }

    @Override
    protected ResponseEntity<Object> handleMaxUploadSizeExceededException(
            MaxUploadSizeExceededException ex, HttpHeaders headers, HttpStatusCode status,
            WebRequest request) {

        String message = "The uploaded files exceed the proposed limit. They must each weigh less"
                + " than 2 MB and together they cannot exceed 10 MB";
        ApplicationResponse<Object> applicationResponse =
                this.getApplicationResponse(CommonsErrorConstants.REQUEST_GENERIC_ERROR_CODE,
                        message, null);

        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(applicationResponse);
    }

    protected ApplicationResponse<Object> getApplicationResponse(String code,
            String message, List<ErrorDetail> details) {

        ErrorResponse errorResponse = ErrorResponse.builder().code(code).message(message)
                .timestamp(LocalDateTime.now())
                .details(details).build();

        return new ApplicationResponse<>(null, errorResponse);
    }

    protected ResponseEntity<Object> getGenericBadRequestApplicationResponse(String message) {
        ApplicationResponse<Object> applicationResponse =
                this.getApplicationResponse(CommonsErrorConstants.REQUEST_GENERIC_ERROR_CODE,
                        message.isEmpty() ? CommonsErrorConstants.REQUEST_GENERIC_ERROR_MESSAGE
                                : message,
                        null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(applicationResponse);
    }


    protected String generateArgumentNotValidErrorMessage(ObjectError objectError) {
        String message = CommonsErrorConstants.REQUEST_GENERIC_ERROR_MESSAGE;
        if (objectError.getArguments() != null && objectError.getArguments().length > 0) {
            String field = ((DefaultMessageSourceResolvable) objectError.getArguments()[0])
                    .getDefaultMessage();
            String defaultMessage = objectError.getDefaultMessage();
            if (defaultMessage != null) {
                message = String.format(defaultMessage, field);
            }
        }

        return message;
    }

}
