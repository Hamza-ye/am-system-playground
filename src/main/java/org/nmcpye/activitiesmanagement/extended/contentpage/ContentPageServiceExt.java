package org.nmcpye.activitiesmanagement.extended.contentpage;

import org.nmcpye.activitiesmanagement.domain.ContentPage;
import org.nmcpye.activitiesmanagement.domain.User;

import java.util.List;

/**
 * Created by Hamza on 17-11-2021.
 */
public interface ContentPageServiceExt {

    String ID = ContentPageServiceExt.class.getName();

    // -------------------------------------------------------------------------
    // Chv
    // -------------------------------------------------------------------------

    /**
     * Adds a ContentPage.
     *
     * @param contentPage The ContentPage to add.
     * @return The generated unique identifier for this ContentPage.
     */
    Long addContentPage(ContentPage contentPage);

    /**
     * Updates a ContentPage.
     *
     * @param contentPage The ContentPage to update.
     */
    void updateContentPage(ContentPage contentPage);

    /**
     * Deletes a ContentPage.
     *
     * @param contentPage The ContentPage to delete.
     */
    void deleteContentPage(ContentPage contentPage);

    /**
     * Get a ContentPage
     *
     * @param id The unique identifier for the ContentPage to get.
     * @return The ContentPage with the given id or null if it does not exist.
     */
    ContentPage getContentPage(Long id);

    /**
     * Returns the ContentPage with the given UID.
     *
     * @param uid the UID.
     * @return the ContentPage with the given UID, or null if no match.
     */
    ContentPage getContentPage(String uid);

    /**
     * Returns the ContentPage with the given UID. Bypasses the ACL system.
     *
     * @param uid the UID.
     * @return the ContentPage with the given UID, or null if no match.
     */
    ContentPage getContentPageNoAcl(String uid);

    /**
     * Get all ContentPages.
     *
     * @return A list containing all ContentPages.
     */
    List<ContentPage> getAllContentPages();

    /**
     * Returns the ContentPages which current user have READ access. If the current
     * user has the ALL authority then all ContentPages are returned.
     */
    List<ContentPage> getAllDataRead();

    /**
     * Returns the ContentPages which given user have READ access. If the current
     * user has the ALL authority then all ContentPages are returned.
     *
     * @param user the user to query for data set list.
     * @return a list of ContentPages which the given user has data read access to.
     */
    List<ContentPage> getUserDataRead(User user);

    /**
     * Returns the ContentPages which current user have WRITE access. If the
     * current user has the ALL authority then all ContentPages are returned.
     */
    List<ContentPage> getAllDataWrite();

    /**
     * Returns the ContentPages which current user have WRITE access. If the
     * current user has the ALL authority then all ContentPages are returned.
     *
     * @param user the user to query for data set list.
     * @return a list of ContentPages which given user has data write access to.
     */
    List<ContentPage> getUserDataWrite(User user);

}
