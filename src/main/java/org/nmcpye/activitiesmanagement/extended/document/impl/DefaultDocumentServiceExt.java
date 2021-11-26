package org.nmcpye.activitiesmanagement.extended.document.impl;

import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.fileresource.FileResource;
import org.nmcpye.activitiesmanagement.domain.document.Document;
import org.nmcpye.activitiesmanagement.extended.document.DocumentServiceExt;
import org.nmcpye.activitiesmanagement.extended.document.DocumentStore;
import org.nmcpye.activitiesmanagement.extended.fileresource.FileResourceServiceExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class DefaultDocumentServiceExt
    implements DocumentServiceExt {
    @Autowired
    private FileResourceServiceExt fileResourceServiceExt;

    @Autowired
    private DocumentStore documentStore;

    // -------------------------------------------------------------------------
    // DocumentServiceExt implementation
    // -------------------------------------------------------------------------

    @Override
    public long saveDocument(Document document) {
        documentStore.saveObject(document);

        return document.getId();
    }

    @Override
    public Document getDocument(long id) {
        return documentStore.get(id);
    }

    @Override
    public Document getDocument(String uid) {
        return documentStore.getByUid(uid);
    }

    @Override
    public void deleteFileFromDocument(Document document) {
        FileResource fileResource = document.getFileResource();

        // Remove reference to fileResource from document to avoid db constraint
        // exception
        document.setFileResource(null);
        documentStore.saveObject(document);

        // Delete file
        fileResourceServiceExt.deleteFileResource(fileResource.getUid());
    }

    @Override
    public void deleteDocument(Document document) {
        documentStore.delete(document);
    }

    @Override
    public List<Document> getAllDocuments() {
        return documentStore.getAll();
    }

    @Override
    public int getDocumentCount() {
        return documentStore.getCount();
    }

    @Override
    public int getDocumentCountByName(String name) {
        return documentStore.getCountLikeName(name);
    }

    @Override
    public List<Document> getDocumentsByUid(List<String> uids) {
        return documentStore.getByUid(uids);
    }

    @Override
    public long getCountDocumentByUser(User user) {
        return documentStore.getCountByUser(user);
    }
}
