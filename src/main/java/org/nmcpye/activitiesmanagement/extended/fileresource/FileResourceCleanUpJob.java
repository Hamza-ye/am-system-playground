package org.nmcpye.activitiesmanagement.extended.fileresource;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.nmcpye.activitiesmanagement.domain.scheduling.JobConfiguration;
import org.nmcpye.activitiesmanagement.domain.fileresource.FileResource;
import org.nmcpye.activitiesmanagement.extended.common.DeleteNotAllowedException;
import org.nmcpye.activitiesmanagement.extended.scheduling.Job;
import org.nmcpye.activitiesmanagement.extended.scheduling.JobType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Deletes any orphaned FileResources. Queries for non-assigned or failed-upload
 * FileResources and deletes them from the database and/or file store.
 */
@Component("fileResourceCleanUpJob")
public class FileResourceCleanUpJob implements Job {
    private final Logger log = LoggerFactory.getLogger(FileResourceCleanUpJob.class);

    private final FileResourceService fileResourceService;

    private final FileResourceContentStore fileResourceContentStore;

    public FileResourceCleanUpJob(FileResourceService fileResourceService,
                                  FileResourceContentStore fileResourceContentStore) {
        this.fileResourceService = fileResourceService;
        this.fileResourceContentStore = fileResourceContentStore;
    }

    // -------------------------------------------------------------------------
    // Implementation
    // -------------------------------------------------------------------------

    @Override
    public JobType getJobType() {
        return JobType.FILE_RESOURCE_CLEANUP;
    }

    @Override
    public void execute(JobConfiguration jobConfiguration) {
        //        FileResourceRetentionStrategy retentionStrategy = systemSettingManager
//                .getSystemSetting( SettingKey.FILE_RESOURCE_RETENTION_STRATEGY, FileResourceRetentionStrategy.class );
        FileResourceRetentionStrategy retentionStrategy = FileResourceRetentionStrategy.NONE;

        List<Pair<String, String>> deletedOrphans = new ArrayList<>();

        List<Pair<String, String>> deletedAuditFiles = new ArrayList<>();

        // Delete expired FRs
        if (!FileResourceRetentionStrategy.FOREVER.equals(retentionStrategy)) {
            List<FileResource> expired = fileResourceService.getExpiredFileResources(retentionStrategy);
            expired.forEach(this::safeDelete);
            expired.forEach(fr -> deletedAuditFiles.add(ImmutablePair.of(fr.getName(), fr.getUid())));
        }

        // Delete failed uploads
        fileResourceService.getOrphanedFileResources().stream()
            .filter(fr -> !isFileStored(fr))
            .filter(this::safeDelete)
            .forEach(fr -> deletedOrphans.add(ImmutablePair.of(fr.getName(), fr.getUid())));

        if (!deletedOrphans.isEmpty()) {
            log.info(String.format("Deleted %d orphaned FileResources: %s", deletedOrphans.size(),
                prettyPrint(deletedOrphans)));
        }

        if (!deletedAuditFiles.isEmpty()) {
            log.info(String.format("Deleted %d expired FileResource audits: %s", deletedAuditFiles.size(),
                prettyPrint(deletedAuditFiles)));
        }
    }

    private String prettyPrint(List<Pair<String, String>> list) {
        if (list.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder("[ ");

        list.forEach(
            pair -> sb.append(pair.getLeft()).append(" , uid: ").append(pair.getRight()).append(", "));

        sb.deleteCharAt(sb.lastIndexOf(",")).append("]");

        return sb.toString();
    }

    private boolean isFileStored(FileResource fileResource) {
        return fileResourceContentStore.fileResourceContentExists(fileResource.getStorageKey());
    }

    /**
     * Attempts to delete a fileresource. Fixes the isAssigned status if it
     * turns out to be referenced by something else
     *
     * @param fileResource the fileresource to delete
     * @return true if the delete was successful
     */
    private boolean safeDelete(FileResource fileResource) {
        try {
            fileResourceService.deleteFileResource(fileResource);
            return true;
        } catch (DeleteNotAllowedException e) {
            fileResource.setAssigned(true);
            fileResourceService.updateFileResource(fileResource);
        }

        return false;
    }
}
