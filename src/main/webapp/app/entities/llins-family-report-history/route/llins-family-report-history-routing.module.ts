import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LLINSFamilyReportHistoryComponent } from '../list/llins-family-report-history.component';
import { LLINSFamilyReportHistoryDetailComponent } from '../detail/llins-family-report-history-detail.component';
import { LLINSFamilyReportHistoryUpdateComponent } from '../update/llins-family-report-history-update.component';
import { LLINSFamilyReportHistoryRoutingResolveService } from './llins-family-report-history-routing-resolve.service';

const lLINSFamilyReportHistoryRoute: Routes = [
  {
    path: '',
    component: LLINSFamilyReportHistoryComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LLINSFamilyReportHistoryDetailComponent,
    resolve: {
      lLINSFamilyReportHistory: LLINSFamilyReportHistoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LLINSFamilyReportHistoryUpdateComponent,
    resolve: {
      lLINSFamilyReportHistory: LLINSFamilyReportHistoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LLINSFamilyReportHistoryUpdateComponent,
    resolve: {
      lLINSFamilyReportHistory: LLINSFamilyReportHistoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(lLINSFamilyReportHistoryRoute)],
  exports: [RouterModule],
})
export class LLINSFamilyReportHistoryRoutingModule {}
