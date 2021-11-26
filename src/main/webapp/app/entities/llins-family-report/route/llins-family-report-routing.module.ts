import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LlinsFamilyReportComponent } from '../list/llins-family-report.component';
import { LlinsFamilyReportDetailComponent } from '../detail/llins-family-report-detail.component';
import { LlinsFamilyReportUpdateComponent } from '../update/llins-family-report-update.component';
import { LlinsFamilyReportRoutingResolveService } from './llins-family-report-routing-resolve.service';

const llinsFamilyReportRoute: Routes = [
  {
    path: '',
    component: LlinsFamilyReportComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LlinsFamilyReportDetailComponent,
    resolve: {
      llinsFamilyReport: LlinsFamilyReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LlinsFamilyReportUpdateComponent,
    resolve: {
      llinsFamilyReport: LlinsFamilyReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LlinsFamilyReportUpdateComponent,
    resolve: {
      llinsFamilyReport: LlinsFamilyReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(llinsFamilyReportRoute)],
  exports: [RouterModule],
})
export class LlinsFamilyReportRoutingModule {}
