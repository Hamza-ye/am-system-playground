package org.nmcpye.activitiesmanagement.extended.relatedlink;

import org.nmcpye.activitiesmanagement.domain.RelatedLink;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObjectStore;

/**
 * Created by Hamza on 17-11-2021.
 */
public interface RelatedLinkStore extends IdentifiableObjectStore<RelatedLink> {
    String ID = RelatedLinkStore.class.getName();
}
