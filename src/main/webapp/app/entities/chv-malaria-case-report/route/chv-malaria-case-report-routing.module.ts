import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CHVMalariaCaseReportComponent } from '../list/chv-malaria-case-report.component';
import { CHVMalariaCaseReportDetailComponent } from '../detail/chv-malaria-case-report-detail.component';
import { CHVMalariaCaseReportUpdateComponent } from '../update/chv-malaria-case-report-update.component';
import { CHVMalariaCaseReportRoutingResolveService } from './chv-malaria-case-report-routing-resolve.service';

const cHVMalariaCaseReportRoute: Routes = [
  {
    path: '',
    component: CHVMalariaCaseReportComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CHVMalariaCaseReportDetailComponent,
    resolve: {
      cHVMalariaCaseReport: CHVMalariaCaseReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CHVMalariaCaseReportUpdateComponent,
    resolve: {
      cHVMalariaCaseReport: CHVMalariaCaseReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CHVMalariaCaseReportUpdateComponent,
    resolve: {
      cHVMalariaCaseReport: CHVMalariaCaseReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cHVMalariaCaseReportRoute)],
  exports: [RouterModule],
})
export class CHVMalariaCaseReportRoutingModule {}
