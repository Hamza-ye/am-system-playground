import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ChvMalariaReportVersion1Component } from '../list/chv-malaria-report-version-1.component';
import { ChvMalariaReportVersion1DetailComponent } from '../detail/chv-malaria-report-version-1-detail.component';
import { ChvMalariaReportVersion1UpdateComponent } from '../update/chv-malaria-report-version-1-update.component';
import { ChvMalariaReportVersion1RoutingResolveService } from './chv-malaria-report-version-1-routing-resolve.service';

const chvMalariaReportVersion1Route: Routes = [
  {
    path: '',
    component: ChvMalariaReportVersion1Component,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ChvMalariaReportVersion1DetailComponent,
    resolve: {
      chvMalariaReportVersion1: ChvMalariaReportVersion1RoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ChvMalariaReportVersion1UpdateComponent,
    resolve: {
      chvMalariaReportVersion1: ChvMalariaReportVersion1RoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ChvMalariaReportVersion1UpdateComponent,
    resolve: {
      chvMalariaReportVersion1: ChvMalariaReportVersion1RoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(chvMalariaReportVersion1Route)],
  exports: [RouterModule],
})
export class ChvMalariaReportVersion1RoutingModule {}
