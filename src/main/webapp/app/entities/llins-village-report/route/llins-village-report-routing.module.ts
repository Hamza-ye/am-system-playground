import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LLINSVillageReportComponent } from '../list/llins-village-report.component';
import { LLINSVillageReportDetailComponent } from '../detail/llins-village-report-detail.component';
import { LLINSVillageReportUpdateComponent } from '../update/llins-village-report-update.component';
import { LLINSVillageReportRoutingResolveService } from './llins-village-report-routing-resolve.service';

const lLINSVillageReportRoute: Routes = [
  {
    path: '',
    component: LLINSVillageReportComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LLINSVillageReportDetailComponent,
    resolve: {
      lLINSVillageReport: LLINSVillageReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LLINSVillageReportUpdateComponent,
    resolve: {
      lLINSVillageReport: LLINSVillageReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LLINSVillageReportUpdateComponent,
    resolve: {
      lLINSVillageReport: LLINSVillageReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(lLINSVillageReportRoute)],
  exports: [RouterModule],
})
export class LLINSVillageReportRoutingModule {}
