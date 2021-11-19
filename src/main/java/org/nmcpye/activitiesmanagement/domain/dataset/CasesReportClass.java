package org.nmcpye.activitiesmanagement.domain.dataset;

import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonRootName;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.extended.common.BaseIdentifiableObject;
import org.nmcpye.activitiesmanagement.extended.common.DxfNamespaces;
import org.nmcpye.activitiesmanagement.extended.common.MetadataObject;

/**
 * A CasesReportClass.
 */
@Entity
@Table(name = "cases_report_class")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonRootName( value = "casesReportClass", namespace = DxfNamespaces.DXF_2_0 )
public class CasesReportClass extends BaseIdentifiableObject implements MetadataObject {

//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
//    @SequenceGenerator(name = "sequenceGenerator")
//    private Long id;
//
//    @NotNull
//    @Size(max = 11)
//    @Column(name = "uid", length = 11, nullable = false, unique = true)
//    private String uid;
//
//    @Column(name = "code", unique = true)
//    private String code;
//
//    @Column(name = "name")
//    private String name;

    @Size(max = 50)
    @Column(name = "short_name", length = 50)
    private String shortName;

    @Column(name = "description")
    private String description;
//
//    @Column(name = "created")
//    private Instant created;
//
//    @Column(name = "last_updated")
//    private Instant lastUpdated;

//    @ManyToOne
//    private User user;
//
//    @ManyToOne
//    private User lastUpdatedBy;

    public CasesReportClass id(Long id) {
        this.id = id;
        return this;
    }

    public CasesReportClass uid(String uid) {
        this.uid = uid;
        return this;
    }

    public CasesReportClass code(String code) {
        this.code = code;
        return this;
    }

    public CasesReportClass name(String name) {
        this.name = name;
        return this;
    }

    public String getShortName() {
        return this.shortName;
    }

    public CasesReportClass shortName(String shortName) {
        this.shortName = shortName;
        return this;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getDescription() {
        return this.description;
    }

    public CasesReportClass description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CasesReportClass created(Date created) {
        this.created = created;
        return this;
    }

    public CasesReportClass lastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public CasesReportClass user(User user) {
        this.setUser(user);
        return this;
    }

    public CasesReportClass lastUpdatedBy(User user) {
        this.setLastUpdatedBy(user);
        return this;
    }
}
