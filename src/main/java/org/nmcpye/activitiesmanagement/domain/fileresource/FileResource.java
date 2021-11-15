package org.nmcpye.activitiesmanagement.domain.fileresource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableSet;
import org.nmcpye.activitiesmanagement.extended.common.BaseIdentifiableObject;
import org.nmcpye.activitiesmanagement.extended.fileresource.FileResourceDomain;
import org.nmcpye.activitiesmanagement.extended.fileresource.FileResourceKeyUtil;
import org.nmcpye.activitiesmanagement.extended.fileresource.FileResourceStorageStatus;
import org.springframework.util.MimeTypeUtils;

import javax.persistence.*;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(name = "file_resource")
public class FileResource
    extends BaseIdentifiableObject {
    public static final String DEFAULT_FILENAME = "untitled";

    public static final String DEFAULT_CONTENT_TYPE = MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE;

    public static final Set<String> IMAGE_CONTENT_TYPES = ImmutableSet.of("image/jpg", "image/png", "image/jpeg");

    /**
     * MIME type.
     */
    @Column(name = "content_type")
    private String contentType;

    /**
     * Byte size of content, non negative.
     */
    @Column(name = "content_length")
    private long contentLength;

    /**
     * MD5 digest of content.
     */
    @Column(name = "content_md5")
    private String contentMd5;

    /**
     * Key used for content storage at external location.
     */
    @Column(name = "storage_key")
    private String storageKey;

    /**
     * Flag indicating whether the resource is assigned (e.g. to a DataValue) or
     * not. Unassigned FileResources are generally safe to delete when reaching
     * a certain age (unassigned objects might be in staging).
     */
    @Column(name = "assigned")
    private boolean assigned = false;

    /**
     * The domain which this FileResource belongs to.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "domain")
    private FileResourceDomain domain;

    /**
     * To keep track of those files which are not pre-generated and need to be
     * processed later. Flag will be set to true for FileResource having more
     * than one file associated with it (e.g images)
     */
    @Column(name = "has_multiple_storage_files")
    private boolean hasMultipleStorageFiles;

    /**
     * Current storage status of content.
     */
    private transient FileResourceStorageStatus storageStatus = FileResourceStorageStatus.NONE;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public FileResource() {
    }

    public FileResource(String name, String contentType, long contentLength, String contentMd5,
                        FileResourceDomain domain) {
        this.name = name;
        this.contentType = contentType;
        this.contentLength = contentLength;
        this.contentMd5 = contentMd5;
        this.domain = domain;
        this.storageKey = FileResourceKeyUtil.makeKey(domain, Optional.empty());
    }

    public FileResource(String key, String name, String contentType, long contentLength, String contentMd5,
                        FileResourceDomain domain) {
        this.name = name;
        this.contentType = contentType;
        this.contentLength = contentLength;
        this.contentMd5 = contentMd5;
        this.domain = domain;
        this.storageKey = FileResourceKeyUtil.makeKey(domain, Optional.of(key));
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty
    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @JsonProperty
    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    @JsonProperty
    public String getContentMd5() {
        return contentMd5;
    }

    public void setContentMd5(String contentMd5) {
        this.contentMd5 = contentMd5;
    }

    public String getStorageKey() {
        return storageKey;
    }

    public void setStorageKey(String storageKey) {
        this.storageKey = storageKey;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }

    @JsonProperty
    public FileResourceStorageStatus getStorageStatus() {
        return storageStatus;
    }

    public void setStorageStatus(FileResourceStorageStatus storageStatus) {
        this.storageStatus = storageStatus;
    }

    @JsonProperty
    public FileResourceDomain getDomain() {
        return domain;
    }

    public void setDomain(FileResourceDomain domain) {
        this.domain = domain;
    }

    public String getFormat() {
        return this.contentType.split("[/;]")[1];
    }

    @JsonProperty
    public boolean isHasMultipleStorageFiles() {
        return hasMultipleStorageFiles;
    }

    public void setHasMultipleStorageFiles(boolean hasMultipleStorageFiles) {
        this.hasMultipleStorageFiles = hasMultipleStorageFiles;
    }
}
