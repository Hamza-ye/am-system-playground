import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CHVMalariaReportVersion1Component } from '../list/chv-malaria-report-version-1.component';
import { CHVMalariaReportVersion1DetailComponent } from '../detail/chv-malaria-report-version-1-detail.component';
import { CHVMalariaReportVersion1UpdateComponent } from '../update/chv-malaria-report-version-1-update.component';
import { CHVMalariaReportVersion1RoutingResolveService } from './chv-malaria-report-version-1-routing-resolve.service';

const cHVMalariaReportVersion1Route: Routes = [
  {
    path: '',
    component: CHVMalariaReportVersion1Component,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CHVMalariaReportVersion1DetailComponent,
    resolve: {
      cHVMalariaReportVersion1: CHVMalariaReportVersion1RoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CHVMalariaReportVersion1UpdateComponent,
    resolve: {
      cHVMalariaReportVersion1: CHVMalariaReportVersion1RoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CHVMalariaReportVersion1UpdateComponent,
    resolve: {
      cHVMalariaReportVersion1: CHVMalariaReportVersion1RoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cHVMalariaReportVersion1Route)],
  exports: [RouterModule],
})
export class CHVMalariaReportVersion1RoutingModule {}
