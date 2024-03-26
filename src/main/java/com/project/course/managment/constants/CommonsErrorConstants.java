package com.project.course.managment.constants;

public final class CommonsErrorConstants {

    public static final String DEFAULT_SERVICE_ERROR_CODE = "CM-ERR-099";

    public static final String DEFAULT_SERVICE_ERROR_MESSAGE = "Service running failed";

    public static final String EXTERNAL_API_ERROR_MESSAGE = "Error communicating with %s";

    public static final String INTERNAL_ERROR_CODE = "CM-ERR-001";

    public static final String INTERNAL_ERROR_MESSAGE = "Internal system error";

    public static final String METHOD_NOT_ALLOWED_ERROR_CODE = "CM-ERR-002";

    public static final String METHOD_NOT_ALLOWED_ERROR_MESSAGES =
            "The allowed HTTP methods are: %s";

    public static final String UNSUPPORTED_MEDIA_TYPE_ERROR_CODE = "CM-ERR-003";

    public static final String UNSUPPORTED_MEDIA_TYPE_ERROR_MESSAGE = "Content type not supported";

    public static final String REQUEST_GENERIC_ERROR_CODE = "CM-ERR-004";

    public static final String REQUEST_GENERIC_ERROR_MESSAGE = "The request is wrong";

    public static final String TYPE_MISMATCH_PARAM_ERROR_CODE = "CM-ERR-005";

    public static final String TYPE_MISMATCH_PARAM_ERROR_MESSAGE =
            "Parameter [%s] is not of the expected type";

    public static final String REQUEST_VALIDATION_ERROR_CODE = "CM-ERR-007";

    public static final String REQUEST_VALIDATION_ERROR_MESSAGE = "One or more fields are invalid";

    public static final String REQUIRED_PARAM_ERROR_MESSAGE =
            "The [%s] attribute is mandatory and must not be empty";


    public static final String LOG_ERROR_MESSAGE = "Messages: {} ";

    public static final String LOG_VALIDATION_MESSAGE = "Exception: {}. Messages: {}";

    public static final String STORAGE_SERVICE_LOG_ERROR = "Error Occurred: [{}]";

    private CommonsErrorConstants() {
    }
}
