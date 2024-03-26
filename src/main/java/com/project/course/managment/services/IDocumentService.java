package com.project.course.managment.services;

import com.google.cloud.storage.Blob;
import java.util.List;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

public interface IDocumentService {

    List<String> uploadFilesByCourse(String directory, List<MultipartFile> files);

    String deleteFileOfCourse(String directory, String filename);

    Optional<Blob> getFileByCourse(String directory, String filename);
}
