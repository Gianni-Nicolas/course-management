package com.project.course.managment.controllers;

import com.project.course.managment.constants.CommonsErrorConstants;
import com.project.course.managment.constants.documentation.CourseControllerConstants;
import com.project.course.managment.converters.CourseConverter;
import com.project.course.managment.dtos.request.RequestCourseDTO;
import com.project.course.managment.dtos.response.ResponseCourseDTO;
import com.project.course.managment.models.response.ApplicationResponse;
import com.project.course.managment.models.response.ErrorResponse;
import com.project.course.managment.services.ICourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/courses")
@Tag(name = "Course Controller")
public class CourseController {

    private final ICourseService courseService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces =
            MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = CourseControllerConstants.CREATE_COURSE_API_OPERATION)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description =
                    CourseControllerConstants.CREATE_COURSE_201),
            @ApiResponse(responseCode = "400", description =
                    CourseControllerConstants.CREATE_COURSE_400,
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description =
                    CommonsErrorConstants.INTERNAL_ERROR_MESSAGE,
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationResponse<ResponseCourseDTO> createCourse(
            @Valid @RequestBody RequestCourseDTO requestCourseDTO) {

        log.info("POST /courses");

        ResponseCourseDTO response = CourseConverter.toResponseCourseDTO(
                courseService.createCourse(requestCourseDTO));

        return new ApplicationResponse<>(response, null);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = CourseControllerConstants.FIND_COURSE_BY_CATEGORY_OPERATION)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description =
                    CourseControllerConstants.FIND_COURSE_BY_CATEGORY_200),
            @ApiResponse(responseCode = "400", description =
                    CourseControllerConstants.FIND_COURSE_BY_CATEGORY_400,
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description =
                    CommonsErrorConstants.INTERNAL_ERROR_MESSAGE,
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @ResponseStatus(HttpStatus.OK)
    public ApplicationResponse<Page<ResponseCourseDTO>> getCoursesByCategory(
            @NotBlank(message = CommonsErrorConstants.REQUIRED_PARAM_ERROR_MESSAGE)
            @RequestParam(value = "category") String category,
            @RequestParam(required = false, value = "page") Optional<Integer> page,
            @RequestParam(required = false, value = "sortBy") Optional<String> sortBy,
            @RequestParam(required = false, value = "direction") Optional<Direction> direction,
            @RequestParam(required = false, value = "size") Optional<Integer> size
    ) {

        log.info("GET /courses");

        PageRequest pageRequest = PageRequest
                .of(page.orElse(0), size.orElse(25), direction.orElse(Direction.ASC),
                        sortBy.orElse("id"));

        return new ApplicationResponse<>(
                courseService.findCoursesByCategory(category, pageRequest), null);
    }

    @DeleteMapping
    @Operation(summary = CourseControllerConstants.DELETE_COURSE_API_OPERATION)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description =
                    CourseControllerConstants.DELETE_COURSE_200),
            @ApiResponse(responseCode = "404", description =
                    CourseControllerConstants.DELETE_COURSE_404,
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description =
                    CommonsErrorConstants.INTERNAL_ERROR_MESSAGE,
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @ResponseStatus(HttpStatus.OK)
    public ApplicationResponse<String> deleteAllDocumentsByCourse(
            @RequestParam("name") String name) {

        log.info("DELETE /courses");

        return new ApplicationResponse<>(courseService.deleteCourse(name), null);
    }


}
