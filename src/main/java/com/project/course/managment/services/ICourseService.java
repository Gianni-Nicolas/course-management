package com.project.course.managment.services;

import com.project.course.managment.dtos.request.RequestCourseDTO;
import com.project.course.managment.dtos.response.ResponseCourseDTO;
import com.project.course.managment.models.database.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ICourseService {

    void save(Course course);

    Course findByName(String name);

    Course createCourse(RequestCourseDTO requestCourseDTO);

    Page<ResponseCourseDTO> findCoursesByCategory(String category, PageRequest pageRequest);

    String deleteCourse(String name);
}
