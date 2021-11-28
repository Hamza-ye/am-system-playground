package org.nmcpye.activitiesmanagement.extended.relatedlink;

import org.nmcpye.activitiesmanagement.domain.RelatedLink;
import org.nmcpye.activitiesmanagement.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Hamza on 17-11-2021.
 */
@Service
public class DefaultRelatedLinkService implements RelatedLinkServiceExt {
    private final RelatedLinkStore relatedLinkStore;

    public DefaultRelatedLinkService(RelatedLinkStore relatedLinkStore) {
        this.relatedLinkStore = relatedLinkStore;
    }

    @Override
    public Long addRelatedLink(RelatedLink relatedLink) {
        relatedLinkStore.saveObject(relatedLink);
        return relatedLink.getId();
    }

    @Override
    public void updateRelatedLink(RelatedLink relatedLink) {
        relatedLinkStore.update(relatedLink);
    }

    @Override
    public void deleteRelatedLink(RelatedLink relatedLink) {
        relatedLinkStore.delete(relatedLink);
    }

    @Override
    public RelatedLink getRelatedLink(Long id) {
        return relatedLinkStore.get(id);
    }

    @Override
    public RelatedLink getRelatedLink(String uid) {
        return relatedLinkStore.getByUid(uid);
    }

    @Override
    public RelatedLink getRelatedLinkNoAcl(String uid) {
        return relatedLinkStore.getByUidNoAcl(uid);
    }

    @Override
    public List<RelatedLink> getAllRelatedLinks() {
        return relatedLinkStore.getAll();
    }

    @Override
    public List<RelatedLink> getAllDataRead() {
        return relatedLinkStore.getDataReadAll();
    }

    @Override
    public List<RelatedLink> getUserDataRead(User user) {
        return relatedLinkStore.getDataReadAll(user);
    }

    @Override
    public List<RelatedLink> getAllDataWrite() {
        return relatedLinkStore.getDataWriteAll();
    }

    @Override
    public List<RelatedLink> getUserDataWrite(User user) {
        return relatedLinkStore.getDataWriteAll(user);
    }
}
