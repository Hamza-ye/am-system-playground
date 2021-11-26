import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LlinsFamilyReportHistoryComponent } from '../list/llins-family-report-history.component';
import { LlinsFamilyReportHistoryDetailComponent } from '../detail/llins-family-report-history-detail.component';
import { LlinsFamilyReportHistoryUpdateComponent } from '../update/llins-family-report-history-update.component';
import { LlinsFamilyReportHistoryRoutingResolveService } from './llins-family-report-history-routing-resolve.service';

const llinsFamilyReportHistoryRoute: Routes = [
  {
    path: '',
    component: LlinsFamilyReportHistoryComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LlinsFamilyReportHistoryDetailComponent,
    resolve: {
      llinsFamilyReportHistory: LlinsFamilyReportHistoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LlinsFamilyReportHistoryUpdateComponent,
    resolve: {
      llinsFamilyReportHistory: LlinsFamilyReportHistoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LlinsFamilyReportHistoryUpdateComponent,
    resolve: {
      llinsFamilyReportHistory: LlinsFamilyReportHistoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(llinsFamilyReportHistoryRoute)],
  exports: [RouterModule],
})
export class LlinsFamilyReportHistoryRoutingModule {}
