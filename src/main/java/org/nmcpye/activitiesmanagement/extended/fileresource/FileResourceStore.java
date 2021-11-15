package org.nmcpye.activitiesmanagement.extended.fileresource;

import org.joda.time.DateTime;
import org.nmcpye.activitiesmanagement.domain.fileresource.FileResource;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObjectStore;

import java.util.List;

public interface FileResourceStore extends IdentifiableObjectStore<FileResource> {
    List<FileResource> getExpiredFileResources(DateTime expires);

    List<FileResource> getAllUnProcessedImages();
}
