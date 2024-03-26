package com.project.course.managment.converters;

import com.project.course.managment.dtos.response.ResponseCourseDTO;
import com.project.course.managment.models.database.Course;

public class CourseConverter {

    private CourseConverter() {
    }

    public static ResponseCourseDTO toResponseCourseDTO(Course course) {
        ResponseCourseDTO dto = new ResponseCourseDTO();

        dto.setId(course.getId());
        dto.setName(course.getName());
        dto.setCategory(course.getCategory());
        dto.setDescription(course.getDescription());
        dto.setLink(course.getLink());
        dto.setDocuments(course.getDocuments());

        return dto;
    }
}
