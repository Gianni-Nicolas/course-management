package com.project.course.managment.services;

import static com.project.course.managment.constants.CustomExceptionConstants.INVALID_FILE_TYPE_ERROR_MESSAGE;
import static java.lang.String.format;

import com.google.cloud.storage.Blob;
import com.project.course.managment.constants.CustomExceptionConstants;
import com.project.course.managment.exceptions.DeleteStorageException;
import com.project.course.managment.exceptions.DocumentNotOwnedException;
import com.project.course.managment.exceptions.DocumentsAlreadyOwnedException;
import com.project.course.managment.exceptions.InvalidFileTypePdfException;
import com.project.course.managment.models.database.Course;
import com.project.course.managment.models.database.Document;
import com.project.course.managment.repositories.DocumentRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class DocumentServiceImpl implements IDocumentService {

    private static final String PDF_EXTENSION = ".pdf";

    private final IStorageService storageService;

    private final ICourseService courseService;

    private final DocumentRepository documentRepository;

    public DocumentServiceImpl(IStorageService storageService, ICourseService courseService,
            DocumentRepository documentRepository) {

        this.storageService = storageService;
        this.courseService = courseService;
        this.documentRepository = documentRepository;
    }

    @Override
    public List<String> uploadFilesByCourse(String directory, List<MultipartFile> files) {
        List<String> filesUpload = new ArrayList<>();

        Course course = courseService.findByName(directory);

        files.forEach(file -> {
            if (file.getContentType() == null ||
                    !file.getContentType().equals(MediaType.APPLICATION_PDF_VALUE)) {
                throw new InvalidFileTypePdfException(INVALID_FILE_TYPE_ERROR_MESSAGE);
            }
            Document document = new Document(file.getOriginalFilename());

            if (course.getDocuments().contains(document)) {
                throw new DocumentsAlreadyOwnedException(
                        CustomExceptionConstants.DOCUMENT_ALREADY_OWNED_ERROR_CODE,
                        String.format(CustomExceptionConstants.DOCUMENT_ALREADY_OWNED_ERROR_MESSAGE,
                                document.getName()));
            }

            try {
                Blob blob = storageService.uploadFile(file.getInputStream(),
                        file.getOriginalFilename(), directory);

                if (blob != null && !blob.getContentType().isBlank()) {
                    log.info("File [{}] uploaded successfully.", file.getOriginalFilename());
                    filesUpload.add(blob.getName().concat(" uploaded successfully"));
                    course.addDocument(document);
                }
            } catch (Exception e) {
                log.error("Error Occurred: [{}]", e.getMessage(), e);
                filesUpload.add(Objects.requireNonNull(file.getOriginalFilename())
                        .concat(" failed to upload"));
            }
        });
        courseService.save(course);

        return filesUpload;
    }

    @Override
    public String deleteFileOfCourse(String courseName, String filename) {

        Course course = courseService.findByName(courseName);

        if (!filename.contains(PDF_EXTENSION)) {
            throw new InvalidFileTypePdfException(INVALID_FILE_TYPE_ERROR_MESSAGE);
        }

        AtomicReference<Document> document = new AtomicReference<>();
        course.getDocuments().forEach(doc -> {
            if (doc.getName().equals(filename)) {
                document.set(doc);
            }
        });

        if (document.get() == null) {
            throw new DocumentNotOwnedException(
                    CustomExceptionConstants.DOCUMENT_NOT_OWNED_ERROR_CODE,
                    String.format(CustomExceptionConstants.DOCUMENT_NOT_OWNED_ERROR_MESSAGE,
                            filename));
        }

        boolean deleted = storageService.deleteFileOfCourse(course.getName(), filename);

        String result = format("File [%s] of course [%s] deleted %s", filename,
                course.getName(), deleted ? "successfully" : "failed");

        log.info(result);

        if (deleted) {
            course.removeDocument(document.get());
            courseService.save(course);
            documentRepository.delete(document.get());
        } else {
            throw new DeleteStorageException(
                    CustomExceptionConstants.DELETE_STORAGE_EXCEPTION_MESSAGE);
        }

        return result;
    }

    @Override
    public Optional<Blob> getFileByCourse(String course, String filename) {
        if (!filename.contains(PDF_EXTENSION)) {
            throw new InvalidFileTypePdfException(INVALID_FILE_TYPE_ERROR_MESSAGE);
        }
        courseService.findByName(course);

        return storageService.getFileByCourse(course, filename);
    }
}
