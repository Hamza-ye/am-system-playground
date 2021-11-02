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
        data: { pageTitle: 'amSystemPlaygroundApp.wHMovement.home.title' },
        loadChildren: () => import('./wh-movement/wh-movement.module').then(m => m.WHMovementModule),
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
        data: { pageTitle: 'amSystemPlaygroundApp.lLINSVillageTarget.home.title' },
        loadChildren: () => import('./llins-village-target/llins-village-target.module').then(m => m.LLINSVillageTargetModule),
      },
      {
        path: 'status-of-coverage',
        data: { pageTitle: 'amSystemPlaygroundApp.statusOfCoverage.home.title' },
        loadChildren: () => import('./status-of-coverage/status-of-coverage.module').then(m => m.StatusOfCoverageModule),
      },
      {
        path: 'llins-family-target',
        data: { pageTitle: 'amSystemPlaygroundApp.lLINSFamilyTarget.home.title' },
        loadChildren: () => import('./llins-family-target/llins-family-target.module').then(m => m.LLINSFamilyTargetModule),
      },
      {
        path: 'llins-family-report',
        data: { pageTitle: 'amSystemPlaygroundApp.lLINSFamilyReport.home.title' },
        loadChildren: () => import('./llins-family-report/llins-family-report.module').then(m => m.LLINSFamilyReportModule),
      },
      {
        path: 'llins-village-report',
        data: { pageTitle: 'amSystemPlaygroundApp.lLINSVillageReport.home.title' },
        loadChildren: () => import('./llins-village-report/llins-village-report.module').then(m => m.LLINSVillageReportModule),
      },
      {
        path: 'llins-family-report-history',
        data: { pageTitle: 'amSystemPlaygroundApp.lLINSFamilyReportHistory.home.title' },
        loadChildren: () =>
          import('./llins-family-report-history/llins-family-report-history.module').then(m => m.LLINSFamilyReportHistoryModule),
      },
      {
        path: 'llins-village-report-history',
        data: { pageTitle: 'amSystemPlaygroundApp.lLINSVillageReportHistory.home.title' },
        loadChildren: () =>
          import('./llins-village-report-history/llins-village-report-history.module').then(m => m.LLINSVillageReportHistoryModule),
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
        data: { pageTitle: 'amSystemPlaygroundApp.cHV.home.title' },
        loadChildren: () => import('./chv/chv.module').then(m => m.CHVModule),
      },
      {
        path: 'chv-team',
        data: { pageTitle: 'amSystemPlaygroundApp.cHVTeam.home.title' },
        loadChildren: () => import('./chv-team/chv-team.module').then(m => m.CHVTeamModule),
      },
      {
        path: 'chv-malaria-report-version-1',
        data: { pageTitle: 'amSystemPlaygroundApp.cHVMalariaReportVersion1.home.title' },
        loadChildren: () =>
          import('./chv-malaria-report-version-1/chv-malaria-report-version-1.module').then(m => m.CHVMalariaReportVersion1Module),
      },
      {
        path: 'chv-malaria-case-report',
        data: { pageTitle: 'amSystemPlaygroundApp.cHVMalariaCaseReport.home.title' },
        loadChildren: () => import('./chv-malaria-case-report/chv-malaria-case-report.module').then(m => m.CHVMalariaCaseReportModule),
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
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
