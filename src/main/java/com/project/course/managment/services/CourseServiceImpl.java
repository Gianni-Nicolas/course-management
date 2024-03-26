package com.project.course.managment.services;

import static java.lang.String.format;

import com.project.course.managment.constants.CustomExceptionConstants;
import com.project.course.managment.converters.CourseConverter;
import com.project.course.managment.dtos.request.RequestCourseDTO;
import com.project.course.managment.dtos.response.ResponseCourseDTO;
import com.project.course.managment.exceptions.CourseNameAlreadyTakenException;
import com.project.course.managment.exceptions.CourseNotFoundException;
import com.project.course.managment.exceptions.DeleteStorageException;
import com.project.course.managment.models.database.Course;
import com.project.course.managment.repositories.CourseRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements ICourseService {

    private final CourseRepository courseRepository;

    private final IStorageService storageService;

    @Override
    public void save(Course course) {
        courseRepository.save(course);
    }

    @Override
    public Course findByName(String name) {
        return courseRepository.findByName(name).orElseThrow(
                () -> new CourseNotFoundException(
                        String.format(CustomExceptionConstants.COURSE_NOT_FOUND_ERROR_MESSAGE,
                                name))
        );
    }

    @Override
    public Course createCourse(RequestCourseDTO requestCourseDTO) {

        Optional<Course> course = courseRepository.findByName(requestCourseDTO.getName());
        if (course.isPresent()) {
            throw new CourseNameAlreadyTakenException(
                    CustomExceptionConstants.COURSE_NAME_ALREADY_TAKEN_ERROR_MESSAGE);
        }

        Course newCourse = new Course();
        newCourse.setName(requestCourseDTO.getName());
        newCourse.setDescription(requestCourseDTO.getDescription());
        newCourse.setCategory(requestCourseDTO.getCategory());
        newCourse.setLink(requestCourseDTO.getLink());

        return courseRepository.save(newCourse);
    }

    @Override
    public Page<ResponseCourseDTO> findCoursesByCategory(String category, PageRequest pageRequest) {
        Page<Course> courses = courseRepository.findByCategory(category, pageRequest);

        List<ResponseCourseDTO> responseUserDTO = new ArrayList<>();
        courses.forEach(c -> responseUserDTO.add(CourseConverter.toResponseCourseDTO(c)));
        return new PageImpl<>(responseUserDTO, courses.getPageable(), courses.getTotalElements());
    }

    @Override
    public String deleteCourse(String name) {
        Course course = findByName(name);
        boolean deleted;
        if (course.getDocuments().isEmpty()) {
            deleted = true;
        } else {
            deleted = storageService.deleteFolder(name);
        }

        String result = format("Course [%s] deleted %s", name,
                deleted ? "successfully" : "failed");

        if (deleted) {
            courseRepository.delete(course);
        } else {
            throw new DeleteStorageException(
                    CustomExceptionConstants.DELETE_STORAGE_EXCEPTION_MESSAGE);
        }

        log.info(result);

        return result;
    }
}
