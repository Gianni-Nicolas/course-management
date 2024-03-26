package com.project.course.managment.models.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"code", "message", "details", "timestamp"})
public class ErrorResponse {

    private String code;

    private String message;

    private LocalDateTime timestamp;

    private List<ErrorDetail> details;
}
