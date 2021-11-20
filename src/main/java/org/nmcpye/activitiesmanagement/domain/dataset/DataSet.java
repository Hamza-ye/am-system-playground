package org.nmcpye.activitiesmanagement.domain.dataset;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Sets;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.joda.time.DateTime;
import org.nmcpye.activitiesmanagement.domain.DataInputPeriod;
import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnit;
import org.nmcpye.activitiesmanagement.domain.period.Period;
import org.nmcpye.activitiesmanagement.domain.period.PeriodType;
import org.nmcpye.activitiesmanagement.domain.person.PeopleGroup;
import org.nmcpye.activitiesmanagement.domain.person.Person;
import org.nmcpye.activitiesmanagement.extended.common.BaseDimensionalItemObject;
import org.nmcpye.activitiesmanagement.extended.common.BaseIdentifiableObject;
import org.nmcpye.activitiesmanagement.extended.common.DxfNamespaces;
import org.nmcpye.activitiesmanagement.extended.common.MetadataObject;
import org.nmcpye.activitiesmanagement.extended.common.adapter.JacksonPeriodTypeDeserializer;
import org.nmcpye.activitiesmanagement.extended.common.adapter.JacksonPeriodTypeSerializer;
import org.nmcpye.activitiesmanagement.extended.schema.PropertyType;
import org.nmcpye.activitiesmanagement.extended.schema.annotation.Property;
import org.nmcpye.activitiesmanagement.extended.security.Authorities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * A DataSet.
 */
@Entity
@Table(name = "data_set")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonRootName( value = "dataSet", namespace = DxfNamespaces.DXF_2_0 )
public class DataSet extends BaseDimensionalItemObject
    implements MetadataObject {

    public static final int NO_EXPIRY = 0;

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

    @Size(max = 50)
    @Column(name = "short_name", length = 50)
    private String shortName;

    private String description;

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

    @Column(name = "expiry_days")
    private Integer expiryDays;

    @Column(name = "timely_days")
    private Integer timelyDays;

    /**
     * The dataInputPeriods is a set of periods with opening and closing dates, which determines the period
     * of which data can belong (period) and at which dates (between opening and closing dates) actually registering
     * this data is allowed. The same period can exist at the same time with different opening and closing dates to
     * allow for multiple periods for registering data.
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "data_set_data_input_periods",
        joinColumns = @JoinColumn(name = "data_set_id"),
        inverseJoinColumns = @JoinColumn(name = "data_input_period_id")
    )
    private Set<DataInputPeriod> dataInputPeriods = new HashSet<>();

    @OneToMany(mappedBy = "dataSet")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = {"user", "createdBy", "lastUpdatedBy", "reportClass", "period", "dataSet", "organisationUnit"}, allowSetters = true)
    private Set<MalariaCasesReport> malariaCasesReports = new HashSet<>();

    @OneToMany(mappedBy = "dataSet")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = {"user", "createdBy", "lastUpdatedBy", "reportClass", "period", "dataSet", "organisationUnit"}, allowSetters = true)
    private Set<DengueCasesReport> dengueCasesReports = new HashSet<>();

    @ManyToOne
    private PeriodType periodType;

    @ManyToOne
    @JsonIgnoreProperties(value = {"user", "createdBy", "lastUpdatedBy", "members", "managedByGroups", "managedGroups"}, allowSetters = true)
    private PeopleGroup notificationRecipients;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "data_set_source",
        joinColumns = @JoinColumn(name = "data_set_id"),
        inverseJoinColumns = @JoinColumn(name = "source_id")
    )
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
    private Set<OrganisationUnit> sources = new HashSet<>();

    public DataSet() {
    }

    public DataSet(String name) {
        this.name = name;
    }

    public DataSet(String name, PeriodType periodType) {
        this(name);
        this.periodType = periodType;
    }

    public DataSet(String name, String shortName, PeriodType periodType) {
        this(name, periodType);
        this.shortName = shortName;
    }

    public DataSet(String name, String shortName, String code, PeriodType periodType) {
        this(name, shortName, periodType);
        this.code = code;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here
//    public Long getId() {
//        return id;
//    }

//    public void setId(Long id) {
//        this.id = id;
//    }

    public DataSet id(Long id) {
        this.id = id;
        return this;
    }

//    public String getUid() {
//        return this.uid;
//    }

    public DataSet uid(String uid) {
        this.uid = uid;
        return this;
    }

//    public void setUid(String uid) {
//        this.uid = uid;
//    }
//
//    public String getCode() {
//        return this.code;
//    }

    public DataSet code(String code) {
        this.code = code;
        return this;
    }

//    public void setCode(String code) {
//        this.code = code;
//    }

//    public String getName() {
//        return this.name;
//    }

    public DataSet name(String name) {
        this.name = name;
        return this;
    }

//    public void setName(String name) {
//        this.name = name;
//    }

//    public String getShortName() {
//        return this.shortName;
//    }

    public DataSet shortName(String shortName) {
        this.shortName = shortName;
        return this;
    }

//    public void setShortName(String shortName) {
//        this.shortName = shortName;
//    }

//    public String getDescription() {
//        return this.description;
//    }

    public DataSet description(String description) {
        this.description = description;
        return this;
    }

//    public void setDescription(String description) {
//        this.description = description;
//    }

//    public Date getCreated() {
//        return this.created;
//    }

    public DataSet created(Date created) {
        this.created = created;
        return this;
    }

//    public void setCreated(Date created) {
//        this.created = created;
//    }

//    public Date getLastUpdated() {
//        return this.lastUpdated;
//    }

    public DataSet lastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

//    public void setLastUpdated(Date lastUpdated) {
//        this.lastUpdated = lastUpdated;
//    }

    @JsonProperty
    public Integer getExpiryDays() {
        return this.expiryDays;
    }

    public DataSet expiryDays(Integer expiryDays) {
        this.expiryDays = expiryDays;
        return this;
    }

    public void setExpiryDays(Integer expiryDays) {
        this.expiryDays = expiryDays;
    }

    @JsonProperty
    public Integer getTimelyDays() {
        return this.timelyDays;
    }

    public DataSet timelyDays(Integer timelyDays) {
        this.timelyDays = timelyDays;
        return this;
    }

    public void setTimelyDays(Integer timelyDays) {
        this.timelyDays = timelyDays;
    }

    @JsonProperty
//    @JsonSerialize(contentAs = BaseIdentifiableObject.class)
    public Set<MalariaCasesReport> getMalariaCasesReports() {
        return this.malariaCasesReports;
    }

    public DataSet malariaCasesReports(Set<MalariaCasesReport> malariaCasesReports) {
        this.setMalariaCasesReports(malariaCasesReports);
        return this;
    }

    public DataSet addMalariaCasesReport(MalariaCasesReport malariaCasesReport) {
        this.malariaCasesReports.add(malariaCasesReport);
        malariaCasesReport.setDataSet(this);
        return this;
    }

    public DataSet removeMalariaCasesReport(MalariaCasesReport malariaCasesReport) {
        this.malariaCasesReports.remove(malariaCasesReport);
        malariaCasesReport.setDataSet(null);
        return this;
    }

    public void setMalariaCasesReports(Set<MalariaCasesReport> malariaCasesReports) {
        if (this.malariaCasesReports != null) {
            this.malariaCasesReports.forEach(i -> i.setDataSet(null));
        }
        if (malariaCasesReports != null) {
            malariaCasesReports.forEach(i -> i.setDataSet(this));
        }
        this.malariaCasesReports = malariaCasesReports;
    }

    @JsonProperty
//    @JsonSerialize(contentAs = BaseIdentifiableObject.class)
    public Set<DengueCasesReport> getDengueCasesReports() {
        return this.dengueCasesReports;
    }

    public DataSet dengueCasesReports(Set<DengueCasesReport> dengueCasesReports) {
        this.setDengueCasesReports(dengueCasesReports);
        return this;
    }

    public DataSet addDengueCasesReport(DengueCasesReport dengueCasesReport) {
        this.dengueCasesReports.add(dengueCasesReport);
        dengueCasesReport.setDataSet(this);
        return this;
    }

    public DataSet removeDengueCasesReport(DengueCasesReport dengueCasesReport) {
        this.dengueCasesReports.remove(dengueCasesReport);
        dengueCasesReport.setDataSet(null);
        return this;
    }

    public void setDengueCasesReports(Set<DengueCasesReport> dengueCasesReports) {
        if (this.dengueCasesReports != null) {
            this.dengueCasesReports.forEach(i -> i.setDataSet(null));
        }
        if (dengueCasesReports != null) {
            dengueCasesReports.forEach(i -> i.setDataSet(this));
        }
        this.dengueCasesReports = dengueCasesReports;
    }

    @JsonProperty
    @JsonSerialize( using = JacksonPeriodTypeSerializer.class )
    @JsonDeserialize( using = JacksonPeriodTypeDeserializer.class )
    @Property( PropertyType.TEXT )
    public PeriodType getPeriodType() {
        return this.periodType;
    }

    public DataSet periodType(PeriodType periodType) {
        this.setPeriodType(periodType);
        return this;
    }

    public void setPeriodType(PeriodType periodType) {
        this.periodType = periodType;
    }

    @JsonProperty
    @JsonSerialize(contentAs = BaseIdentifiableObject.class)
    public PeopleGroup getNotificationRecipients() {
        return this.notificationRecipients;
    }

    public DataSet notificationRecipients(PeopleGroup peopleGroup) {
        this.setNotificationRecipients(peopleGroup);
        return this;
    }

    public void setNotificationRecipients(PeopleGroup peopleGroup) {
        this.notificationRecipients = peopleGroup;
    }

//    public User getUser() {
//        return this.user;
//    }

    public DataSet user(User user) {
        this.setUser(user);
        return this;
    }

//    public void setUser(User user) {
//        this.user = user;
//    }

//    public User getLastUpdatedBy() {
//        return this.lastUpdatedBy;
//    }

    public DataSet lastUpdatedBy(User user) {
        this.setLastUpdatedBy(user);
        return this;
    }

//    public void setLastUpdatedBy(User user) {
//        this.lastUpdatedBy = user;
//    }

    @JsonProperty(value = "organisationUnits")
    @JsonSerialize(contentAs = BaseIdentifiableObject.class)
    public Set<OrganisationUnit> getSources() {
        return this.sources;
    }

    public DataSet sources(Set<OrganisationUnit> organisationUnits) {
        this.setSources(organisationUnits);
        return this;
    }

    public DataSet addOrganisationUnit(OrganisationUnit organisationUnit) {
        this.sources.add(organisationUnit);
        organisationUnit.getDataSets().add(this);
        return this;
    }

    public DataSet removeOrganisationUnit(OrganisationUnit organisationUnit) {
        this.sources.remove(organisationUnit);
        organisationUnit.getDataSets().remove(this);
        return this;
    }

    public void removeAllOrganisationUnits() {
        for (OrganisationUnit unit : sources) {
            unit.getDataSets().remove(this);
        }

        sources.clear();
    }

    public void updateOrganisationUnits(Set<OrganisationUnit> updates) {
        Set<OrganisationUnit> toRemove = Sets.difference(sources, updates);
        Set<OrganisationUnit> toAdd = Sets.difference(updates, sources);

        toRemove.forEach(u -> u.getDataSets().remove(this));
        toAdd.forEach(u -> u.getDataSets().add(this));

        sources.clear();
        sources.addAll(updates);
    }

    public boolean hasOrganisationUnit(OrganisationUnit unit) {
        return sources.contains(unit);
    }

    public void setSources(Set<OrganisationUnit> organisationUnits) {
        this.sources = organisationUnits;
    }

    @JsonProperty
    public Set<DataInputPeriod> getDataInputPeriods() {
        return dataInputPeriods;
    }

    public void setDataInputPeriods(Set<DataInputPeriod> dataInputPeriods) {
        this.dataInputPeriods = dataInputPeriods;
    }

    /**
     * Indicates whether the data set is locked for data entry based on the
     * expiry days.
     *
     * @param period the period to compare with.
     * @param now the date indicating now, uses current date if null.
     */
    public boolean isLocked(User person, Period period, Date now )
    {
        if ( person != null && person.isAuthorized( Authorities.F_EDIT_EXPIRED.getAuthority() ) )
        {
            return false;
        }

        DateTime date = now != null ? new DateTime( now ) : new DateTime();

        return expiryDays != DataSet.NO_EXPIRY &&
            new DateTime( period.getEndDate() ).plusDays( expiryDays ).isBefore( date );
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
    public String getShortName() {
        return shortName;
    }

    @Override
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
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
}
