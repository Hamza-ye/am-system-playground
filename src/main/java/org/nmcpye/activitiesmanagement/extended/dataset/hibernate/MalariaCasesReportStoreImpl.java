package org.nmcpye.activitiesmanagement.extended.dataset.hibernate;

import org.nmcpye.activitiesmanagement.domain.dataset.MalariaCasesReport;
import org.nmcpye.activitiesmanagement.extended.common.hibernate.HibernateIdentifiableObjectStore;
import org.nmcpye.activitiesmanagement.extended.dataset.MalariaCasesReportStore;
import org.nmcpye.activitiesmanagement.extended.serviceaclmodule.security.acl.AclService;
import org.nmcpye.activitiesmanagement.service.UserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by Hamza on 17-11-2021.
 */
public class MalariaCasesReportStoreImpl
    extends HibernateIdentifiableObjectStore<MalariaCasesReport>
    implements MalariaCasesReportStore {
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    public MalariaCasesReportStoreImpl(JdbcTemplate jdbcTemplate, ApplicationEventPublisher publisher,
                                       UserService userService, AclService aclService) {
        super(jdbcTemplate, publisher, MalariaCasesReport.class, userService, aclService, true);
    }
}
