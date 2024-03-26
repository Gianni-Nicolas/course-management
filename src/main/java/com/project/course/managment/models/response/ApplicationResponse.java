package com.project.course.managment.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonPropertyOrder({"data", "errors"})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationResponse<T> {

    private T data;

    @JsonProperty("errors")
    private ErrorResponse errorResponse;

}
