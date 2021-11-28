import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'project',
        data: { pageTitle: 'amSystemPlaygroundApp.project.home.title' },
        loadChildren: () => import('./project/project.module').then(m => m.ProjectModule),
      },
      {
        path: 'activity',
        data: { pageTitle: 'amSystemPlaygroundApp.activity.home.title' },
        loadChildren: () => import('./activity/activity.module').then(m => m.ActivityModule),
      },
      {
        path: 'demographic-data',
        data: { pageTitle: 'amSystemPlaygroundApp.demographicData.home.title' },
        loadChildren: () => import('./demographic-data/demographic-data.module').then(m => m.DemographicDataModule),
      },
      {
        path: 'demographic-data-source',
        data: { pageTitle: 'amSystemPlaygroundApp.demographicDataSource.home.title' },
        loadChildren: () => import('./demographic-data-source/demographic-data-source.module').then(m => m.DemographicDataSourceModule),
      },
      {
        path: 'person',
        data: { pageTitle: 'amSystemPlaygroundApp.person.home.title' },
        loadChildren: () => import('./person/person.module').then(m => m.PersonModule),
      },
      {
        path: 'team',
        data: { pageTitle: 'amSystemPlaygroundApp.team.home.title' },
        loadChildren: () => import('./team/team.module').then(m => m.TeamModule),
      },
      {
        path: 'warehouse',
        data: { pageTitle: 'amSystemPlaygroundApp.warehouse.home.title' },
        loadChildren: () => import('./warehouse/warehouse.module').then(m => m.WarehouseModule),
      },
      {
        path: 'wh-movement',
        data: { pageTitle: 'amSystemPlaygroundApp.whMovement.home.title' },
        loadChildren: () => import('./wh-movement/wh-movement.module').then(m => m.WhMovementModule),
      },
      {
        path: 'family',
        data: { pageTitle: 'amSystemPlaygroundApp.family.home.title' },
        loadChildren: () => import('./family/family.module').then(m => m.FamilyModule),
      },
      {
        path: 'family-head',
        data: { pageTitle: 'amSystemPlaygroundApp.familyHead.home.title' },
        loadChildren: () => import('./family-head/family-head.module').then(m => m.FamilyHeadModule),
      },
      {
        path: 'data-provider',
        data: { pageTitle: 'amSystemPlaygroundApp.dataProvider.home.title' },
        loadChildren: () => import('./data-provider/data-provider.module').then(m => m.DataProviderModule),
      },
      {
        path: 'fingerprint',
        data: { pageTitle: 'amSystemPlaygroundApp.fingerprint.home.title' },
        loadChildren: () => import('./fingerprint/fingerprint.module').then(m => m.FingerprintModule),
      },
      {
        path: 'llins-village-target',
        data: { pageTitle: 'amSystemPlaygroundApp.llinsVillageTarget.home.title' },
        loadChildren: () => import('./llins-village-target/llins-village-target.module').then(m => m.LlinsVillageTargetModule),
      },
      {
        path: 'status-of-coverage',
        data: { pageTitle: 'amSystemPlaygroundApp.statusOfCoverage.home.title' },
        loadChildren: () => import('./status-of-coverage/status-of-coverage.module').then(m => m.StatusOfCoverageModule),
      },
      {
        path: 'llins-family-target',
        data: { pageTitle: 'amSystemPlaygroundApp.llinsFamilyTarget.home.title' },
        loadChildren: () => import('./llins-family-target/llins-family-target.module').then(m => m.LlinsFamilyTargetModule),
      },
      {
        path: 'llins-family-report',
        data: { pageTitle: 'amSystemPlaygroundApp.llinsFamilyReport.home.title' },
        loadChildren: () => import('./llins-family-report/llins-family-report.module').then(m => m.LlinsFamilyReportModule),
      },
      {
        path: 'llins-village-report',
        data: { pageTitle: 'amSystemPlaygroundApp.llinsVillageReport.home.title' },
        loadChildren: () => import('./llins-village-report/llins-village-report.module').then(m => m.LlinsVillageReportModule),
      },
      {
        path: 'llins-family-report-history',
        data: { pageTitle: 'amSystemPlaygroundApp.llinsFamilyReportHistory.home.title' },
        loadChildren: () =>
          import('./llins-family-report-history/llins-family-report-history.module').then(m => m.LlinsFamilyReportHistoryModule),
      },
      {
        path: 'llins-village-report-history',
        data: { pageTitle: 'amSystemPlaygroundApp.llinsVillageReportHistory.home.title' },
        loadChildren: () =>
          import('./llins-village-report-history/llins-village-report-history.module').then(m => m.LlinsVillageReportHistoryModule),
      },
      {
        path: 'working-day',
        data: { pageTitle: 'amSystemPlaygroundApp.workingDay.home.title' },
        loadChildren: () => import('./working-day/working-day.module').then(m => m.WorkingDayModule),
      },
      {
        path: 'malaria-unit',
        data: { pageTitle: 'amSystemPlaygroundApp.malariaUnit.home.title' },
        loadChildren: () => import('./malaria-unit/malaria-unit.module').then(m => m.MalariaUnitModule),
      },
      {
        path: 'malaria-unit-staff-member',
        data: { pageTitle: 'amSystemPlaygroundApp.malariaUnitStaffMember.home.title' },
        loadChildren: () =>
          import('./malaria-unit-staff-member/malaria-unit-staff-member.module').then(m => m.MalariaUnitStaffMemberModule),
      },
      {
        path: 'period-type',
        data: { pageTitle: 'amSystemPlaygroundApp.periodType.home.title' },
        loadChildren: () => import('./period-type/period-type.module').then(m => m.PeriodTypeModule),
      },
      {
        path: 'period',
        data: { pageTitle: 'amSystemPlaygroundApp.period.home.title' },
        loadChildren: () => import('./period/period.module').then(m => m.PeriodModule),
      },
      {
        path: 'malaria-cases-report',
        data: { pageTitle: 'amSystemPlaygroundApp.malariaCasesReport.home.title' },
        loadChildren: () => import('./malaria-cases-report/malaria-cases-report.module').then(m => m.MalariaCasesReportModule),
      },
      {
        path: 'dengue-cases-report',
        data: { pageTitle: 'amSystemPlaygroundApp.dengueCasesReport.home.title' },
        loadChildren: () => import('./dengue-cases-report/dengue-cases-report.module').then(m => m.DengueCasesReportModule),
      },
      {
        path: 'cases-report-class',
        data: { pageTitle: 'amSystemPlaygroundApp.casesReportClass.home.title' },
        loadChildren: () => import('./cases-report-class/cases-report-class.module').then(m => m.CasesReportClassModule),
      },
      {
        path: 'chv',
        data: { pageTitle: 'amSystemPlaygroundApp.chv.home.title' },
        loadChildren: () => import('./chv/chv.module').then(m => m.ChvModule),
      },
      {
        path: 'chv-team',
        data: { pageTitle: 'amSystemPlaygroundApp.chvTeam.home.title' },
        loadChildren: () => import('./chv-team/chv-team.module').then(m => m.ChvTeamModule),
      },
      {
        path: 'chv-malaria-report-version-1',
        data: { pageTitle: 'amSystemPlaygroundApp.chvMalariaReportVersion1.home.title' },
        loadChildren: () =>
          import('./chv-malaria-report-version-1/chv-malaria-report-version-1.module').then(m => m.ChvMalariaReportVersion1Module),
      },
      {
        path: 'chv-malaria-case-report',
        data: { pageTitle: 'amSystemPlaygroundApp.chvMalariaCaseReport.home.title' },
        loadChildren: () => import('./chv-malaria-case-report/chv-malaria-case-report.module').then(m => m.ChvMalariaCaseReportModule),
      },
      {
        path: 'organisation-unit',
        data: { pageTitle: 'amSystemPlaygroundApp.organisationUnit.home.title' },
        loadChildren: () => import('./organisation-unit/organisation-unit.module').then(m => m.OrganisationUnitModule),
      },
      {
        path: 'organisation-unit-group',
        data: { pageTitle: 'amSystemPlaygroundApp.organisationUnitGroup.home.title' },
        loadChildren: () => import('./organisation-unit-group/organisation-unit-group.module').then(m => m.OrganisationUnitGroupModule),
      },
      {
        path: 'organisation-unit-group-set',
        data: { pageTitle: 'amSystemPlaygroundApp.organisationUnitGroupSet.home.title' },
        loadChildren: () =>
          import('./organisation-unit-group-set/organisation-unit-group-set.module').then(m => m.OrganisationUnitGroupSetModule),
      },
      {
        path: 'organisation-unit-level',
        data: { pageTitle: 'amSystemPlaygroundApp.organisationUnitLevel.home.title' },
        loadChildren: () => import('./organisation-unit-level/organisation-unit-level.module').then(m => m.OrganisationUnitLevelModule),
      },
      {
        path: 'people-group',
        data: { pageTitle: 'amSystemPlaygroundApp.peopleGroup.home.title' },
        loadChildren: () => import('./people-group/people-group.module').then(m => m.PeopleGroupModule),
      },
      {
        path: 'person-authority-group',
        data: { pageTitle: 'amSystemPlaygroundApp.personAuthorityGroup.home.title' },
        loadChildren: () => import('./person-authority-group/person-authority-group.module').then(m => m.PersonAuthorityGroupModule),
      },
      {
        path: 'data-set',
        data: { pageTitle: 'amSystemPlaygroundApp.dataSet.home.title' },
        loadChildren: () => import('./data-set/data-set.module').then(m => m.DataSetModule),
      },
      {
        path: 'data-input-period',
        data: { pageTitle: 'amSystemPlaygroundApp.dataInputPeriod.home.title' },
        loadChildren: () => import('./data-input-period/data-input-period.module').then(m => m.DataInputPeriodModule),
      },
      {
        path: 'relative-periods',
        data: { pageTitle: 'amSystemPlaygroundApp.relativePeriods.home.title' },
        loadChildren: () => import('./relative-periods/relative-periods.module').then(m => m.RelativePeriodsModule),
      },
      {
        path: 'file-resource',
        data: { pageTitle: 'amSystemPlaygroundApp.fileResource.home.title' },
        loadChildren: () => import('./file-resource/file-resource.module').then(m => m.FileResourceModule),
      },
      {
        path: 'external-file-resource',
        data: { pageTitle: 'amSystemPlaygroundApp.externalFileResource.home.title' },
        loadChildren: () => import('./external-file-resource/external-file-resource.module').then(m => m.ExternalFileResourceModule),
      },
      {
        path: 'document',
        data: { pageTitle: 'amSystemPlaygroundApp.document.home.title' },
        loadChildren: () => import('./document/document.module').then(m => m.DocumentModule),
      },
      {
        path: 'content-page',
        data: { pageTitle: 'amSystemPlaygroundApp.contentPage.home.title' },
        loadChildren: () => import('./content-page/content-page.module').then(m => m.ContentPageModule),
      },
      {
        path: 'image-album',
        data: { pageTitle: 'amSystemPlaygroundApp.imageAlbum.home.title' },
        loadChildren: () => import('./image-album/image-album.module').then(m => m.ImageAlbumModule),
      },
      {
        path: 'related-link',
        data: { pageTitle: 'amSystemPlaygroundApp.relatedLink.home.title' },
        loadChildren: () => import('./related-link/related-link.module').then(m => m.RelatedLinkModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
