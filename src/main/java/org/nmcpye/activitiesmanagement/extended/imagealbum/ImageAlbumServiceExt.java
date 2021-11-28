package org.nmcpye.activitiesmanagement.extended.imagealbum;

import org.nmcpye.activitiesmanagement.domain.ImageAlbum;
import org.nmcpye.activitiesmanagement.domain.User;

import java.util.List;

/**
 * Created by Hamza on 17-11-2021.
 */
public interface ImageAlbumServiceExt {

    String ID = ImageAlbumServiceExt.class.getName();

    // -------------------------------------------------------------------------
    // Chv
    // -------------------------------------------------------------------------

    /**
     * Adds a ImageAlbum.
     *
     * @param imageAlbum The ImageAlbum to add.
     * @return The generated unique identifier for this ImageAlbum.
     */
    Long addImageAlbum(ImageAlbum imageAlbum);

    /**
     * Updates a ImageAlbum.
     *
     * @param imageAlbum The ImageAlbum to update.
     */
    void updateImageAlbum(ImageAlbum imageAlbum);

    /**
     * Deletes a ImageAlbum.
     *
     * @param imageAlbum The ImageAlbum to delete.
     */
    void deleteImageAlbum(ImageAlbum imageAlbum);

    /**
     * Get a ImageAlbum
     *
     * @param id The unique identifier for the ImageAlbum to get.
     * @return The ImageAlbum with the given id or null if it does not exist.
     */
    ImageAlbum getImageAlbum(Long id);

    /**
     * Returns the ImageAlbum with the given UID.
     *
     * @param uid the UID.
     * @return the ImageAlbum with the given UID, or null if no match.
     */
    ImageAlbum getImageAlbum(String uid);

    /**
     * Returns the ImageAlbum with the given UID. Bypasses the ACL system.
     *
     * @param uid the UID.
     * @return the ImageAlbum with the given UID, or null if no match.
     */
    ImageAlbum getImageAlbumNoAcl(String uid);

    /**
     * Get all ImageAlbums.
     *
     * @return A list containing all ImageAlbums.
     */
    List<ImageAlbum> getAllImageAlbums();

    /**
     * Returns the ImageAlbums which current user have READ access. If the current
     * user has the ALL authority then all ImageAlbums are returned.
     */
    List<ImageAlbum> getAllDataRead();

    /**
     * Returns the ImageAlbums which given user have READ access. If the current
     * user has the ALL authority then all ImageAlbums are returned.
     *
     * @param user the user to query for data set list.
     * @return a list of ImageAlbums which the given user has data read access to.
     */
    List<ImageAlbum> getUserDataRead(User user);

    /**
     * Returns the ImageAlbums which current user have WRITE access. If the
     * current user has the ALL authority then all ImageAlbums are returned.
     */
    List<ImageAlbum> getAllDataWrite();

    /**
     * Returns the ImageAlbums which current user have WRITE access. If the
     * current user has the ALL authority then all ImageAlbums are returned.
     *
     * @param user the user to query for data set list.
     * @return a list of ImageAlbums which given user has data write access to.
     */
    List<ImageAlbum> getUserDataWrite(User user);

}
