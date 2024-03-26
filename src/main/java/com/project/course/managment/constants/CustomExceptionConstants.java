package com.project.course.managment.constants;

public final class CustomExceptionConstants {

    //***********************************************
    //**** ALREADY OWNED OR ALREADY TAKEN ERRORS ****
    //***********************************************

    public static final String COURSE_NAME_ALREADY_TAKEN_ERROR_CODE = "CM-ERR-100";

    public static final String COURSE_NAME_ALREADY_TAKEN_ERROR_MESSAGE = "There is already a "
            + "course with that name";

    public static final String DOCUMENT_ALREADY_OWNED_ERROR_CODE = "CM-ERR-101";

    public static final String DOCUMENT_ALREADY_OWNED_ERROR_MESSAGE = "The document named [%s] "
            + "already exists within the course";

    //*******************************************************
    //**** NOT OWNED OR NOT BELONG OR NOT ALLOWED ERRORS ****
    //*******************************************************

    public static final String DOCUMENT_NOT_OWNED_ERROR_CODE = "CM-ERR-200";

    public static final String DOCUMENT_NOT_OWNED_ERROR_MESSAGE = "The document named [%s] "
            + "not exists within the course";

    //**************************
    //**** NOT FOUND ERRORS ****
    //**************************

    public static final String COURSE_NOT_FOUND_ERROR_CODE = "CM-ERR-300";

    public static final String COURSE_NOT_FOUND_ERROR_MESSAGE = "Course [%s] does not exist. Enter "
            + "the name of a course that exists";

    public static final String PDF_NOT_FOUND_STORAGE_ERROR_CODE = "CM-ERR-301";

    public static final String PDF_NOT_FOUND_STORAGE_ERROR_MESSAGE = "The file [%s] was not found";

    //**************************
    //**** NOT VALID ERRORS ****
    //**************************
    public static final String INVALID_FILE_TYPE_ERROR_CODE = "CM-ERR-400";

    public static final String INVALID_FILE_TYPE_ERROR_MESSAGE = "The file type must be pdf";

    public static final String DELETE_STORAGE_EXCEPTION_CODE = "CM-ERR-401";

    public static final String DELETE_STORAGE_EXCEPTION_MESSAGE = "An unexpected error occurred "
            + "while trying to delete";


    private CustomExceptionConstants() {
    }
}
