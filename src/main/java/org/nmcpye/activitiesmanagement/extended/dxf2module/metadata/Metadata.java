package org.nmcpye.activitiesmanagement.extended.dxf2module.metadata;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.nmcpye.activitiesmanagement.domain.dataset.DataSet;
import org.nmcpye.activitiesmanagement.domain.JobConfiguration;
import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnit;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnitGroup;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnitGroupSet;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnitLevel;
import org.nmcpye.activitiesmanagement.domain.person.PeopleGroup;
import org.nmcpye.activitiesmanagement.domain.person.PersonAuthorityGroup;
import org.nmcpye.activitiesmanagement.extended.common.DimensionalObject;
import org.nmcpye.activitiesmanagement.extended.common.NameableObject;
import org.nmcpye.activitiesmanagement.extended.schemamodule.Schema;
import org.nmcpye.activitiesmanagement.domain.sqlview.SqlView;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class Metadata {
    private Date created;

    private final java.util.Map<Class<?>, List<?>> values = new HashMap<>();

    @SuppressWarnings("unchecked")
    public final <T> List<T> getValues(Class<T> type) {
        List<?> objects = values.get(type);
        return objects == null ? Collections.emptyList() : (List<T>) objects;
    }

    private <T> void setValues(Class<T> type, List<T> list) {
        if (list == null) {
            values.remove(type);
        } else {
            values.put(type, list);
        }
    }

    public Metadata() {
    }

    @JsonProperty
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @JsonProperty
    public List<Schema> getSchemas() {
        return getValues(Schema.class);
    }

    public void setSchemas(List<Schema> schemas) {
        setValues(Schema.class, schemas);
    }

//    @JsonProperty
//    public List<MetadataVersion> getMetadataVersions()
//    {
//        return getValues( MetadataVersion.class );
//    }
//
//    public void setMetadataVersions( List<MetadataVersion> metadataVersions )
//    {
//        setValues( MetadataVersion.class, metadataVersions );
//    }

    @JsonProperty
    public List<User> getUsers() {
        return getValues(User.class);
    }

    public void setUsers(List<User> users) {
        setValues(User.class, users);
    }

    @JsonProperty
    public List<PersonAuthorityGroup> getUserRoles() {
        return getValues(PersonAuthorityGroup.class);
    }

    public void setUserRoles(List<PersonAuthorityGroup> userRoles) {
        setValues(PersonAuthorityGroup.class, userRoles);
    }

    @JsonProperty
    public List<PeopleGroup> getUserGroups() {
        return getValues(PeopleGroup.class);
    }

    public void setUserGroups(List<PeopleGroup> userGroups) {
        setValues(PeopleGroup.class, userGroups);
    }

//    @JsonProperty
//    public List<Interpretation> getInterpretations()
//    {
//        return getValues( Interpretation.class );
//    }
//
//    public void setInterpretations( List<Interpretation> interpretations )
//    {
//        setValues( Interpretation.class, interpretations );
//    }

    @JsonProperty
    public List<NameableObject> getItems() {
        return getValues(NameableObject.class);
    }

    public void setItems(List<NameableObject> items) {
        setValues(NameableObject.class, items);
    }

    @JsonProperty
    public List<OrganisationUnit> getOrganisationUnits() {
        return getValues(OrganisationUnit.class);
    }

    public void setOrganisationUnits(List<OrganisationUnit> organisationUnits) {
        setValues(OrganisationUnit.class, organisationUnits);
    }

    @JsonProperty
    public List<OrganisationUnitGroup> getOrganisationUnitGroups() {
        return getValues(OrganisationUnitGroup.class);
    }

    public void setOrganisationUnitGroups(List<OrganisationUnitGroup> organisationUnitGroups) {
        setValues(OrganisationUnitGroup.class, organisationUnitGroups);
    }

    @JsonProperty
    public List<OrganisationUnitGroupSet> getOrganisationUnitGroupSets() {
        return getValues(OrganisationUnitGroupSet.class);
    }

    public void setOrganisationUnitGroupSets(List<OrganisationUnitGroupSet> organisationUnitGroupSets) {
        setValues(OrganisationUnitGroupSet.class, organisationUnitGroupSets);
    }

    @JsonProperty
    public List<OrganisationUnitLevel> getOrganisationUnitLevels() {
        return getValues(OrganisationUnitLevel.class);
    }

    public void setOrganisationUnitLevels(List<OrganisationUnitLevel> organisationUnitLevels) {
        setValues(OrganisationUnitLevel.class, organisationUnitLevels);
    }

    @JsonProperty
    public List<DataSet> getDataSets() {
        return getValues(DataSet.class);
    }

    public void setDataSets(List<DataSet> dataSets) {
        setValues(DataSet.class, dataSets);
    }

    @JsonProperty
    public List<SqlView> getSqlViews() {
        return getValues(SqlView.class);
    }

    public void setSqlViews(List<SqlView> sqlViews) {
        setValues(SqlView.class, sqlViews);
    }

//    @JsonProperty
//    public List<Document> getDocuments()
//    {
//        return getValues( Document.class );
//    }
//
//    public void setDocuments( List<Document> documents )
//    {
//        setValues( Document.class, documents );
//    }

//    @JsonProperty
//    public List<Constant> getConstants()
//    {
//        return getValues( Constant.class );
//    }
//
//    public void setConstants( List<Constant> constants )
//    {
//        setValues( Constant.class, constants );
//    }

    @JsonProperty
    public List<DimensionalObject> getDimensions() {
        return getValues(DimensionalObject.class);
    }

    public void setDimensions(List<DimensionalObject> dimensions) {
        setValues(DimensionalObject.class, dimensions);
    }

    @JsonProperty
    public List<JobConfiguration> getJobConfigurations() {
        return getValues(JobConfiguration.class);
    }

    public void setJobConfigurations(List<JobConfiguration> jobConfigurations) {
        setValues(JobConfiguration.class, jobConfigurations);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("MetaData{");
        str.append("created=").append(created);
        for (Entry<Class<?>, List<?>> e : values.entrySet()) {
            String key = e.getKey().getSimpleName();
            str.append(Character.toLowerCase(key.charAt(0)) + key.substring(1))
                .append(e.getValue().toString());
        }
        str.append("}");
        return str.toString();
    }
}
