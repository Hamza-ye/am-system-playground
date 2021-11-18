package org.nmcpye.activitiesmanagement.extended.document.impl;

import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.extended.common.adapter.BaseIdentifiableObject_;
import org.nmcpye.activitiesmanagement.extended.common.hibernate.HibernateIdentifiableObjectStore;
import org.nmcpye.activitiesmanagement.domain.document.Document;
import org.nmcpye.activitiesmanagement.extended.document.DocumentStore;
import org.nmcpye.activitiesmanagement.extended.serviceaclmodule.security.acl.AclService;
import org.nmcpye.activitiesmanagement.service.UserService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository("org.nmcpye.activitiesmanagement.extended.document.DocumentStore")
public class HibernateDocumentStore
    extends HibernateIdentifiableObjectStore<Document> implements DocumentStore {
    public HibernateDocumentStore(JdbcTemplate jdbcTemplate, UserService currentUserService, AclService aclService) {
        super(jdbcTemplate, Document.class, currentUserService, aclService, true);
    }

    @Override
    public long getCountByUser(User user) {
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<Document> root = query.from(Document.class);
        query.select(builder.count(root));
        query.where(builder.equal(root.get(BaseIdentifiableObject_.CREATED_BY), user));

        return getSession().createQuery(query).getSingleResult();
    }
}
