package org.nmcpye.activitiesmanagement.domain.chv;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nmcpye.activitiesmanagement.domain.ChvTeam;
import org.nmcpye.activitiesmanagement.domain.ContentPage;
import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnit;
import org.nmcpye.activitiesmanagement.domain.person.Person;
import org.nmcpye.activitiesmanagement.extended.common.BaseIdentifiableObject;
import org.nmcpye.activitiesmanagement.extended.common.DxfNamespaces;
import org.nmcpye.activitiesmanagement.extended.common.MetadataObject;

/**
 * A Chv.
 */
@Entity
@Table(name = "chv")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonRootName( value = "chv", namespace = DxfNamespaces.DXF_2_0 )
public class Chv extends BaseIdentifiableObject implements MetadataObject {

    ////////////////////////
    ///
    /// Common Columns
    ///
    ////////////////////////

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 11)
    @Column(name = "uid", length = 11, nullable = false, unique = true)
    private String uid;

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "created")
    private Date created;

    @Column(name = "last_updated")
    private Date lastUpdated;

    /**
     * Owner of this object.
     */
    @ManyToOne
    @JoinColumn(name = "created_by")
    protected User createdBy;

    /**
     * Last user updated this object.
     */
    @ManyToOne
    @JoinColumn(name = "last_updated_by")
    protected User lastUpdatedBy;

    ////////////////////////
    ///
    ///
    ////////////////////////

    @Column(name = "description")
    private String description;

    @Column(name = "mobile")
    private String mobile;

    @JsonIgnoreProperties(
        value = {
            "userInfo", "user", "createdBy", "lastUpdatedBy", "organisationUnits", "dataViewOrganisationUnits", "personAuthorityGroups", "groups",
        },
        allowSetters = true
    )
    @OneToOne
    @JoinColumn(unique = true)
    private Person person;

    /**
     * When OrgUnit is subvillage, Which Chv covers it
     */
    @ApiModelProperty(value = "When OrgUnit is subvillage, Which Chv covers it")
    @OneToMany(mappedBy = "assignedChv")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "malariaReports",
            "dengueReports",
            "parent",
            "hfHomeSubVillage",
            "coveredByHf",
            "createdBy",
            "user",
            "lastUpdatedBy",
            "malariaUnit",
            "assignedChv",
            "children",
            "demographicData",
            "groups",
            "people",
            "dataViewPeople",
            "dataSets",
        },
        allowSetters = true
    )
    private Set<OrganisationUnit> coveredSubVillages = new HashSet<>();

    /**
     * The district Chv belongs to it When OrgUnit is district
     */
    @ApiModelProperty(value = "The district Chv belongs to it When OrgUnit is district")
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "malariaReports",
            "dengueReports",
            "parent",
            "hfHomeSubVillage",
            "coveredByHf",
            "createdBy",
            "user",
            "lastUpdatedBy",
            "malariaUnit",
            "assignedChv",
            "children",
            "demographicData",
            "groups",
            "people",
            "dataViewPeople",
            "dataSets",
        },
        allowSetters = true
    )
    private OrganisationUnit district;

    /**
     * When OrgUnit is SubVillage, What Chv it is home to
     */
    @ApiModelProperty(value = "When OrgUnit is SubVillage, What Chv it is home to")
    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "malariaReports",
            "dengueReports",
            "parent",
            "hfHomeSubVillage",
            "coveredByHf",
            "createdBy",
            "user",
            "lastUpdatedBy",
            "malariaUnit",
            "assignedChv",
            "children",
            "demographicData",
            "groups",
            "people",
            "dataViewPeople",
            "dataSets",
        },
        allowSetters = true
    )
    private OrganisationUnit homeSubvillage;

    /**
     * When OrgUnit is HealthFacility, What Chv it manage
     */
    @ApiModelProperty(value = "When OrgUnit is HealthFacility, What Chv it manage")
    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "malariaReports",
            "dengueReports",
            "parent",
            "hfHomeSubVillage",
            "coveredByHf",
            "createdBy",
            "user",
            "lastUpdatedBy",
            "malariaUnit",
            "assignedChv",
            "children",
            "demographicData",
            "groups",
            "people",
            "dataViewPeople",
            "dataSets",
        },
        allowSetters = true
    )
    private OrganisationUnit managedByHf;

    @ManyToMany(mappedBy = "responsibleForChvs")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "createdBy", "lastUpdatedBy", "person", "responsibleForChvs" }, allowSetters = true)
    private Set<ChvTeam> supervisionTeams = new HashSet<>();

    @JsonIgnoreProperties(value = { "imageAlbum", "createdBy", "lastUpdatedBy", "relatedLinks", "attachments" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private ContentPage contentPage;

    public Chv id(Long id) {
        this.id = id;
        return this;
    }

    public Chv uid(String uid) {
        this.uid = uid;
        return this;
    }

    public Chv code(String code) {
        this.code = code;
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public Chv description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Chv created(Date created) {
        this.created = created;
        return this;
    }

    public Chv lastUpdated(Date  lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public String getMobile() {
        return this.mobile;
    }

    public Chv mobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Person getPerson() {
        return this.person;
    }

    public Chv person(Person person) {
        this.setPerson(person);
        return this;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Set<OrganisationUnit> getCoveredSubVillages() {
        return this.coveredSubVillages;
    }

    public Chv coveredSubVillages(Set<OrganisationUnit> organisationUnits) {
        this.setCoveredSubVillages(organisationUnits);
        return this;
    }

    public Chv addCoveredSubVillage(OrganisationUnit organisationUnit) {
        this.coveredSubVillages.add(organisationUnit);
        organisationUnit.setAssignedChv(this);
        return this;
    }

    public Chv removeCoveredSubVillage(OrganisationUnit organisationUnit) {
        this.coveredSubVillages.remove(organisationUnit);
        organisationUnit.setAssignedChv(null);
        return this;
    }

    public void setCoveredSubVillages(Set<OrganisationUnit> organisationUnits) {
        if (this.coveredSubVillages != null) {
            this.coveredSubVillages.forEach(i -> i.setAssignedChv(null));
        }
        if (organisationUnits != null) {
            organisationUnits.forEach(i -> i.setAssignedChv(this));
        }
        this.coveredSubVillages = organisationUnits;
    }

    public OrganisationUnit getDistrict() {
        return this.district;
    }

    public Chv district(OrganisationUnit organisationUnit) {
        this.setDistrict(organisationUnit);
        return this;
    }

    public void setDistrict(OrganisationUnit organisationUnit) {
        this.district = organisationUnit;
    }

    public OrganisationUnit getHomeSubvillage() {
        return this.homeSubvillage;
    }

    public Chv homeSubvillage(OrganisationUnit organisationUnit) {
        this.setHomeSubvillage(organisationUnit);
        return this;
    }

    public void setHomeSubvillage(OrganisationUnit organisationUnit) {
        this.homeSubvillage = organisationUnit;
    }

    public OrganisationUnit getManagedByHf() {
        return this.managedByHf;
    }

    public Chv managedByHf(OrganisationUnit organisationUnit) {
        this.setManagedByHf(organisationUnit);
        return this;
    }

    public void setManagedByHf(OrganisationUnit organisationUnit) {
        this.managedByHf = organisationUnit;
    }

    public Chv user(User user) {
        this.setUser(user);
        return this;
    }

    public Chv lastUpdatedBy(User user) {
        this.setLastUpdatedBy(user);
        return this;
    }

    public Set<ChvTeam> getSupervisionTeams() {
        return this.supervisionTeams;
    }

    public Chv supervisionTeams(Set<ChvTeam> cHVTeams) {
        this.setSupervisionTeams(cHVTeams);
        return this;
    }

    public Chv addSupervisionTeam(ChvTeam cHVTeam) {
        this.supervisionTeams.add(cHVTeam);
        cHVTeam.getResponsibleForChvs().add(this);
        return this;
    }

    public Chv removeSupervisionTeam(ChvTeam cHVTeam) {
        this.supervisionTeams.remove(cHVTeam);
        cHVTeam.getResponsibleForChvs().remove(this);
        return this;
    }

    public void setSupervisionTeams(Set<ChvTeam> cHVTeams) {
        if (this.supervisionTeams != null) {
            this.supervisionTeams.forEach(i -> i.removeResponsibleForChv(this));
        }
        if (cHVTeams != null) {
            cHVTeams.forEach(i -> i.addResponsibleForChv(this));
        }
        this.supervisionTeams = cHVTeams;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUid() {
        return uid;
    }

    @Override
    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Date getCreated() {
        return created;
    }

    @Override
    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public Date getLastUpdated() {
        return lastUpdated;
    }

    @Override
    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public User getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public User getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    @Override
    public void setLastUpdatedBy(User lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    @JsonProperty
    @JsonSerialize(contentAs = BaseIdentifiableObject.class)
    public ContentPage getContentPage() {
        return contentPage;
    }

    public void setContentPage(ContentPage contentPage) {
        this.contentPage = contentPage;
    }

    public Chv contentPage(ContentPage contentPage) {
        this.setContentPage(contentPage);
        return this;
    }

}
