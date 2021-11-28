package org.nmcpye.activitiesmanagement.extended.contentpage;

import org.nmcpye.activitiesmanagement.domain.ContentPage;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObjectStore;

/**
 * Created by Hamza on 17-11-2021.
 */
public interface ContentPageStore extends IdentifiableObjectStore<ContentPage> {
    String ID = ContentPageStore.class.getName();
}
