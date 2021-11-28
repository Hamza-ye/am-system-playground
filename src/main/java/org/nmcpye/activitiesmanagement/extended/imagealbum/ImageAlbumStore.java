package org.nmcpye.activitiesmanagement.extended.imagealbum;

import org.nmcpye.activitiesmanagement.domain.ImageAlbum;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObjectStore;

/**
 * Created by Hamza on 17-11-2021.
 */
public interface ImageAlbumStore extends IdentifiableObjectStore<ImageAlbum> {
    String ID = ImageAlbumStore.class.getName();
}
