import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LlinsVillageReportComponent } from '../list/llins-village-report.component';
import { LlinsVillageReportDetailComponent } from '../detail/llins-village-report-detail.component';
import { LlinsVillageReportUpdateComponent } from '../update/llins-village-report-update.component';
import { LlinsVillageReportRoutingResolveService } from './llins-village-report-routing-resolve.service';

const llinsVillageReportRoute: Routes = [
  {
    path: '',
    component: LlinsVillageReportComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LlinsVillageReportDetailComponent,
    resolve: {
      llinsVillageReport: LlinsVillageReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LlinsVillageReportUpdateComponent,
    resolve: {
      llinsVillageReport: LlinsVillageReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LlinsVillageReportUpdateComponent,
    resolve: {
      llinsVillageReport: LlinsVillageReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(llinsVillageReportRoute)],
  exports: [RouterModule],
})
export class LlinsVillageReportRoutingModule {}
