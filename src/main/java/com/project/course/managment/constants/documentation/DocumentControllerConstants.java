package com.project.course.managment.constants.documentation;

public final class DocumentControllerConstants {

    public static final String UPLOAD_DOCUMENT_API_OPERATION = "Create new course";

    public static final String UPLOAD_DOCUMENT_200 = "File uploaded successfully";

    public static final String UPLOAD_DOCUMENT_400 = "Invalid Request: course name o file "
            + "type incorrect";

    public static final String DOWNLOAD_DOCUMENT_API_OPERATION = "Download document by course and"
            + " filename";

    public static final String DOWNLOAD_DOCUMENT_200 = "Document downloaded successfully";

    public static final String DOWNLOAD_DOCUMENT_404 = "Invalid Request: course or filename "
            + "not found";

    public static final String DELETE_DOCUMENT_API_OPERATION = "Delete document by course and"
            + " filename";

    public static final String DELETE_DOCUMENT_200 = "Document delete successfully";

    public static final String DELETE_DOCUMENT_404 = "Invalid Request: course or filename "
            + "not found";

    private DocumentControllerConstants() {

    }

}
