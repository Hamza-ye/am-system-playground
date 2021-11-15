package org.nmcpye.activitiesmanagement.extended.fileresource;

import org.nmcpye.activitiesmanagement.domain.fileresource.ExternalFileResource;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObjectStore;

public interface ExternalFileResourceStore
    extends IdentifiableObjectStore<ExternalFileResource> {
    /**
     * Returns a single ExternalFileResource with the given (unique)
     * accessToken.
     *
     * @param accessToken unique string belonging to a single
     *                    ExternalFileResource.
     * @return ExternalFileResource
     */
    ExternalFileResource getExternalFileResourceByAccessToken(String accessToken);
}
