package org.nmcpye.activitiesmanagement.extended.fileresource;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

public enum FileResourceDomain {
    DATA_VALUE("dataValue"),
    PUSH_ANALYSIS("pushAnalysis"),
    DOCUMENT("document"),
    MESSAGE_ATTACHMENT("messageAttachment"),
    USER_AVATAR("userAvatar"),
    ORG_UNIT("organisationUnit");

    /**
     * Container name to use when storing blobs of this FileResourceDomain
     */
    private String containerName;

    private static final ImmutableSet<FileResourceDomain> DOMAIN_FOR_MULTIPLE_IMAGES = ImmutableSet.of(
        DATA_VALUE,
        USER_AVATAR,
        ORG_UNIT);

    FileResourceDomain(String containerName) {
        this.containerName = containerName;
    }

    public String getContainerName() {
        return containerName;
    }

    public static Set<FileResourceDomain> getDomainForMultipleImages() {
        return DOMAIN_FOR_MULTIPLE_IMAGES;
    }
}
