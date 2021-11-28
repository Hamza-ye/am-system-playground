package org.nmcpye.activitiesmanagement.extended.imagealbum;

import org.nmcpye.activitiesmanagement.domain.ImageAlbum;
import org.nmcpye.activitiesmanagement.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Hamza on 17/11/2021.
 */
@Service
public class DefaultImageAlbumService implements ImageAlbumServiceExt {

    private final ImageAlbumStore imageAlbumStore;

    public DefaultImageAlbumService(ImageAlbumStore imageAlbumStore) {
        this.imageAlbumStore = imageAlbumStore;
    }

    @Override
    public Long addImageAlbum(ImageAlbum demographicData) {
        imageAlbumStore.saveObject(demographicData);
        return demographicData.getId();
    }

    @Override
    public void updateImageAlbum(ImageAlbum demographicData) {
        imageAlbumStore.update(demographicData);
    }

    @Override
    public void deleteImageAlbum(ImageAlbum demographicData) {
        imageAlbumStore.delete(demographicData);
    }

    @Override
    public ImageAlbum getImageAlbum(Long id) {
        return imageAlbumStore.get(id);
    }

    @Override
    public ImageAlbum getImageAlbum(String uid) {
        return imageAlbumStore.getByUid(uid);
    }

    @Override
    public ImageAlbum getImageAlbumNoAcl(String uid) {
        return imageAlbumStore.getByUidNoAcl(uid);
    }

    @Override
    public List<ImageAlbum> getAllImageAlbums() {
        return imageAlbumStore.getAll();
    }

    @Override
    public List<ImageAlbum> getAllDataRead() {
        return imageAlbumStore.getDataReadAll();
    }

    @Override
    public List<ImageAlbum> getUserDataRead(User user) {
        return imageAlbumStore.getDataReadAll(user);
    }

    @Override
    public List<ImageAlbum> getAllDataWrite() {
        return imageAlbumStore.getDataWriteAll();
    }

    @Override
    public List<ImageAlbum> getUserDataWrite(User user) {
        return imageAlbumStore.getDataWriteAll(user);
    }
}
