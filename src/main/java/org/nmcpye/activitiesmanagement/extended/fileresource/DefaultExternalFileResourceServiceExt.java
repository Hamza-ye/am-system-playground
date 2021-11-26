package org.nmcpye.activitiesmanagement.extended.fileresource;

import org.nmcpye.activitiesmanagement.domain.fileresource.ExternalFileResource;
import org.nmcpye.activitiesmanagement.extended.common.CodeGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class DefaultExternalFileResourceServiceExt
    implements ExternalFileResourceServiceExt {
    private final ExternalFileResourceStore externalFileResourceStore;

    public DefaultExternalFileResourceServiceExt(ExternalFileResourceStore externalFileResourceStore) {
        checkNotNull(externalFileResourceStore);

        this.externalFileResourceStore = externalFileResourceStore;
    }

    @Override
    @Transactional(readOnly = true)
    public ExternalFileResource getExternalFileResourceByAccessToken(String accessToken) {
        return externalFileResourceStore.getExternalFileResourceByAccessToken(accessToken);
    }

    @Override
    @Transactional
    public String saveExternalFileResource(ExternalFileResource externalFileResource) {
        Assert.notNull(externalFileResource, "External file resource cannot be null");
        Assert.notNull(externalFileResource.getFileResource(), "External file resource entity cannot be null");

        externalFileResource.setAccessToken(CodeGenerator.getRandomUrlToken());

        externalFileResourceStore.saveObject(externalFileResource);

        return externalFileResource.getAccessToken();
    }
}
