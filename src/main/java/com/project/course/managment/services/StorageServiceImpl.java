package com.project.course.managment.services;

import static com.project.course.managment.constants.CommonsErrorConstants.DEFAULT_SERVICE_ERROR_MESSAGE;
import static com.project.course.managment.constants.CommonsErrorConstants.STORAGE_SERVICE_LOG_ERROR;
import static java.lang.String.format;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.project.course.managment.exceptions.StorageGeneralException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StorageServiceImpl implements IStorageService {

    public static final String PATH_STORAGE_FORMAT = "%s/%s";

    private final Storage storage;

    private final String bucketName;

    public StorageServiceImpl(final Storage storage,
            @Value("${gps.bucket.name}") final String bucketName) {
        this.storage = storage;
        this.bucketName = bucketName;
    }

    @Override
    public Optional<Blob> getFileByCourse(String directory, String filename) {
        try {
            String storagePath = format(PATH_STORAGE_FORMAT, directory, filename);

            BlobId blobId = BlobInfo
                    .newBuilder(bucketName, storagePath)
                    .build().getBlobId();

            Blob blob = storage.get(blobId);
            log.info("File [{}] fetched.", filename);
            return blob != null ? Optional.of(blob) : Optional.empty();

        } catch (Exception ex) {
            log.error(STORAGE_SERVICE_LOG_ERROR, ex.getMessage(), ex);
            throw new StorageGeneralException(DEFAULT_SERVICE_ERROR_MESSAGE);
        }
    }

    @Override
    public Blob uploadFile(InputStream inputStream, String filename, String directory)
            throws IOException {
        byte[] fileBytes = inputStream.readAllBytes();
        long fileSize = fileBytes.length;
        String contentType = Files.probeContentType(new File(filename).toPath());
        String storagePath = format(PATH_STORAGE_FORMAT, directory, filename);

        Map<String, String> metadata = new HashMap<>();
        metadata.put("filename", filename);
        metadata.put("content-type", contentType);
        metadata.put("content-length", String.valueOf(fileSize));

        BlobInfo blobInfo = BlobInfo
                .newBuilder(bucketName, storagePath)
                .setContentType(contentType)
                .setMetadata(metadata)
                .build();

        return storage.create(blobInfo, fileBytes);
    }

    @Override
    public boolean deleteFileOfCourse(String directory, String filename) {
        try {
            String storagePath = format(PATH_STORAGE_FORMAT, directory, filename);

            var blobId = BlobInfo
                    .newBuilder(bucketName, storagePath)
                    .build().getBlobId();

            return storage.delete(blobId);

        } catch (Exception ex) {
            log.error(STORAGE_SERVICE_LOG_ERROR, ex.getMessage(), ex);
            throw new StorageGeneralException(DEFAULT_SERVICE_ERROR_MESSAGE);
        }
    }

    @Override
    public boolean deleteFolder(String folderName) {
        try {
            AtomicBoolean delete = new AtomicBoolean(false);
            storage.list(bucketName, Storage.BlobListOption.prefix(folderName))
                    .iterateAll()
                    .forEach(blob -> delete.set(
                            storage.delete(BlobId.of(bucketName, blob.getName()))));
            return delete.get();

        } catch (Exception ex) {
            log.error(STORAGE_SERVICE_LOG_ERROR, ex.getMessage(), ex);
            throw new StorageGeneralException(DEFAULT_SERVICE_ERROR_MESSAGE);
        }
    }
}
