package org.nmcpye.activitiesmanagement.extended.imagealbum.hibernate;

import org.nmcpye.activitiesmanagement.domain.ImageAlbum;
import org.nmcpye.activitiesmanagement.extended.common.hibernate.HibernateIdentifiableObjectStore;
import org.nmcpye.activitiesmanagement.extended.imagealbum.ImageAlbumStore;
import org.nmcpye.activitiesmanagement.extended.serviceaclmodule.security.acl.AclService;
import org.nmcpye.activitiesmanagement.service.UserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Created by Hamza on 17/11/2021.
 */
@Repository
public class ImageAlbumStoreImpl
    extends HibernateIdentifiableObjectStore<ImageAlbum>
    implements ImageAlbumStore {
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    public ImageAlbumStoreImpl(JdbcTemplate jdbcTemplate, ApplicationEventPublisher publisher,
                               UserService userService, AclService aclService) {
        super(jdbcTemplate, publisher, ImageAlbum.class, userService, aclService, true);
    }
}
