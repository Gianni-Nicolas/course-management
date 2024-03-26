package com.project.course.managment.constants.documentation;

public final class CourseControllerConstants {

    public static final String CREATE_COURSE_API_OPERATION = "Create new course";

    public static final String CREATE_COURSE_201 = "Course created successfully";

    public static final String CREATE_COURSE_400 = "Invalid body request";

    public static final String FIND_COURSE_BY_CATEGORY_OPERATION = "Find users by category";

    public static final String FIND_COURSE_BY_CATEGORY_200 = "Find users successfully";

    public static final String FIND_COURSE_BY_CATEGORY_400 = "Invalid request: category "
            + "is incorrect";

    public static final String DELETE_COURSE_API_OPERATION = "Delete course by name";

    public static final String DELETE_COURSE_200 = "Course deleted successfully";

    public static final String DELETE_COURSE_404 = "Invalid Request: course name not found";

    private CourseControllerConstants() {
    }
}
