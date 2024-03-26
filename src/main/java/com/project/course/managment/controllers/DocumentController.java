package com.project.course.managment.controllers;

import static com.project.course.managment.constants.CustomExceptionConstants.PDF_NOT_FOUND_STORAGE_ERROR_MESSAGE;

import com.google.cloud.storage.Blob;
import com.project.course.managment.constants.CommonsErrorConstants;
import com.project.course.managment.constants.documentation.DocumentControllerConstants;
import com.project.course.managment.exceptions.DocumentNotFoundStorageException;
import com.project.course.managment.models.response.ApplicationResponse;
import com.project.course.managment.models.response.ErrorResponse;
import com.project.course.managment.services.IDocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/courses/document/")
@Tag(name = "Document Controller")
public class DocumentController {

    private final IDocumentService storageService;

    @PostMapping(path = "/upload")
    @Operation(summary = DocumentControllerConstants.UPLOAD_DOCUMENT_API_OPERATION)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description =
                    DocumentControllerConstants.UPLOAD_DOCUMENT_200),
            @ApiResponse(responseCode = "400", description =
                    DocumentControllerConstants.UPLOAD_DOCUMENT_400,
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description =
                    CommonsErrorConstants.INTERNAL_ERROR_MESSAGE,
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @ResponseStatus(HttpStatus.OK)
    public ApplicationResponse<List<String>> uploadDocumentByCourse(
            @RequestParam("course") String course,
            @NotNull(message = CommonsErrorConstants.REQUIRED_PARAM_ERROR_MESSAGE)
            @RequestParam("files") List<MultipartFile> multiPartFiles) {

        log.info("POST /courses/document/upload");

        return new ApplicationResponse<>(storageService.uploadFilesByCourse(course, multiPartFiles),
                null);
    }

    @GetMapping(path = "/download", produces = MediaType.APPLICATION_PDF_VALUE)
    @Operation(summary = DocumentControllerConstants.DOWNLOAD_DOCUMENT_API_OPERATION)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description =
                    DocumentControllerConstants.DOWNLOAD_DOCUMENT_200),
            @ApiResponse(responseCode = "404", description =
                    DocumentControllerConstants.DOWNLOAD_DOCUMENT_404,
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description =
                    CommonsErrorConstants.INTERNAL_ERROR_MESSAGE,
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<byte[]> downloadAllDocumentsByCourse(
            @RequestParam("course") String course,
            @RequestParam("filename") String filename) {

        log.info("GET /courses/document/download");

        Blob file = storageService.getFileByCourse(course, filename).orElseThrow(() ->
                new DocumentNotFoundStorageException(
                        String.format(PDF_NOT_FOUND_STORAGE_ERROR_MESSAGE, filename))
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename(file.getName()).build());
        headers.setContentLength(file.getContent().length);

        return new ResponseEntity<>(file.getContent(), headers, HttpStatus.OK);
    }


    @DeleteMapping(path = "/remove")
    @Operation(summary = DocumentControllerConstants.DELETE_DOCUMENT_API_OPERATION)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description =
                    DocumentControllerConstants.DELETE_DOCUMENT_200),
            @ApiResponse(responseCode = "404", description =
                    DocumentControllerConstants.DELETE_DOCUMENT_404,
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description =
                    CommonsErrorConstants.INTERNAL_ERROR_MESSAGE,
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @ResponseStatus(HttpStatus.OK)
    public ApplicationResponse<String> deleteAllDocumentsByCourse(
            @RequestParam("course") String course,
            @RequestParam("filename") String filename) {

        log.info("DELETE /courses/document/remove");

        return new ApplicationResponse<>(storageService.deleteFileOfCourse(course, filename), null);
    }

}
