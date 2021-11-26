package org.nmcpye.activitiesmanagement.extended.document;

import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.document.Document;

import java.util.List;

public interface DocumentServiceExt {
    String ID = DocumentServiceExt.class.getName();

    String DIR = "documents";

    /**
     * Saves a Document.
     *
     * @param document the Document to save.
     * @return the generated identifier.
     */
    long saveDocument(Document document);

    /**
     * Retrieves the Document with the given identifier.
     *
     * @param id the identifier of the Document.
     * @return the Document.
     */
    Document getDocument(long id);

    /**
     * Retrieves the Document with the given identifier.
     *
     * @param uid the identifier of the Document.
     * @return the Document.
     */
    Document getDocument(String uid);

    /**
     * Used when removing a file reference from a Document.
     *
     * @param document
     */
    void deleteFileFromDocument(Document document);

    /**
     * Deletes a Document.
     *
     * @param document the Document to delete.
     */
    void deleteDocument(Document document);

    /**
     * Retrieves all Documents.
     *
     * @return a Collection of Documents.
     */
    List<Document> getAllDocuments();

    int getDocumentCount();

    int getDocumentCountByName(String name);

    List<Document> getDocumentsByUid(List<String> uids);

    long getCountDocumentByUser(User user);
}
