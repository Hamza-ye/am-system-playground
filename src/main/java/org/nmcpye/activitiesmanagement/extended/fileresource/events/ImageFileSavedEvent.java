package org.nmcpye.activitiesmanagement.extended.fileresource.events;

import org.nmcpye.activitiesmanagement.extended.fileresource.ImageFileDimension;

import java.io.File;
import java.util.Map;

public class ImageFileSavedEvent {
    private String fileResource;

    private Map<ImageFileDimension, File> imageFiles;

    public ImageFileSavedEvent(String fileResource, Map<ImageFileDimension, File> imageFiles) {
        this.fileResource = fileResource;
        this.imageFiles = imageFiles;
    }

    public String getFileResource() {
        return fileResource;
    }

    public Map<ImageFileDimension, File> getImageFiles() {
        return imageFiles;
    }
}
