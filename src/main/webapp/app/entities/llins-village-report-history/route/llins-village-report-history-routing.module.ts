import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LlinsVillageReportHistoryComponent } from '../list/llins-village-report-history.component';
import { LlinsVillageReportHistoryDetailComponent } from '../detail/llins-village-report-history-detail.component';
import { LlinsVillageReportHistoryUpdateComponent } from '../update/llins-village-report-history-update.component';
import { LlinsVillageReportHistoryRoutingResolveService } from './llins-village-report-history-routing-resolve.service';

const llinsVillageReportHistoryRoute: Routes = [
  {
    path: '',
    component: LlinsVillageReportHistoryComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LlinsVillageReportHistoryDetailComponent,
    resolve: {
      llinsVillageReportHistory: LlinsVillageReportHistoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LlinsVillageReportHistoryUpdateComponent,
    resolve: {
      llinsVillageReportHistory: LlinsVillageReportHistoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LlinsVillageReportHistoryUpdateComponent,
    resolve: {
      llinsVillageReportHistory: LlinsVillageReportHistoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(llinsVillageReportHistoryRoute)],
  exports: [RouterModule],
})
export class LlinsVillageReportHistoryRoutingModule {}
