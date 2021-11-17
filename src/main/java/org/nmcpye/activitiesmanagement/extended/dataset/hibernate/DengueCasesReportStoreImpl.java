package org.nmcpye.activitiesmanagement.extended.dataset.hibernate;

import org.nmcpye.activitiesmanagement.domain.dataset.DengueCasesReport;
import org.nmcpye.activitiesmanagement.extended.common.hibernate.HibernateIdentifiableObjectStore;
import org.nmcpye.activitiesmanagement.extended.dataset.DengueCasesReportStore;
import org.nmcpye.activitiesmanagement.extended.serviceaclmodule.security.acl.AclService;
import org.nmcpye.activitiesmanagement.service.UserService;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by Hamza on 17-11-2021.
 */
public class DengueCasesReportStoreImpl extends HibernateIdentifiableObjectStore<DengueCasesReport> implements DengueCasesReportStore {
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    public DengueCasesReportStoreImpl(JdbcTemplate jdbcTemplate, UserService userService, AclService aclService) {
        super(jdbcTemplate, DengueCasesReport.class, userService, aclService, true);
    }
}
