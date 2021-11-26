package org.nmcpye.activitiesmanagement.extended.fileresource;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormat;
import org.nmcpye.activitiesmanagement.domain.fileresource.FileResource;
import org.nmcpye.activitiesmanagement.extended.fileresource.events.BinaryFileSavedEvent;
import org.nmcpye.activitiesmanagement.extended.fileresource.events.FileDeletedEvent;
import org.nmcpye.activitiesmanagement.extended.fileresource.events.FileSavedEvent;
import org.nmcpye.activitiesmanagement.extended.fileresource.events.ImageFileSavedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.io.File;
import java.util.Map;
import java.util.stream.Stream;

@Component("org.nmcpye.activitiesmanagement.extended.fileresource.FileResourceEventListener")
public class FileResourceEventListener {
    private final Logger log = LoggerFactory.getLogger(FileResourceEventListener.class);

    private final FileResourceServiceExt fileResourceServiceExt;

    private final FileResourceContentStore fileResourceContentStore;

    public FileResourceEventListener(FileResourceServiceExt fileResourceServiceExt, FileResourceContentStore contentStore) {
        this.fileResourceServiceExt = fileResourceServiceExt;
        this.fileResourceContentStore = contentStore;
    }

    @TransactionalEventListener
    @Async
    public void save(FileSavedEvent fileSavedEvent) {
        DateTime startTime = DateTime.now();

        File file = fileSavedEvent.getFile();

        FileResource fileResource = fileResourceServiceExt.getFileResource(fileSavedEvent.getFileResource());

        String storageId = fileResourceContentStore.saveFileResourceContent(fileResource, file);

        Period timeDiff = new Period(startTime, DateTime.now());

        logMessage(storageId, fileResource, timeDiff);
    }

    @TransactionalEventListener
    @Async
    public void saveImageFile(ImageFileSavedEvent imageFileSavedEvent) {
        DateTime startTime = DateTime.now();

        Map<ImageFileDimension, File> imageFiles = imageFileSavedEvent.getImageFiles();

        FileResource fileResource = fileResourceServiceExt.getFileResource(imageFileSavedEvent.getFileResource());

        String storageId = fileResourceContentStore.saveFileResourceContent(fileResource, imageFiles);

        if (storageId != null) {
            fileResource.setHasMultipleStorageFiles(true);

            fileResourceServiceExt.updateFileResource(fileResource);
        }

        Period timeDiff = new Period(startTime, DateTime.now());

        logMessage(storageId, fileResource, timeDiff);
    }

    @TransactionalEventListener
    @Async
    public void saveBinaryFile(BinaryFileSavedEvent binaryFileSavedEvent) {
        DateTime startTime = DateTime.now();

        byte[] bytes = binaryFileSavedEvent.getBytes();

        FileResource fileResource = fileResourceServiceExt.getFileResource(binaryFileSavedEvent.getFileResource());

        String storageId = fileResourceContentStore.saveFileResourceContent(fileResource, bytes);

        Period timeDiff = new Period(startTime, DateTime.now());

        logMessage(storageId, fileResource, timeDiff);
    }

    @TransactionalEventListener
    @Async
    public void deleteFile(FileDeletedEvent deleteFileEvent) {
        if (!fileResourceContentStore.fileResourceContentExists(deleteFileEvent.getStorageKey())) {
            log.error(String.format("No file exist for key: %s", deleteFileEvent.getStorageKey()));
            return;
        }

        if (FileResource.IMAGE_CONTENT_TYPES.contains(deleteFileEvent.getContentType())
            && FileResourceDomain.getDomainForMultipleImages().contains(deleteFileEvent.getDomain())) {
            String storageKey = deleteFileEvent.getStorageKey();

            Stream.of(ImageFileDimension.values()).forEach(d -> fileResourceContentStore
                .deleteFileResourceContent(StringUtils.join(storageKey, d.getDimension())));
        } else {
            fileResourceContentStore.deleteFileResourceContent(deleteFileEvent.getStorageKey());
        }
    }

    private void logMessage(String storageId, FileResource fileResource, Period timeDiff) {
        if (storageId == null) {
            log.error(String.format("Saving content for file resource failed: %s", fileResource.getUid()));
            return;
        }

        log.info(String.format("File stored with key: %s'. Upload finished in %s", storageId,
            timeDiff.toString(PeriodFormat.getDefault())));
    }
}
