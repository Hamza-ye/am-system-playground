package org.nmcpye.activitiesmanagement.extended.dataset.hibernate;

import org.nmcpye.activitiesmanagement.domain.dataset.CasesReportClass;
import org.nmcpye.activitiesmanagement.extended.common.hibernate.HibernateIdentifiableObjectStore;
import org.nmcpye.activitiesmanagement.extended.dataset.CasesReportClassStore;
import org.nmcpye.activitiesmanagement.extended.serviceaclmodule.security.acl.AclService;
import org.nmcpye.activitiesmanagement.service.UserService;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by Hamza on 17-11-2021.
 */
public class CasesReportClassStoreImpl
    extends HibernateIdentifiableObjectStore<CasesReportClass>
    implements CasesReportClassStore {
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    public CasesReportClassStoreImpl(JdbcTemplate jdbcTemplate, UserService currentUserService,
                                     AclService aclService) {
        super(jdbcTemplate, CasesReportClass.class, currentUserService, aclService, true);
    }

    // -------------------------------------------------------------------------
    // CasesReportClass
    // -------------------------------------------------------------------------

}
