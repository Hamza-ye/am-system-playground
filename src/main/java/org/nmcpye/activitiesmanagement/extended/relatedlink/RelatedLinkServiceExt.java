package org.nmcpye.activitiesmanagement.extended.relatedlink;

import org.nmcpye.activitiesmanagement.domain.RelatedLink;
import org.nmcpye.activitiesmanagement.domain.User;

import java.util.List;

/**
 * Created by Hamza on 17-11-2021.
 */
public interface RelatedLinkServiceExt {

    String ID = RelatedLinkServiceExt.class.getName();

    // -------------------------------------------------------------------------
    // RelatedLink
    // -------------------------------------------------------------------------

    /**
     * Adds a RelatedLink.
     *
     * @param relatedLink The RelatedLink to add.
     * @return The generated unique identifier for this RelatedLink.
     */
    Long addRelatedLink(RelatedLink relatedLink);

    /**
     * Updates a RelatedLink.
     *
     * @param relatedLink The RelatedLink to update.
     */
    void updateRelatedLink(RelatedLink relatedLink);

    /**
     * Deletes a RelatedLink.
     *
     * @param relatedLink The RelatedLink to delete.
     */
    void deleteRelatedLink(RelatedLink relatedLink);

    /**
     * Get a RelatedLink
     *
     * @param id The unique identifier for the RelatedLink to get.
     * @return The RelatedLink with the given id or null if it does not exist.
     */
    RelatedLink getRelatedLink(Long id);

    /**
     * Returns the RelatedLink with the given UID.
     *
     * @param uid the UID.
     * @return the RelatedLink with the given UID, or null if no match.
     */
    RelatedLink getRelatedLink(String uid);

    /**
     * Returns the RelatedLink with the given UID. Bypasses the ACL system.
     *
     * @param uid the UID.
     * @return the RelatedLink with the given UID, or null if no match.
     */
    RelatedLink getRelatedLinkNoAcl(String uid);

    /**
     * Get all RelatedLinks.
     *
     * @return A list containing all RelatedLinks.
     */
    List<RelatedLink> getAllRelatedLinks();

    /**
     * Returns the RelatedLinks which current user have READ access. If the current
     * user has the ALL authority then all RelatedLinks are returned.
     */
    List<RelatedLink> getAllDataRead();

    /**
     * Returns the RelatedLinks which given user have READ access. If the current
     * user has the ALL authority then all RelatedLinks are returned.
     *
     * @param user the user to query for data set list.
     * @return a list of RelatedLinks which the given user has data read access to.
     */
    List<RelatedLink> getUserDataRead(User user);

    /**
     * Returns the RelatedLinks which current user have WRITE access. If the
     * current user has the ALL authority then all RelatedLinks are returned.
     */
    List<RelatedLink> getAllDataWrite();

    /**
     * Returns the RelatedLinks which current user have WRITE access. If the
     * current user has the ALL authority then all RelatedLinks are returned.
     *
     * @param user the user to query for data set list.
     * @return a list of RelatedLinks which given user has data write access to.
     */
    List<RelatedLink> getUserDataWrite(User user);

}
