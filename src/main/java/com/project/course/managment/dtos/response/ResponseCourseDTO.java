package com.project.course.managment.dtos.response;

import com.project.course.managment.models.database.Document;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseCourseDTO {

    private Long id;

    private String name;

    private String category;

    private String description;

    private String link;

    private Set<Document> documents;
}
