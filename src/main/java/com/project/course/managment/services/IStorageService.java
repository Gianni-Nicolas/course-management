package com.project.course.managment.services;

import com.google.cloud.storage.Blob;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public interface IStorageService {

    Optional<Blob> getFileByCourse(String directory, String filename);

    Blob uploadFile(InputStream inputStream, String filename, String directory)
            throws IOException;

    boolean deleteFileOfCourse(String directory, String filename);

    boolean deleteFolder(String folderName);
}
