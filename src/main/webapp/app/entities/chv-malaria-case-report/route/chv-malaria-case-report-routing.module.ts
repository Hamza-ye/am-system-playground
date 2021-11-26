import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ChvMalariaCaseReportComponent } from '../list/chv-malaria-case-report.component';
import { ChvMalariaCaseReportDetailComponent } from '../detail/chv-malaria-case-report-detail.component';
import { ChvMalariaCaseReportUpdateComponent } from '../update/chv-malaria-case-report-update.component';
import { ChvMalariaCaseReportRoutingResolveService } from './chv-malaria-case-report-routing-resolve.service';

const chvMalariaCaseReportRoute: Routes = [
  {
    path: '',
    component: ChvMalariaCaseReportComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ChvMalariaCaseReportDetailComponent,
    resolve: {
      chvMalariaCaseReport: ChvMalariaCaseReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ChvMalariaCaseReportUpdateComponent,
    resolve: {
      chvMalariaCaseReport: ChvMalariaCaseReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ChvMalariaCaseReportUpdateComponent,
    resolve: {
      chvMalariaCaseReport: ChvMalariaCaseReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(chvMalariaCaseReportRoute)],
  exports: [RouterModule],
})
export class ChvMalariaCaseReportRoutingModule {}
