package org.nmcpye.activitiesmanagement.extended.fileresource;

import org.nmcpye.activitiesmanagement.domain.scheduling.JobConfiguration;
import org.nmcpye.activitiesmanagement.domain.fileresource.FileResource;
import org.nmcpye.activitiesmanagement.extended.common.DebugUtils;
import org.nmcpye.activitiesmanagement.extended.scheduling.Job;
import org.nmcpye.activitiesmanagement.extended.scheduling.JobType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Job will fetch all the image FileResources with flag hasMultiple set to
 * false. It will process those image FileResources create three images files
 * for each of them. Once created, images will be stored at EWS and flag
 * hasMultiple is set to true.
 */
@Component("imageResizingJob")
public class ImageResizingJob implements Job {
    private final Logger log = LoggerFactory.getLogger(ImageResizingJob.class);

    private final FileResourceContentStore fileResourceContentStore;

    private final FileResourceService fileResourceService;

    private final ImageProcessingService imageProcessingService;

    public ImageResizingJob(FileResourceContentStore fileResourceContentStore, FileResourceService fileResourceService,
                            ImageProcessingService imageProcessingService) {
        this.fileResourceContentStore = fileResourceContentStore;
        this.fileResourceService = fileResourceService;
        this.imageProcessingService = imageProcessingService;
    }

    @Override
    public JobType getJobType() {
        return JobType.IMAGE_PROCESSING;
    }

    @Override
    public void execute(JobConfiguration jobConfiguration) {
        List<FileResource> fileResources = fileResourceService.getAllUnProcessedImagesFiles();

        File tmpFile = null;

        String storageKey;

        int count = 0;

        for (FileResource fileResource : fileResources) {
            String key = fileResource.getStorageKey();

            tmpFile = new File(UUID.randomUUID().toString());

            if (!fileResourceContentStore.fileResourceContentExists(key)) {
                log.error("The referenced file could not be found for FileResource: " + fileResource.getUid());
                continue;
            }

            try (FileOutputStream fileOutputStream = new FileOutputStream(tmpFile)) {

                fileResourceContentStore.copyContent(key, fileOutputStream);

                Map<ImageFileDimension, File> imageFiles = imageProcessingService.createImages(fileResource, tmpFile);

                storageKey = fileResourceContentStore.saveFileResourceContent(fileResource, imageFiles);

                if (storageKey != null) {
                    fileResource.setHasMultipleStorageFiles(true);
                    fileResourceService.updateFileResource(fileResource);
                    count++;
                } else {
                    log.error("File upload failed");
                }
            } catch (Exception e) {
                DebugUtils.getStackTrace(e);
            } finally {
                try {
                    if (tmpFile != null) {
                        Files.deleteIfExists(tmpFile.toPath());
                    }
                } catch (IOException ioe) {
                    log.warn(String.format("Temporary file '%s' could not be deleted.", tmpFile.toPath()), ioe);
                }
            }
        }

        log.info(String.format("Number of FileResources processed: %d", count));
    }
}
