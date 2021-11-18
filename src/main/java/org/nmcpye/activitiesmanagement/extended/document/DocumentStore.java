package org.nmcpye.activitiesmanagement.extended.document;

import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.document.Document;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObjectStore;

public interface DocumentStore
    extends IdentifiableObjectStore<Document> {
    long getCountByUser(User user);
}
