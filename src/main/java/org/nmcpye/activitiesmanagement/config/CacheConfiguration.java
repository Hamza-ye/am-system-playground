package org.nmcpye.activitiesmanagement.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.nmcpye.activitiesmanagement.domain.activity.Activity;
import org.nmcpye.activitiesmanagement.domain.chv.CHV;
import org.nmcpye.activitiesmanagement.domain.dataset.CasesReportClass;
import org.nmcpye.activitiesmanagement.domain.dataset.DataSet;
import org.nmcpye.activitiesmanagement.domain.dataset.DengueCasesReport;
import org.nmcpye.activitiesmanagement.domain.dataset.MalariaCasesReport;
import org.nmcpye.activitiesmanagement.domain.demographicdata.DemographicData;
import org.nmcpye.activitiesmanagement.domain.demographicdata.DemographicDataSource;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnit;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnitGroup;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnitGroupSet;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnitLevel;
import org.nmcpye.activitiesmanagement.domain.period.Period;
import org.nmcpye.activitiesmanagement.domain.period.PeriodType;
import org.nmcpye.activitiesmanagement.domain.period.RelativePeriods;
import org.nmcpye.activitiesmanagement.domain.person.PeopleGroup;
import org.nmcpye.activitiesmanagement.domain.person.Person;
import org.nmcpye.activitiesmanagement.domain.person.PersonAuthorityGroup;
import org.nmcpye.activitiesmanagement.domain.project.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, org.nmcpye.activitiesmanagement.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, org.nmcpye.activitiesmanagement.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, org.nmcpye.activitiesmanagement.domain.User.class.getName());
            createCache(cm, org.nmcpye.activitiesmanagement.domain.Authority.class.getName());
            createCache(cm, org.nmcpye.activitiesmanagement.domain.User.class.getName() + ".authorities");
            createCache(cm, Project.class.getName());
            createCache(cm, Project.class.getName() + ".activities");
            createCache(cm, Activity.class.getName());
            createCache(cm, Activity.class.getName() + ".warehouses");
            createCache(cm, DemographicData.class.getName());
            createCache(cm, DemographicDataSource.class.getName());
            createCache(cm, DemographicDataSource.class.getName() + ".demographicData");
            createCache(cm, Person.class.getName());
            createCache(cm, Person.class.getName() + ".organisationUnits");
            createCache(cm, Person.class.getName() + ".dataViewOrganisationUnits");
            createCache(cm, Person.class.getName() + ".personAuthorityGroups");
            createCache(cm, Person.class.getName() + ".groups");
            createCache(cm, org.nmcpye.activitiesmanagement.domain.Team.class.getName());
            createCache(cm, org.nmcpye.activitiesmanagement.domain.Team.class.getName() + ".whMovements");
            createCache(cm, org.nmcpye.activitiesmanagement.domain.Team.class.getName() + ".llinsVillageTargets");
            createCache(cm, org.nmcpye.activitiesmanagement.domain.Team.class.getName() + ".llinsVillageReports");
            createCache(cm, org.nmcpye.activitiesmanagement.domain.Team.class.getName() + ".llinsFamilyTargets");
            createCache(cm, org.nmcpye.activitiesmanagement.domain.Team.class.getName() + ".llinsFamilyReports");
            createCache(cm, org.nmcpye.activitiesmanagement.domain.Team.class.getName() + ".assignedToWarehouses");
            createCache(cm, org.nmcpye.activitiesmanagement.domain.Warehouse.class.getName());
            createCache(cm, org.nmcpye.activitiesmanagement.domain.Warehouse.class.getName() + ".initiatedMovements");
            createCache(cm, org.nmcpye.activitiesmanagement.domain.Warehouse.class.getName() + ".notInitiatedMovements");
            createCache(cm, org.nmcpye.activitiesmanagement.domain.Warehouse.class.getName() + ".teams");
            createCache(cm, org.nmcpye.activitiesmanagement.domain.WHMovement.class.getName());
            createCache(cm, org.nmcpye.activitiesmanagement.domain.Family.class.getName());
            createCache(cm, org.nmcpye.activitiesmanagement.domain.Family.class.getName() + ".familyHeads");
            createCache(cm, org.nmcpye.activitiesmanagement.domain.Family.class.getName() + ".dataProviders");
            createCache(cm, org.nmcpye.activitiesmanagement.domain.Family.class.getName() + ".fingerprints");
            createCache(cm, org.nmcpye.activitiesmanagement.domain.Family.class.getName() + ".llinsFamilyTargets");
            createCache(cm, org.nmcpye.activitiesmanagement.domain.FamilyHead.class.getName());
            createCache(cm, org.nmcpye.activitiesmanagement.domain.DataProvider.class.getName());
            createCache(cm, org.nmcpye.activitiesmanagement.domain.Fingerprint.class.getName());
            createCache(cm, org.nmcpye.activitiesmanagement.domain.LLINSVillageTarget.class.getName());
            createCache(cm, org.nmcpye.activitiesmanagement.domain.LLINSVillageTarget.class.getName() + ".llinsVillageReports");
            createCache(cm, org.nmcpye.activitiesmanagement.domain.StatusOfCoverage.class.getName());
            createCache(cm, org.nmcpye.activitiesmanagement.domain.LLINSFamilyTarget.class.getName());
            createCache(cm, org.nmcpye.activitiesmanagement.domain.LLINSFamilyTarget.class.getName() + ".llinsFamilyReports");
            createCache(cm, org.nmcpye.activitiesmanagement.domain.LLINSFamilyReport.class.getName());
            createCache(cm, org.nmcpye.activitiesmanagement.domain.LLINSFamilyReport.class.getName() + ".llinsFamilyReportHistories");
            createCache(cm, org.nmcpye.activitiesmanagement.domain.LLINSVillageReport.class.getName());
            createCache(cm, org.nmcpye.activitiesmanagement.domain.LLINSVillageReport.class.getName() + ".llinsVillageReportHistories");
            createCache(cm, org.nmcpye.activitiesmanagement.domain.LLINSFamilyReportHistory.class.getName());
            createCache(cm, org.nmcpye.activitiesmanagement.domain.LLINSVillageReportHistory.class.getName());
            createCache(cm, org.nmcpye.activitiesmanagement.domain.WorkingDay.class.getName());
            createCache(cm, org.nmcpye.activitiesmanagement.domain.MalariaUnit.class.getName());
            createCache(cm, org.nmcpye.activitiesmanagement.domain.MalariaUnit.class.getName() + ".organisationUnits");
            createCache(cm, org.nmcpye.activitiesmanagement.domain.MalariaUnit.class.getName() + ".malariaUnitStaffMembers");
            createCache(cm, org.nmcpye.activitiesmanagement.domain.MalariaUnitStaffMember.class.getName());
            createCache(cm, PeriodType.class.getName());
            createCache(cm, Period.class.getName());
            createCache(cm, MalariaCasesReport.class.getName());
            createCache(cm, DengueCasesReport.class.getName());
            createCache(cm, CasesReportClass.class.getName());
            createCache(cm, CHV.class.getName());
            createCache(cm, CHV.class.getName() + ".coveredSubVillages");
            createCache(cm, CHV.class.getName() + ".supervisionTeams");
            createCache(cm, org.nmcpye.activitiesmanagement.domain.CHVTeam.class.getName());
            createCache(cm, org.nmcpye.activitiesmanagement.domain.CHVTeam.class.getName() + ".responsibleForChvs");
            createCache(cm, org.nmcpye.activitiesmanagement.domain.CHVMalariaReportVersion1.class.getName());
            createCache(cm, org.nmcpye.activitiesmanagement.domain.CHVMalariaCaseReport.class.getName());
            createCache(cm, OrganisationUnit.class.getName());
            createCache(cm, OrganisationUnit.class.getName() + ".malariaReports");
            createCache(cm, OrganisationUnit.class.getName() + ".dengueReports");
            createCache(cm, OrganisationUnit.class.getName() + ".children");
            createCache(cm, OrganisationUnit.class.getName() + ".demographicData");
            createCache(cm, OrganisationUnit.class.getName() + ".groups");
            createCache(cm, OrganisationUnit.class.getName() + ".people");
            createCache(cm, OrganisationUnit.class.getName() + ".dataViewPeople");
            createCache(cm, OrganisationUnit.class.getName() + ".dataSets");
            createCache(cm, OrganisationUnitGroup.class.getName());
            createCache(cm, OrganisationUnitGroup.class.getName() + ".members");
            createCache(cm, OrganisationUnitGroup.class.getName() + ".groupSets");
            createCache(cm, OrganisationUnitGroupSet.class.getName());
            createCache(cm, OrganisationUnitGroupSet.class.getName() + ".organisationUnitGroups");
            createCache(cm, OrganisationUnitLevel.class.getName());
            createCache(cm, PeopleGroup.class.getName());
            createCache(cm, PeopleGroup.class.getName() + ".members");
            createCache(cm, PeopleGroup.class.getName() + ".managedByGroups");
            createCache(cm, PeopleGroup.class.getName() + ".managedGroups");
            createCache(cm, PersonAuthorityGroup.class.getName());
            createCache(cm, PersonAuthorityGroup.class.getName() + ".members");
            createCache(cm, DataSet.class.getName());
            createCache(cm, DataSet.class.getName() + ".malariaCasesReports");
            createCache(cm, DataSet.class.getName() + ".dengueCasesReports");
            createCache(cm, DataSet.class.getName() + ".sources");
            createCache(cm, org.nmcpye.activitiesmanagement.domain.DataInputPeriod.class.getName());
            createCache(cm, RelativePeriods.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
