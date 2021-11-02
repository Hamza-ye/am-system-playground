import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LLINSFamilyReportComponent } from '../list/llins-family-report.component';
import { LLINSFamilyReportDetailComponent } from '../detail/llins-family-report-detail.component';
import { LLINSFamilyReportUpdateComponent } from '../update/llins-family-report-update.component';
import { LLINSFamilyReportRoutingResolveService } from './llins-family-report-routing-resolve.service';

const lLINSFamilyReportRoute: Routes = [
  {
    path: '',
    component: LLINSFamilyReportComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LLINSFamilyReportDetailComponent,
    resolve: {
      lLINSFamilyReport: LLINSFamilyReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LLINSFamilyReportUpdateComponent,
    resolve: {
      lLINSFamilyReport: LLINSFamilyReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LLINSFamilyReportUpdateComponent,
    resolve: {
      lLINSFamilyReport: LLINSFamilyReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(lLINSFamilyReportRoute)],
  exports: [RouterModule],
})
export class LLINSFamilyReportRoutingModule {}
