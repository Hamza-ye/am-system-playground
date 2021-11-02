import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DengueCasesReportComponent } from '../list/dengue-cases-report.component';
import { DengueCasesReportDetailComponent } from '../detail/dengue-cases-report-detail.component';
import { DengueCasesReportUpdateComponent } from '../update/dengue-cases-report-update.component';
import { DengueCasesReportRoutingResolveService } from './dengue-cases-report-routing-resolve.service';

const dengueCasesReportRoute: Routes = [
  {
    path: '',
    component: DengueCasesReportComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DengueCasesReportDetailComponent,
    resolve: {
      dengueCasesReport: DengueCasesReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DengueCasesReportUpdateComponent,
    resolve: {
      dengueCasesReport: DengueCasesReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DengueCasesReportUpdateComponent,
    resolve: {
      dengueCasesReport: DengueCasesReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(dengueCasesReportRoute)],
  exports: [RouterModule],
})
export class DengueCasesReportRoutingModule {}
