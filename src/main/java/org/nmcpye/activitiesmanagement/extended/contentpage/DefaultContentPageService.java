package org.nmcpye.activitiesmanagement.extended.contentpage;

import org.nmcpye.activitiesmanagement.domain.ContentPage;
import org.nmcpye.activitiesmanagement.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Hamza on 17/11/2021.
 */
@Service
public class DefaultContentPageService implements ContentPageServiceExt {

    private final ContentPageStore contentPageStore;

    public DefaultContentPageService(ContentPageStore contentPageStore) {
        this.contentPageStore = contentPageStore;
    }

    @Override
    public Long addContentPage(ContentPage demographicData) {
        contentPageStore.saveObject(demographicData);
        return demographicData.getId();
    }

    @Override
    public void updateContentPage(ContentPage demographicData) {
        contentPageStore.update(demographicData);
    }

    @Override
    public void deleteContentPage(ContentPage demographicData) {
        contentPageStore.delete(demographicData);
    }

    @Override
    public ContentPage getContentPage(Long id) {
        return contentPageStore.get(id);
    }

    @Override
    public ContentPage getContentPage(String uid) {
        return contentPageStore.getByUid(uid);
    }

    @Override
    public ContentPage getContentPageNoAcl(String uid) {
        return contentPageStore.getByUidNoAcl(uid);
    }

    @Override
    public List<ContentPage> getAllContentPages() {
        return contentPageStore.getAll();
    }

    @Override
    public List<ContentPage> getAllDataRead() {
        return contentPageStore.getDataReadAll();
    }

    @Override
    public List<ContentPage> getUserDataRead(User user) {
        return contentPageStore.getDataReadAll(user);
    }

    @Override
    public List<ContentPage> getAllDataWrite() {
        return contentPageStore.getDataWriteAll();
    }

    @Override
    public List<ContentPage> getUserDataWrite(User user) {
        return contentPageStore.getDataWriteAll(user);
    }
}
