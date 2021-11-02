import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MalariaCasesReportComponent } from '../list/malaria-cases-report.component';
import { MalariaCasesReportDetailComponent } from '../detail/malaria-cases-report-detail.component';
import { MalariaCasesReportUpdateComponent } from '../update/malaria-cases-report-update.component';
import { MalariaCasesReportRoutingResolveService } from './malaria-cases-report-routing-resolve.service';

const malariaCasesReportRoute: Routes = [
  {
    path: '',
    component: MalariaCasesReportComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MalariaCasesReportDetailComponent,
    resolve: {
      malariaCasesReport: MalariaCasesReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MalariaCasesReportUpdateComponent,
    resolve: {
      malariaCasesReport: MalariaCasesReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MalariaCasesReportUpdateComponent,
    resolve: {
      malariaCasesReport: MalariaCasesReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(malariaCasesReportRoute)],
  exports: [RouterModule],
})
export class MalariaCasesReportRoutingModule {}
