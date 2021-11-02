import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LLINSVillageReportHistoryComponent } from '../list/llins-village-report-history.component';
import { LLINSVillageReportHistoryDetailComponent } from '../detail/llins-village-report-history-detail.component';
import { LLINSVillageReportHistoryUpdateComponent } from '../update/llins-village-report-history-update.component';
import { LLINSVillageReportHistoryRoutingResolveService } from './llins-village-report-history-routing-resolve.service';

const lLINSVillageReportHistoryRoute: Routes = [
  {
    path: '',
    component: LLINSVillageReportHistoryComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LLINSVillageReportHistoryDetailComponent,
    resolve: {
      lLINSVillageReportHistory: LLINSVillageReportHistoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LLINSVillageReportHistoryUpdateComponent,
    resolve: {
      lLINSVillageReportHistory: LLINSVillageReportHistoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LLINSVillageReportHistoryUpdateComponent,
    resolve: {
      lLINSVillageReportHistory: LLINSVillageReportHistoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(lLINSVillageReportHistoryRoute)],
  exports: [RouterModule],
})
export class LLINSVillageReportHistoryRoutingModule {}
